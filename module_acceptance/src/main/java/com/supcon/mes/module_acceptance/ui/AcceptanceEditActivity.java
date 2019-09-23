package com.supcon.mes.module_acceptance.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.ModulePermissonCheckController;
import com.supcon.mes.middleware.controller.ModulePowerController;
import com.supcon.mes.middleware.model.api.EamAPI;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.contract.EamContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.presenter.EamPresenter;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_acceptance.IntentRouter;
import com.supcon.mes.module_acceptance.R;
import com.supcon.mes.module_acceptance.model.api.AcceptanceEditAPI;
import com.supcon.mes.module_acceptance.model.api.AcceptanceSubmitAPI;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceEditEntity;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceEntity;
import com.supcon.mes.module_acceptance.model.contract.AcceptanceEditContract;
import com.supcon.mes.module_acceptance.model.contract.AcceptanceSubmitContract;
import com.supcon.mes.module_acceptance.presenter.AcceptanceEditPresenter;
import com.supcon.mes.module_acceptance.presenter.AcceptanceSubmitPresenter;
import com.supcon.mes.module_acceptance.ui.adapter.AcceptanceEditceAdapter;
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
@Router(value = Constant.Router.ACCEPTANCE_EDIT)
@Presenter(value = {AcceptanceEditPresenter.class, AcceptanceSubmitPresenter.class, EamPresenter.class})
public class AcceptanceEditActivity extends BaseRefreshRecyclerActivity<AcceptanceEditEntity> implements AcceptanceEditContract.View, AcceptanceSubmitContract.View, EamContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;

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
    @BindByTag("titleLayout")
    RelativeLayout titleLayout;
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;
    @BindByTag("commentInput")
    CustomEditText commentInput;
    @BindByTag("transition")
    CustomWorkFlowView transition;
    @BindByTag("workFlowBar")
    LinearLayout workFlowBar;

    private AcceptanceEntity acceptanceEntity;
    private AcceptanceEditceAdapter acceptanceEditceAdapter;
    private String eamCodeStr;
    private NFCHelper nfcHelper;
    private boolean isEdit;
    private Long deploymentId;
    private String powerCode;
    private LinkController mLinkController;
    private EamType eamType;

    @Override
    protected IListAdapter createAdapter() {
        acceptanceEditceAdapter = new AcceptanceEditceAdapter(this);
        return acceptanceEditceAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_acceptance_edit;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        acceptanceEntity = (AcceptanceEntity) getIntent().getSerializableExtra(Constant.IntentKey.ACCEPTANCE_ENTITY);
        eamType = (EamType) getIntent().getSerializableExtra(Constant.IntentKey.EAM);
        isEdit = getIntent().getBooleanExtra(Constant.IntentKey.isEdit, false);

        nfcHelper = NFCHelper.getInstance();
        if (nfcHelper != null) {
            nfcHelper.setup(this);
            nfcHelper.setOnNFCListener(new NFCHelper.OnNFCListener() {
                @Override
                public void onNFCReceived(String nfc) {
                    LogUtil.d("NFC Received : " + nfc);
                    EventBus.getDefault().post(new NFCEvent(nfc));
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));

        titleText.setText("设备验收");
        rightBtn.setImageResource(R.drawable.sl_top_submit);
        rightBtn.setVisibility(View.VISIBLE);

        eamCode.setEnabled(isEdit);
        eamName.setEnabled(isEdit);

//        initLink();
    }

    private void initLink() {
        mLinkController.setCancelShow(acceptanceEntity.faultID != null && TextUtils.isEmpty(acceptanceEntity.faultID.tableNo));
        mLinkController.initPendingTransition(transition, acceptanceEntity.pending.id);
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> new CustomDialog(context)
                        .twoButtonAlertDialog("确定提交验收吗?")
                        .bindView(R.id.redBtn, "确定")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, v12 -> {
                            if (acceptanceEntity.checkStaff == null) {
                                ToastUtils.show(this, "请选择验收人!");
                                return;
                            }
                            if (acceptanceEntity.beamID == null && TextUtils.isEmpty(acceptanceEntity.beamID.name)) {
                                ToastUtils.show(this, "请选择验收设备!");
                                return;
                            }
                            if (acceptanceEditceAdapter.getList() == null) {
                                ToastUtils.show(this, "当前设备没有验收项!");
                                return;
                            }
                            List<LinkEntity> linkEntities = mLinkController.getLinkEntities();
                            if (linkEntities.size() > 0) {
                                doSubmit(createWorkFlowVar(linkEntities.get(0)));
                            } else {
                                ToastUtils.show(this, "当前用户并未拥有创建单据权限！");
                            }
                        }, true)
                        .bindClickListener(R.id.grayBtn, null, true)
                        .show());
//        transition.setOnChildViewClickListener(new OnChildViewClickListener() {
//            @Override
//            public void onChildViewClick(View childView, int action, Object obj) {
//                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
//                switch(action){
//                    case 0:
//                        doSave("");
//                        break;
//                    case 1:
//                        doSubmit(workFlowVar);
//                        break;
//                    case 2:
//                        doSubmit(workFlowVar);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
        refreshListController.setOnRefreshListener(() -> {
            if (TextUtils.isEmpty(eamCodeStr)) {
                presenterRouter.create(AcceptanceEditAPI.class).getAcceptanceEdit(acceptanceEntity.getBeamID().id);
            } else {
                Map<String, Object> params = new HashMap<>();
                params.put(Constant.IntentKey.EAM_CODE, eamCodeStr);
                presenterRouter.create(EamAPI.class).getEam(params, 1);
                eamCodeStr = null;
            }
        });

        eamCode.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                acceptanceEntity.beamID = null;
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
            IntentRouter.go(AcceptanceEditActivity.this, Constant.Router.EAM, bundle);
        });
        eamName.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                acceptanceEntity.beamID = null;
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
            IntentRouter.go(AcceptanceEditActivity.this, Constant.Router.EAM, bundle);
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
            IntentRouter.go(AcceptanceEditActivity.this, Constant.Router.STAFF);
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if (acceptanceEntity == null) {
            acceptanceEntity = new AcceptanceEntity();
            if (eamType != null) {
                acceptanceEntity.beamID = eamType;
                acceptanceEntity.dept = eamType.getUseDept();
                acceptanceEntity.area = eamType.getInstallPlace();
            } else {
                acceptanceEntity.getBeamID().id = -1;
            }
        }
        eamCode.setContent(Util.strFormat2(acceptanceEntity.getBeamID().code));
        eamName.setContent(Util.strFormat2(acceptanceEntity.getBeamID().name));
        acceptanceDept.setContent(Util.strFormat2(acceptanceEntity.getDept().name));
        acceptanceArea.setContent(Util.strFormat2(acceptanceEntity.getArea().name));

        if (TextUtils.isEmpty(acceptanceEntity.getCheckStaff().name)) {
            Staff checkStaff = new Staff();
            checkStaff.name = EamApplication.getAccountInfo().staffName;
            checkStaff.code = EamApplication.getAccountInfo().staffCode;
            checkStaff.id = EamApplication.getAccountInfo().staffId;
            acceptanceEntity.checkStaff = checkStaff;
        }
        acceptanceStaff.setContent(Util.strFormat2(acceptanceEntity.getCheckStaff().name));
        acceptanceTime.setContent(DateUtil.dateFormat(acceptanceEntity.applyDate != null ? acceptanceEntity.applyDate : System.currentTimeMillis()));
        acceptanceItem.setContent(Util.strFormat2(acceptanceEntity.checkItem));

        ModulePermissonCheckController mModulePermissonCheckController = new ModulePermissonCheckController();
        mModulePermissonCheckController.checkModulePermission(EamApplication.getUserName(), "checkApplyFW",
                result -> {
                    deploymentId = result;
                    ModulePowerController modulePowerController = new ModulePowerController();
                    modulePowerController.checkModulePermission(deploymentId, result1 -> powerCode = result1.powerCode);
                }, null);

        mLinkController = new LinkController();
        mLinkController.initStartTransition(null, "checkApplyFW");
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
            if (commonSearchEvent.commonSearchEntity instanceof EamType) {
                EamType eamType = (EamType) commonSearchEvent.commonSearchEntity;
                eamCode.setContent(Util.strFormat(eamType.code));
                eamName.setContent(Util.strFormat(eamType.name));
                acceptanceDept.setContent(Util.strFormat(eamType.getUseDept().name));
                acceptanceArea.setContent(Util.strFormat(eamType.getInstallPlace().name));
                acceptanceEntity.beamID = eamType;
                acceptanceEntity.dept = eamType.getUseDept();
                acceptanceEntity.area = eamType.getInstallPlace();
                refreshListController.refreshBegin();
            }
        }
    }

    /**
     * @param
     * @description NFC事件
     * @author zhangwenshuai1
     * @date 2018/6/28
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCEvent nfcEvent) {
        if (isEdit) {
            LogUtil.d("NFC_TAG", nfcEvent.getNfc());
            Map<String, Object> nfcJson = Util.gsonToMaps(nfcEvent.getNfc());
            if (nfcJson.get("textRecord") == null) {
                ToastUtils.show(context, "标签内容空！");
                return;
            }
            eamCodeStr = (String) nfcJson.get("textRecord");
            refreshListController.refreshBegin();
        }
    }

    @Override
    public void getEamSuccess(CommonListEntity entity) {
        if (entity.result.size() > 0) {
            EamType eamType = (EamType) entity.result.get(0);
            eamCode.setContent(Util.strFormat(eamType.code));
            eamName.setContent(Util.strFormat(eamType.name));
            acceptanceDept.setContent(Util.strFormat(eamType.getUseDept().name));
            acceptanceArea.setContent(Util.strFormat(eamType.getInstallPlace().name));
            acceptanceEntity.beamID = eamType;
            acceptanceEntity.dept = eamType.getUseDept();
            acceptanceEntity.area = eamType.getInstallPlace();
            presenterRouter.create(AcceptanceEditAPI.class).getAcceptanceEdit(acceptanceEntity.getBeamID().id);
            return;
        }
        SnackbarHelper.showError(rootView, "未查询到设备：" + eamCodeStr);
    }

    @Override
    public void getEamFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAcceptanceEditSuccess(List entity) {
        Map<String, AcceptanceEditEntity> acceptanceEditEntities = new LinkedHashMap<>();
        if (entity == null) {
            refreshListController.refreshComplete(null);
            return;
        }
        Flowable.fromIterable((List<AcceptanceEditEntity>) entity)
                .subscribe(acceptanceEditEntity -> {

                    AcceptanceEditEntity acceptanceEditEntityOld;
                    if (acceptanceEditEntities.containsKey(acceptanceEditEntity.item)) {
                        acceptanceEditEntityOld = acceptanceEditEntities.get(acceptanceEditEntity.item);

                    } else {
                        acceptanceEditEntityOld = acceptanceEditEntity;
                    }
                    if (!TextUtils.isEmpty(acceptanceEditEntity.category)) {
                        acceptanceEditEntityOld.categorys.add(acceptanceEditEntity);
                    }

                    acceptanceEditEntities.put(acceptanceEditEntity.item, acceptanceEditEntityOld);

                }, throwable -> {
                }, () -> refreshListController.refreshComplete(new ArrayList<>(acceptanceEditEntities.values())));

    }

    @Override
    public void getAcceptanceEditFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    private void doSave() {

    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        workFlowEntity.dec = workFlowVar.dec;
        workFlowEntity.type = workFlowVar.outcomeMapJson.get(0).type;
        workFlowEntity.outcome = workFlowVar.outCome;
        workFlowEntities.add(workFlowEntity);


        Map map = AcceptanceMapManager.createMap(acceptanceEntity);

        List list1 = AcceptanceMapManager.dataChange(acceptanceEditceAdapter.getList());
        map.put("dg1561532342588ModelCode", "BEAM2_1.0.0_checkApply_CheckApplyDetail");
        map.put("dg1561532342588ListJson", list1.toString());
        map.put("dgLists['dg1561532342588']", list1.toString());

        map.put("operateType", Constant.Transition.SUBMIT);
        map.put("deploymentId", deploymentId != null ? deploymentId : "1228");

        map.put("workFlowVar.outcomeMapJson", workFlowEntities.toString());
        map.put("workFlowVar.dec", workFlowEntity.dec);
        map.put("workFlowVar.outcome", workFlowEntity.outcome);
        map.put("operateType", Constant.Transition.SUBMIT);

        if (TextUtils.isEmpty(powerCode)) {
            ToastUtils.show(this, "当前用户并未拥有创建单据权限！");
            return;
        }
        onLoading("正在处理中...");
        presenterRouter.create(AcceptanceSubmitAPI.class).doSubmit(map, powerCode);
    }


    private WorkFlowVar createWorkFlowVar(LinkEntity linkEntity) {
        WorkFlowVar workFlowVar = new WorkFlowVar();
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        workFlowEntity.dec = linkEntity.description;
        workFlowEntity.outcome = linkEntity.name;
        workFlowEntity.type = "normal";
        workFlowEntities.add(workFlowEntity);
        workFlowVar.operateType = "submit";
        workFlowVar.dec = linkEntity.description;
        workFlowVar.outCome = linkEntity.name;
        workFlowVar.outcomeMapJson = workFlowEntities;
        return workFlowVar;
    }

    /**
     * @param
     * @return
     * @description 工作流迁移线封装
     * @author zhangwenshuai1 2018/9/4
     */
    private List<WorkFlowEntity> generateWorkFlowMigrationLine() {
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        workFlowEntity.dec = "生效";
        workFlowEntity.outcome = "SequenceFlow_0qa646v";
        workFlowEntity.type = "normal";
        workFlowEntities.add(workFlowEntity);
        return workFlowEntities;
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取到Tag对象
        if (nfcHelper != null)
            nfcHelper.dealNFCTag(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcHelper != null)
            nfcHelper.onPauseNFC(this);
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
        if (nfcHelper != null) {
            nfcHelper.release();
        }
    }

}
