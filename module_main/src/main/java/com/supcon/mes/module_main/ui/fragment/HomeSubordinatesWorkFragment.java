package com.supcon.mes.module_main.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.listener.OnTextListener;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.api.WaitDealtAPI;
import com.supcon.mes.module_main.model.bean.WaitDealtEntity;
import com.supcon.mes.module_main.model.contract.WaitDealtContract;
import com.supcon.mes.module_main.model.contract.WaitDealtSubmitContract;
import com.supcon.mes.module_main.presenter.WaitDealtPresenter;
import com.supcon.mes.module_main.ui.adaper.WaitDealtAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/5/11
 * Email zhangwenshuai1@supcon.com
 * Desc 下属工作提醒SubordinatesWorkFragment
 */
@Deprecated
@Presenter(value = {WaitDealtPresenter.class})
public class HomeSubordinatesWorkFragment extends BaseRefreshRecyclerFragment<WaitDealtEntity> implements WaitDealtContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private WaitDealtAdapter waitDealtAdapter;
    private Map<String, Object> queryParam = new HashMap<>();

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

        waitDealtAdapter.setSubordinate(true);
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

        refreshListController.setOnRefreshListener(() -> {
            queryParam.put(Constant.BAPQuery.SUBORDINATE,EamApplication.getAccountInfo().departmentId);
            presenterRouter.create(WaitDealtAPI.class).getWaitDealt(1, 2, queryParam);
        });
        
        waitDealtAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            WaitDealtEntity waitDealtEntity = (WaitDealtEntity) obj;
            Bundle bundle = new Bundle();
            /*switch (waitDealtEntity.processKey){
                case Constant.ProcessKey.WORK_TICKET:
                    goWorkTicket(waitDealtEntity, bundle);
                    break;
                case Constant.ProcessKey.ELE_ON:
                    bundle.putLong(Constant.IntentKey.TABLE_ID, waitDealtEntity.tableId);
                    IntentRouter.go(context,Constant.Router.HS_ELE_ON_VIEW,bundle);
                    break;
                case Constant.ProcessKey.ELE_OFF:
                    bundle.putLong(Constant.IntentKey.TABLE_ID, waitDealtEntity.tableId);
                    IntentRouter.go(context,Constant.Router.HS_ELE_OFF_VIEW,bundle);
                    break;
//                case Constant.ProcessKey.RUN_STATE_WF:
//                    break;
//                case Constant.ProcessKey.CHECK_APPLY_FW:
//                    break;
                case Constant.ProcessKey.FAULT_INFO:
                    YHEntity yhEntity = new YHEntity();
                    yhEntity.tableNo = waitDealtEntity.workTableNo;
                    bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY,yhEntity);
                    IntentRouter.go(context, Constant.Router.YH_LOOK, bundle);
                    break;
                case Constant.ProcessKey.WORK:
                    WXGDEntity wxgdEntity = new WXGDEntity();
                    wxgdEntity.tableNo = waitDealtEntity.workTableNo;
                    bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY,wxgdEntity);
                    IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                    break;
                default:
                    ToastUtils.show(context, context.getResources().getString(R.string.main_processed_table_no_view));
            }*/
        });
    }

    /**
     * @method
     * @description 跳转检修作业票
     * @author: zhangwenshuai
     * @date: 2020/5/30 15:13
     * @param  * @param null
     * @return
     */
    private void goWorkTicket(WaitDealtEntity waitDealtEntity, Bundle bundle) {
        if (!TextUtils.isEmpty(waitDealtEntity.summary) && waitDealtEntity.summary.contains("offApplyTableinfoid")) {
            try {
                String json = waitDealtEntity.summary.substring(waitDealtEntity.summary.indexOf("*") +1);
                if (GsonUtil.gsonToMaps(json).get("offApplyTableinfoid") != null) {
                    Double offApplyTableInfoId = (Double) GsonUtil.gsonToMaps(json).get("offApplyTableinfoid");
                    bundle.putLong(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID, Objects.requireNonNull(offApplyTableInfoId).longValue()); // 停电作业票tableInfoId
                }
            } catch (Exception e) {
                e.printStackTrace();
                bundle.putLong(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID,-1);
            }
        }
        bundle.putLong(Constant.IntentKey.TABLE_ID, waitDealtEntity.tableId);
        IntentRouter.go(context, Constant.Router.OVERHAUL_WORKTICKET_VIEW, bundle);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {
        refreshListController.refreshBegin();
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

    }

    @Override
    public void proxyPendingFailed(String errorMsg) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

}
