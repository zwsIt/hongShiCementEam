package com.supcon.mes.module_main.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.listener.OnTextListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.UserPowerCheckController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.RepairGroupEntity;
import com.supcon.mes.middleware.model.bean.RepairGroupEntityDao;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.api.WaitDealtAPI;
import com.supcon.mes.module_main.model.api.WaitDealtSubmitAPI;
import com.supcon.mes.module_main.model.bean.WaitDealtEntity;
import com.supcon.mes.module_main.model.contract.WaitDealtContract;
import com.supcon.mes.module_main.model.contract.WaitDealtSubmitContract;
import com.supcon.mes.module_main.presenter.WaitDealtPresenter;
import com.supcon.mes.module_main.presenter.WaitDealtSubmitPresenter;
import com.supcon.mes.module_main.ui.adaper.WaitDealtAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/5/11
 * Email zhangwenshuai1@supcon.com
 * Desc 工作提醒CurrentWorkFragment
 */
@Deprecated
@Presenter(value = {WaitDealtPresenter.class, WaitDealtSubmitPresenter.class})
public class HomeCurrentWorkFragment extends BaseRefreshRecyclerFragment<WaitDealtEntity> implements WaitDealtContract.View, WaitDealtSubmitContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private WaitDealtAdapter waitDealtAdapter;
    private Map<String, Object> queryParam = new HashMap<>();
    private CommonSearchStaff searchStaff;
    private List<CommonSearchEntity> mSelectStaffList;
    private CustomDialog proxyDialog;
    private String reason;

    @Override
    protected IListAdapter<WaitDealtEntity> createAdapter() {
        waitDealtAdapter = new WaitDealtAdapter(context);
        return waitDealtAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.frag_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(5,context),DisplayUtil.dip2px(2,context),DisplayUtil.dip2px(5,context),0);
            }
        });

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        refreshListController.setOnRefreshListener(() -> presenterRouter.create(WaitDealtAPI.class).getWaitDealt(1, 2, queryParam));

        waitDealtAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                WaitDealtEntity waitDealtEntity = (WaitDealtEntity) obj;
                if (childView.getId() == R.id.waitDealtEntrust) {
                    proxyDialog(waitDealtEntity);
                }
            }
        });
    }

    /**
     * 委托代办
     *
     * @param waitDealtEntity
     */
    private void proxyDialog(WaitDealtEntity waitDealtEntity) {
        proxyDialog = new CustomDialog(context).layout(R.layout.proxy_dialog,
                DisplayUtil.getScreenWidth(context) * 2 / 3, WRAP_CONTENT)
                .bindView(R.id.blueBtn, "确定")
                .bindView(R.id.grayBtn, "取消")
                .bindChildListener(R.id.proxyPerson, new OnChildViewClickListener() {
                    @Override
                    public void onChildViewClick(View childView, int action, Object obj) {
                        if (action == -1) {
                            mSelectStaffList = null;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.IntentKey.IS_MULTI, true);
                        bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
//                        IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
                        IntentRouter.go(context, Constant.Router.STAFF,bundle);
                    }
                })
                .bindTextChangeListener(R.id.proxyReason, new OnTextListener() {
                    @Override
                    public void onText(String text) {
                        reason = text.trim();
                    }
                })
                .bindClickListener(R.id.blueBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v12) {
                        if (mSelectStaffList == null) {
                            ToastUtils.show(context, getResources().getString(R.string.main_select_assignor));
                            return;
                        }
                        if (waitDealtEntity.pendingId == null) {
                            ToastUtils.show(context, context.getResources().getString(R.string.no_get_pending));
                            return;
                        }
                        onLoading(getResources().getString(R.string.main_do_proxying));
                        StringBuilder sb = new StringBuilder();
                        for (CommonSearchEntity commonSearchEntity : mSelectStaffList){
                            sb.append(((CommonSearchStaff)commonSearchEntity).userId).append(",");
                        }
                        presenterRouter.create(WaitDealtAPI.class).proxyPending(waitDealtEntity.pendingId, sb.toString(), reason);
                        proxyDialog.dismiss();
                    }
                }, false)
                .bindClickListener(R.id.grayBtn, null, true);
        ((CustomEditText) proxyDialog.getDialog().findViewById(R.id.proxyReason)).editText().setScrollBarSize(0);
        proxyDialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        proxyDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {
        refreshListController.refreshBegin();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity != null) {
            if (commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff) {
                searchStaff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;
                if (proxyDialog != null && proxyDialog.getDialog().isShowing()) {
                    CustomTextView person = proxyDialog.getDialog().findViewById(R.id.proxyPerson);
                    person.setContent(Util.strFormat(searchStaff.name));
                }
            }
        }else if (commonSearchEvent.mCommonSearchEntityList != null){
            mSelectStaffList = commonSearchEvent.mCommonSearchEntityList;
            StringBuilder searchStaffs = new StringBuilder();
            for (CommonSearchEntity commonSearchEntity : mSelectStaffList){
                searchStaffs.append(((CommonSearchStaff)commonSearchEntity).getName()).append(",");
            }
            if (proxyDialog != null && proxyDialog.getDialog().isShowing()) {
                CustomTextView person = proxyDialog.getDialog().findViewById(R.id.proxyPerson);
                person.setContent(Util.strFormat(searchStaffs.substring(0,searchStaffs.length()-1)));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        refreshListController.refreshBegin();
    }

    @Override
    public void getWaitDealtSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getWaitDealtFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }

    @Override
    public void proxyPendingSuccess(BapResultEntity entity) {
        onLoadSuccess("待办委托成功");
        refreshListController.refreshBegin();
    }

    @Override
    public void proxyPendingFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void bulkSubmitCustomSuccess(ResultEntity entity) {
        onLoadSuccess("派单成功");
        refreshListController.refreshBegin();
    }

    @Override
    public void bulkSubmitCustomFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (rootView != null) {
//            ((ViewGroup) rootView.getParent()).removeView(rootView);
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    public void doFilter(String processKey) {
        queryParam.put(Constant.BAPQuery.PROCESSKEY, processKey);
        refreshListController.refreshBegin();
    }
}
