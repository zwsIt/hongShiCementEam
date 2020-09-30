package com.supcon.mes.module_acceptance.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.ModulePowerController;
import com.supcon.mes.middleware.controller.PcController;
import com.supcon.mes.middleware.controller.WorkFlowKeyController;
import com.supcon.mes.middleware.model.api.EamAPI;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.contract.EamContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.presenter.EamPresenter;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_acceptance.IntentRouter;
import com.supcon.mes.module_acceptance.R;
import com.supcon.mes.module_acceptance.model.api.AcceptanceEditAPI;
import com.supcon.mes.module_acceptance.model.api.AcceptanceListAPI;
import com.supcon.mes.module_acceptance.model.api.AcceptanceSubmitAPI;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceEditEntity;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceEntity;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceListEntity;
import com.supcon.mes.module_acceptance.model.contract.AcceptanceEditContract;
import com.supcon.mes.module_acceptance.model.contract.AcceptanceListContract;
import com.supcon.mes.module_acceptance.model.contract.AcceptanceSubmitContract;
import com.supcon.mes.module_acceptance.presenter.AcceptanceEditPresenter;
import com.supcon.mes.module_acceptance.presenter.AcceptanceListPresenter;
import com.supcon.mes.module_acceptance.presenter.AcceptanceSubmitPresenter;
import com.supcon.mes.module_acceptance.ui.adapter.AcceptanceEditAdapter;
import com.supcon.mes.module_acceptance.ui.util.AcceptanceMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
@Router(value = Constant.Router.ACCEPTANCE_VIEW)
@Presenter(value = {AcceptanceEditPresenter.class, AcceptanceSubmitPresenter.class, AcceptanceListPresenter.class})
@Controller(value = {LinkController.class, PcController.class, WorkFlowKeyController.class})
public class AcceptanceViewActivity extends BaseRefreshRecyclerActivity<AcceptanceEditEntity> implements AcceptanceEditContract.View, AcceptanceSubmitContract.View, AcceptanceListContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("eamCode")
    CustomVerticalTextView eamCode;
    @BindByTag("eamName")
    CustomVerticalTextView eamName;
    @BindByTag("acceptanceDept")
    CustomVerticalTextView acceptanceDept;
    @BindByTag("acceptanceArea")
    CustomVerticalTextView acceptanceArea;
    @BindByTag("acceptanceStaff")
    CustomVerticalTextView acceptanceStaff;
    @BindByTag("acceptanceTime")
    CustomVerticalTextView acceptanceTime;
    @BindByTag("acceptanceItem")
    CustomVerticalEditText acceptanceItem;
    @BindByTag("transition")
    CustomWorkFlowView transition;
    @BindByTag("acceptanceResult")
    CustomSpinner acceptanceResult;

    private AcceptanceEntity acceptanceEntity;
    private AcceptanceEditAdapter acceptanceEditAdapter;
    private String eamCodeStr;
    private String powerCode;
    private LinkController mLinkController;
    private SinglePickController mSinglePickController;
    private List<SystemCodeEntity> mCheckResult;  // 验收结论
    private List<String> mCheckResultStringList = new ArrayList<>();

    private final Map<String, Object> queryParam = new HashMap<>();

    @Override
    protected IListAdapter createAdapter() {
        acceptanceEditAdapter = new AcceptanceEditAdapter(this);
        return acceptanceEditAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_acceptance_edit;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));

        acceptanceEntity = (AcceptanceEntity) getIntent().getSerializableExtra(Constant.IntentKey.ACCEPTANCE_ENTITY);
        if (acceptanceEntity == null){ // 工作提醒
            acceptanceEntity = new AcceptanceEntity();
            acceptanceEntity.tableNo = getIntent().getStringExtra(Constant.IntentKey.TABLENO);
        }

        mSinglePickController = new SinglePickController<String>(this);
        mSinglePickController.textSize(18);
        mSinglePickController.setCanceledOnTouchOutside(true);

        mCheckResult = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.CHECK_RESULT);
        for (SystemCodeEntity systemCodeEntity : mCheckResult){
            mCheckResultStringList.add(systemCodeEntity.value);
        }

        acceptanceEditAdapter.setEdit(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("设备验收");

        eamCode.setEditable(false);
        eamName.setEditable(false);
        acceptanceStaff.setEditable(false);
        acceptanceResult.setEditable(false);
        acceptanceItem.setEditable(false);

        mLinkController = getController(LinkController.class);
//        if (acceptanceEntity.id == null){ // 制定
//            mLinkController.initStartTransition(transition, "checkApplyFW"/*ProcessKeyUtil.CHECK_APPLY_FW*/);
//            ModulePowerController modulePowerController = new ModulePowerController();
//            modulePowerController.checkModulePermission(acceptanceEntity.deploymentId == null ? -1L: acceptanceEntity.deploymentId, new OnSuccessListener<BapResultEntity>() {
//                @Override
//                public void onSuccess(BapResultEntity result1) {
//                    powerCode = result1.powerCode;
//                }
//            });
//        }
    }

    private void updateView(){
        mLinkController.initPendingTransition(transition,acceptanceEntity.pending.id);
        getSubmitPc(acceptanceEntity.pending.activityName);
    }

    /**
     * @param
     * @return 获取单据提交pc
     * @description
     * @author user 2019/10/31
     */
    private void getSubmitPc(String operateCode) {

        getController(WorkFlowKeyController.class).queryWorkFlowKeyToPc(operateCode, Constant.EntityCode.CHECK_APPLY_FW, null, new OnAPIResultListener<Object>() {
            @Override
            public void onFail(String errorMsg) {
                ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
            }

            @Override
            public void onSuccess(Object result) {
                powerCode = String.valueOf(result);
            }
        });
//        getController(PcController.class).queryPc(operateCode, "checkApplyFW"/*ProcessKeyUtil.CHECK_APPLY_FW*/, new OnAPIResultListener<String>() {
//            @Override
//            public void onFail(String errorMsg) {
//                ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                powerCode = result;
//            }
//        });
    }
    private void initLink() {
        mLinkController.setCancelShow(acceptanceEntity.faultID == null || TextUtils.isEmpty(acceptanceEntity.faultID.tableNo));
        mLinkController.initPendingTransition(transition, acceptanceEntity.pending.id);
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
        transition.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                switch(action){
                    case 0:
                        doSubmit(null);
                        break;
                    case 1:
                    case 2:
                        if(checkSubmit()){
                            doSubmit(workFlowVar);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        refreshListController.setOnRefreshListener(() -> {
            if (!TextUtils.isEmpty(acceptanceEntity.tableNo)){
                queryParam.put(Constant.BAPQuery.TABLE_NO, acceptanceEntity.tableNo);
                presenterRouter.create(AcceptanceListAPI.class).getAcceptanceList(queryParam, 1,true);
            }
        });

        eamCode.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                acceptanceEntity.beamID = null;
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
            bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, eamCode.getTag().toString());
            bundle.putBoolean(Constant.IntentKey.IS_SELECT,true);
            IntentRouter.go(context, Constant.Router.EAM_TREE_SELECT, bundle);
        });
        eamName.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                acceptanceEntity.beamID = null;
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
            bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, eamName.getTag().toString());
            bundle.putBoolean(Constant.IntentKey.IS_SELECT,true);
            IntentRouter.go(context, Constant.Router.EAM_TREE_SELECT, bundle);
        });
        acceptanceItem.setTextListener(text -> {
            if (!TextUtils.isEmpty(text) && text.equals(acceptanceEntity.checkItem)) {
                return;
            }
            acceptanceEntity.checkItem = Util.strFormat2(text);
        });
        acceptanceStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                acceptanceEntity.checkStaff = null;
            }
//            IntentRouter.go(AcceptanceEditActivity.this, Constant.Router.STAFF);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
            bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
            IntentRouter.go(AcceptanceViewActivity.this, Constant.Router.CONTACT_SELECT, bundle);
        });
        acceptanceResult.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1){
                acceptanceEntity.checkResult = null;
            }
            if (mCheckResult.size() <= 0) {
                ToastUtils.show(context, "验收列表数据为空,请退出重新加载页面！");
                return;
            }
            mSinglePickController.list(mCheckResultStringList)
                    .listener((index, item) -> {
                        acceptanceResult.setContent(String.valueOf(item));
                        acceptanceEntity.checkResult = mCheckResult.get(index);
                    }).show(acceptanceResult.getContent());
        });
    }

    private boolean checkSubmit() {
        if (acceptanceEntity.checkStaff == null) {
            ToastUtils.show(this, "请选择验收人!");
            return false;
        }
        if (acceptanceEntity.beamID == null || TextUtils.isEmpty(acceptanceEntity.beamID.name)) {
            ToastUtils.show(this, "请选择验收设备!");
            return false;
        }
        if (acceptanceEntity.checkResult == null) {
            ToastUtils.show(this, "请填写验收结论!");
            return false;
        }
        if (acceptanceEditAdapter.getList() == null) {
            ToastUtils.show(this, "当前设备没有验收项!");
            return false;
        }
        return false;
    }

    @Override
    protected void initData() {
        super.initData();
        updateData();

    }

    private void updateData(){
        eamCode.setContent(Util.strFormat2(acceptanceEntity.getBeamID().eamAssetCode));
        eamName.setContent(Util.strFormat2(acceptanceEntity.getBeamID().name));
        acceptanceDept.setContent(Util.strFormat2(acceptanceEntity.getBeamID().getUseDept().name));
        acceptanceArea.setContent(Util.strFormat2(acceptanceEntity.getBeamID().installPlace == null ? "" : acceptanceEntity.getBeamID().installPlace.name));
        if (TextUtils.isEmpty(acceptanceEntity.getCheckStaff().name)) {
            Staff checkStaff = new Staff();
            checkStaff.name = EamApplication.getAccountInfo().staffName;
            checkStaff.code = EamApplication.getAccountInfo().staffCode;
            checkStaff.id = EamApplication.getAccountInfo().staffId;
            acceptanceEntity.checkStaff = checkStaff;

            if (acceptanceEntity.createStaff == null){
                acceptanceEntity.createStaff = acceptanceEntity.checkStaff;
            }
        }
        acceptanceStaff.setContent(Util.strFormat2(acceptanceEntity.getCheckStaff().name));
        acceptanceTime.setContent(DateUtil.dateFormat(acceptanceEntity.applyDate != null ? acceptanceEntity.applyDate : System.currentTimeMillis()));
        if (acceptanceEntity.id != null){

            if (acceptanceEntity.checkResult != null){
                acceptanceResult.setContent(acceptanceEntity.checkResult.value);
            }

            acceptanceItem.setContent(Util.strFormat2(acceptanceEntity.checkItem));
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity != null) {
            if (commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff) {
                CommonSearchStaff searchStaff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;
                acceptanceStaff.setContent(Util.strFormat(searchStaff.name));
                Staff staff = new Staff();
                staff.id = searchStaff.id;
                staff.name = searchStaff.name;
                staff.code = searchStaff.code;
                acceptanceEntity.checkStaff = staff;
            }
            if (commonSearchEvent.commonSearchEntity instanceof EamEntity) {
                EamEntity eamEntity = (EamEntity) commonSearchEvent.commonSearchEntity;
                eamCode.setContent(Util.strFormat(eamEntity.eamAssetCode));
                eamName.setContent(Util.strFormat(eamEntity.name));
                acceptanceDept.setContent(Util.strFormat(eamEntity.getUseDept().name));
                acceptanceArea.setContent(eamEntity.installPlace == null ? "--" : eamEntity.installPlace.name);
                acceptanceEntity.beamID = eamEntity;
                acceptanceEntity.dept = eamEntity.getUseDept();
                acceptanceEntity.area = eamEntity.installPlace;
//                refreshListController.refreshBegin();

                eamCodeStr = eamEntity.code;
                loadEam();
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAcceptanceEditSuccess(CommonBAPListEntity entity) {
        Map<String, AcceptanceEditEntity> acceptanceEditEntities = new LinkedHashMap<>();
        if (entity == null) {
            refreshListController.refreshComplete(null);
            return;
        }
        refreshListController.refreshComplete(entity.result);
//        Flowable.fromIterable((List<AcceptanceEditEntity>) entity)
//                .subscribe(acceptanceEditEntity -> {
//
//                    AcceptanceEditEntity acceptanceEditEntityOld;
//                    if (acceptanceEditEntities.containsKey(acceptanceEditEntity.item)) {
//                        acceptanceEditEntityOld = acceptanceEditEntities.get(acceptanceEditEntity.item);
//
//                    } else {
//                        acceptanceEditEntityOld = acceptanceEditEntity;
//                    }
//                    if (!TextUtils.isEmpty(acceptanceEditEntity.category)) {
//                        acceptanceEditEntityOld.categorys.add(acceptanceEditEntity);
//                    }
//
//                    acceptanceEditEntities.put(acceptanceEditEntity.item, acceptanceEditEntityOld);
//
//                }, throwable -> {
//                }, () -> refreshListController.refreshComplete(new ArrayList<>(acceptanceEditEntities.values())));

    }

    @Override
    public void getAcceptanceEditFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {

        Map map = AcceptanceMapManager.createMap(acceptanceEntity);

        List list1 = AcceptanceMapManager.dataChange(acceptanceEditAdapter.getList());
        map.put("dg1561532342588ModelCode", "BEAM2_1.0.0_checkApply_CheckApplyDetail");
        map.put("dg1561532342588ListJson", list1.toString());
        map.put("dgLists['dg1561532342588']", list1.toString());

        map.put("deploymentId", acceptanceEntity.deploymentId != null ? acceptanceEntity.deploymentId : acceptanceEntity.pending.deploymentId);

        if (workFlowVar != null){
            List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
            WorkFlowEntity workFlowEntity = new WorkFlowEntity();
            workFlowEntity.dec = workFlowVar.dec;
            workFlowEntity.type = workFlowVar.outcomeMapJson.get(0).type;
            workFlowEntity.outcome = workFlowVar.outCome;
            workFlowEntities.add(workFlowEntity);

            map.put("workFlowVar.outcomeMapJson", workFlowEntities.toString());
            map.put("workFlowVar.dec", workFlowEntity.dec);
            map.put("workFlowVar.outcome", workFlowEntity.outcome);
            map.put("operateType", Constant.Transition.SUBMIT);
        }else {
            map.put("operateType", Constant.Transition.SAVE);
        }

        onLoading("正在处理中...");
        presenterRouter.create(AcceptanceSubmitAPI.class).doSubmit(map, powerCode);
    }

    @Override
    public void doSubmitSuccess(BapResultEntity entity) {
        onLoadSuccessAndExit("验收成功", () -> {
            EventBus.getDefault().post(new RefreshEvent());
            finish();
        });
    }

    @Override
    public void doSubmitFailed(String errorMsg) {
        if ("本数据已经被其他人修改或删除，请刷页面后重试".equals(errorMsg)) {
            loaderController.showMsgAndclose(ErrorMsgHelper.msgParse(errorMsg), false, 5000);
        } else {
            onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void back() {
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getAcceptanceListSuccess(AcceptanceListEntity entity) {
        refreshListController.refreshComplete();
        if (entity.result.size() > 0){
            acceptanceEntity = entity.result.get(0);
            updateView();
            updateData();
            loadPt(null,acceptanceEntity.id);
        }else {
            ToastUtils.show(context,"未查询到验收单信息");
        }
    }

    /**
     * 加载pt项
     * @param eamId
     * @param tableId
     */
    private void loadPt(Long eamId, Long tableId) {
        presenterRouter.create(AcceptanceEditAPI.class).getAcceptanceEdit(eamId,tableId);
    }


    private void loadEam(){
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.IntentKey.EAM_CODE, eamCodeStr);
        presenterRouter.create(EamAPI.class).getEam(params, true,1,20);
//        eamCodeStr = null;
    }

    @Override
    public void getAcceptanceListFailed(String errorMsg) {
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }
}
