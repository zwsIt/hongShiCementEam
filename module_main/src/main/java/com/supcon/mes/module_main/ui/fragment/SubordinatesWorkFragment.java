package com.supcon.mes.module_main.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.supcon.mes.middleware.util.ProcessKeyUtil;
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
@Presenter(value = {WaitDealtPresenter.class})
public class SubordinatesWorkFragment extends BaseRefreshRecyclerFragment<WaitDealtEntity> implements WaitDealtContract.View, WaitDealtSubmitContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private WaitDealtAdapter waitDealtAdapter;
    private SinglePickController mSinglePickController;
    private Map<String, Object> queryParam = new HashMap<>();
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
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5,context)));

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        waitDealtAdapter.setSubordinate(true);

        mSinglePickController = new SinglePickController(getActivity());
        mSinglePickController.setDividerVisible(false);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.textSize(18);
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

        refreshListController.setOnRefreshPageListener(pageIndex -> {
            queryParam.put(Constant.BAPQuery.SUBORDINATE,EamApplication.getAccountInfo().departmentId);
            presenterRouter.create(WaitDealtAPI.class).getWaitDealt(pageIndex, 20, queryParam);
        });

        waitDealtAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                WaitDealtEntity waitDealtEntity = (WaitDealtEntity) obj;
                Bundle bundle = new Bundle();

                if (waitDealtEntity.processKey.equals(ProcessKeyUtil.WORK_TICKET)){
                    goWorkTicket(waitDealtEntity, bundle);
                }else if (waitDealtEntity.processKey.equals(ProcessKeyUtil.ELE_ON)){
                    bundle.putLong(Constant.IntentKey.TABLE_ID, waitDealtEntity.tableId);
                    IntentRouter.go(context,Constant.Router.HS_ELE_ON_VIEW,bundle);
                }else if (waitDealtEntity.processKey.equals(ProcessKeyUtil.ELE_OFF)){
                    bundle.putLong(Constant.IntentKey.TABLE_ID, waitDealtEntity.tableId);
                    IntentRouter.go(context,Constant.Router.HS_ELE_OFF_VIEW,bundle);
                }else if (waitDealtEntity.processKey.equals(ProcessKeyUtil.FAULT_INFO)){
                    YHEntity yhEntity = new YHEntity();
                    yhEntity.tableNo = waitDealtEntity.workTableNo;
                    bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY,yhEntity);
                    IntentRouter.go(context, Constant.Router.YH_LOOK, bundle);
                }else if (waitDealtEntity.processKey.equals(ProcessKeyUtil.WORK)){
                    WXGDEntity wxgdEntity = new WXGDEntity();
                    wxgdEntity.tableNo = waitDealtEntity.workTableNo;
                    bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY,wxgdEntity);
                    IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                }else {
                    ToastUtils.show(context, context.getResources().getString(R.string.main_processed_table_no_view));
                }

                /*switch (waitDealtEntity.processKey){
                    case ProcessKeyUtil.WORK_TICKET:
                        goWorkTicket(waitDealtEntity, bundle);
                        break;
                    case ProcessKeyUtil.ELE_ON:
                        bundle.putLong(Constant.IntentKey.TABLE_ID, waitDealtEntity.tableId);
                        IntentRouter.go(context,Constant.Router.HS_ELE_ON_VIEW,bundle);
                        break;
                    case ProcessKeyUtil.ELE_OFF:
                        bundle.putLong(Constant.IntentKey.TABLE_ID, waitDealtEntity.tableId);
                        IntentRouter.go(context,Constant.Router.HS_ELE_OFF_VIEW,bundle);
                        break;
//                case ProcessKeyUtil.RUN_STATE_WF:
//                    break;
//                case ProcessKeyUtil.CHECK_APPLY_FW:
//                    break;
                    case ProcessKeyUtil.FAULT_INFO:
                        YHEntity yhEntity = new YHEntity();
                        yhEntity.tableNo = waitDealtEntity.workTableNo;
                        bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY,yhEntity);
                        IntentRouter.go(context, Constant.Router.YH_LOOK, bundle);
                        break;
                    case ProcessKeyUtil.WORK:
                        WXGDEntity wxgdEntity = new WXGDEntity();
                        wxgdEntity.tableNo = waitDealtEntity.workTableNo;
                        bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY,wxgdEntity);
                        IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                        break;
                    default:
                        ToastUtils.show(context, context.getResources().getString(R.string.main_processed_table_no_view));
                }*/
            }
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
