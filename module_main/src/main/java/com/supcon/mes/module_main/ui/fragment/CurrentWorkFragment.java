package com.supcon.mes.module_main.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.listener.OnTextListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.UserPowerCheckController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.CustomFilterBean;
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
import com.supcon.mes.module_main.ui.util.FilterHelper;

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
@Presenter(value = {WaitDealtPresenter.class, WaitDealtSubmitPresenter.class})
public class CurrentWorkFragment extends BaseRefreshRecyclerFragment<WaitDealtEntity> implements WaitDealtContract.View, WaitDealtSubmitContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("waitStateRadioGroup")
    RadioGroup waitStateRadioGroup;
    @BindByTag("dispatch")
    Button dispatch;

    private WaitDealtAdapter waitDealtAdapter;
    private SinglePickController mSinglePickController;
    private static final String BATCH_DIPATCH_CODE = "workTabsList_batchDispatch_add_BEAM2_1.0.0_workList_workTabsList";
    private boolean hasBatchDiapatchPermission = false;
    private List<RepairGroupEntity> mRepairGroups;
    private List<String> repairGroupList = new ArrayList<>();
    private Map<String, Object> queryParam = new HashMap<>();
    private CommonSearchStaff searchStaff;
    private List<CommonSearchEntity> mSelectStaffList;
    private CustomDialog proxyDialog, dispatchDialog;
    private String reason;
    private CustomTextView dispatchGroup;

    @Override
    protected IListAdapter<WaitDealtEntity> createAdapter() {
        waitDealtAdapter = new WaitDealtAdapter(context);
        return waitDealtAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.main_fragment_current_work;
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
        UserPowerCheckController userPowerCheckController = new UserPowerCheckController();
        userPowerCheckController.checkModulePermission(EamApplication.getAccountInfo().cid, BATCH_DIPATCH_CODE, new OnSuccessListener<Map<String, Boolean>>() {
            @Override
            public void onSuccess(Map<String, Boolean> result) {
                if(result.containsKey(BATCH_DIPATCH_CODE)){
                    Object hasPermission = result.get(BATCH_DIPATCH_CODE);
                    if(hasPermission != null){
                        hasBatchDiapatchPermission = (boolean) hasPermission;
                    }
                }
            }
        });

        initRepairGroup();
    }

    /**
     * @param
     * @return
     * @description 初始化维修组
     * @author zhangwenshuai1 2018/8/22
     */
    private void initRepairGroup() {
        mRepairGroups = EamApplication.dao().getRepairGroupEntityDao().queryBuilder().where(RepairGroupEntityDao.Properties.Ip.eq(EamApplication.getIp())).list();
        for (RepairGroupEntity entity : mRepairGroups) {
            repairGroupList.add(entity.name);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(WaitDealtAPI.class).getWaitDealt(pageIndex, 20, queryParam);
            }
        });

        waitStateRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            waitDealtAdapter.setEditable(false);
            dispatch.setVisibility(View.GONE);
            if (checkedId == R.id.allRBtn){
                queryParam.remove(Constant.BAPQuery.OVERDATEFLAG);
                queryParam.remove(Constant.BAPQuery.STATE);
            }else if (checkedId == R.id.overdueRBtn){
                queryParam.put(Constant.BAPQuery.OVERDATEFLAG, "1");
                queryParam.remove(Constant.BAPQuery.STATE);
            }else if (checkedId == R.id.pendingRBtn){
                queryParam.put(Constant.BAPQuery.OVERDATEFLAG, "0");
                queryParam.remove(Constant.BAPQuery.STATE);
            }else if (checkedId == R.id.waitDispatchRBtn){
                waitDealtAdapter.setEditable(true);
                dispatch.setVisibility(View.VISIBLE);
                queryParam.remove(Constant.BAPQuery.OVERDATEFLAG);
                queryParam.put(Constant.BAPQuery.STATE, Constant.TableStatus_CH.DISPATCH);
            }
            refreshListController.refreshBegin();
        });

        waitDealtAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                WaitDealtEntity waitDealtEntity = (WaitDealtEntity) obj;
                if (childView.getId() == R.id.waitDealtEntrust) {
                    proxyDialog(waitDealtEntity);
                }
            }
        });
        dispatch.setOnClickListener(view -> {
            if (!hasBatchDiapatchPermission) {
                ToastUtils.show(context, "当前用户没有派单权限");
                return;
            }
            List<WaitDealtEntity> list = waitDealtAdapter.getList();
            StringBuffer workPendingIds = new StringBuffer();
            StringBuffer workIds = new StringBuffer();
//            StringBuffer faultIds = new StringBuffer();
//            StringBuffer faultPendingIds = new StringBuffer();
            Flowable.fromIterable(list)
                    .filter(waitDealtEntity -> waitDealtEntity.isCheck)
                    .subscribe(waitDealtEntity -> {
                        // 只有工单派工环节可派单
                        /*if (waitDealtEntity.state.equals("编辑")) {
                            faultPendingIds.append(waitDealtEntity.pendingid).append(",");
                            faultIds.append(waitDealtEntity.dataid).append(",");
                        } else if (waitDealtEntity.state.equals("派工")) { */
                        workPendingIds.append(waitDealtEntity.pendingId).append(",");
                        workIds.append(waitDealtEntity.tableId).append(",");
//                        }
                    }, throwable -> {
                    }, () -> {
                        Map<String, Object> queryMap = new HashMap<>();
                        if (!TextUtils.isEmpty(workPendingIds.toString())/* || !TextUtils.isEmpty(faultPendingIds.toString())*/) {
                            if (!TextUtils.isEmpty(workPendingIds.toString())) {
                                workPendingIds.deleteCharAt(workPendingIds.length() - 1);
                                workIds.deleteCharAt(workIds.length() - 1);
                                queryMap.put("workIds", workIds);
                                queryMap.put("workPendingIds", workPendingIds.toString());
                            }
//                            if (!TextUtils.isEmpty(faultPendingIds.toString())) {
//                                faultPendingIds.deleteCharAt(faultPendingIds.length() - 1);
//                                faultIds.deleteCharAt(faultIds.length() - 1);
//                                queryMap.put("faultIds", faultIds);
//                                queryMap.put("faultPendingIds", faultPendingIds.toString());
//                            }
                            queryMap.put("batchType", "plpg");
                            dispatchDialog(queryMap);
                        } else {
                            ToastUtils.show(context, "请选择待派单据！");
                        }

                    });

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

    /**
     * 派单
     */
    private void dispatchDialog(Map<String, Object> queryMap) {
        dispatchDialog = new CustomDialog(context).layout(R.layout.dispatch_dialog,
                DisplayUtil.getScreenWidth(context) * 2 / 3, WRAP_CONTENT)
                .bindView(R.id.blueBtn, "确定")
                .bindView(R.id.grayBtn, "取消")
                .bindChildListener(R.id.dispatchGroup, new OnChildViewClickListener() {
                    @Override
                    public void onChildViewClick(View childView, int action, Object obj) {
                        if (action == -1) {
                            queryMap.remove("repairGroupBatchId");
                        } else {
                            if (repairGroupList.size() <= 0) {
                                ToastUtils.show(context, "维修组列表为空！");
                                return;
                            }
                            mSinglePickController.list(repairGroupList)
                                    .listener((index, item) -> {
                                        dispatchGroup.setContent(item.toString());
                                        RepairGroupEntity repairGroup = mRepairGroups.get(index);
                                        queryMap.put("repairGroupBatchId", repairGroup.id);
                                    }).show(dispatchGroup.getValue());
                        }
                    }
                })
                .bindChildListener(R.id.dispatchPerson, new OnChildViewClickListener() {
                    @Override
                    public void onChildViewClick(View childView, int action, Object obj) {
                        if (action == -1) {
                            searchStaff = null;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                        bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
//                        IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
                        IntentRouter.go(context, Constant.Router.STAFF, bundle);
                    }
                })
                .bindClickListener(R.id.blueBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v12) {
                        if (searchStaff != null) {
                            queryMap.put("staffBatchId", searchStaff.id);
                        }
                        if (/*!queryMap.containsKey("repairGroupBatchId") && */!queryMap.containsKey("staffBatchId")) {
                            ToastUtils.show(context, "负责人必填!");
                            return;
                        }
                        onLoading("正在派单...");
                        presenterRouter.create(WaitDealtSubmitAPI.class).bulkSubmitCustom(queryMap);
                        dispatchDialog.dismiss();
                    }
                }, false)
                .bindClickListener(R.id.grayBtn, null, true);
        dispatchGroup = dispatchDialog.getDialog().findViewById(R.id.dispatchGroup);
        dispatchDialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);  // 设置圆角后出现的黑背景
        dispatchDialog.show();
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
                } else if (dispatchDialog != null && dispatchDialog.getDialog().isShowing()) {
                    CustomTextView person = dispatchDialog.getDialog().findViewById(R.id.dispatchPerson);
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
            } else if (dispatchDialog != null && dispatchDialog.getDialog().isShowing()) {
                CustomTextView person = dispatchDialog.getDialog().findViewById(R.id.dispatchPerson);
                person.setContent(Util.strFormat(searchStaff.name));
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
