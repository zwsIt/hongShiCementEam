package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.controller.RoleController;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntityDao;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.controller.AcceptanceCheckController;
import com.supcon.mes.module_wxgd.controller.LubricateOilsController;
import com.supcon.mes.module_wxgd.controller.MaintenanceController;
import com.supcon.mes.module_wxgd.controller.RepairStaffController;
import com.supcon.mes.module_wxgd.controller.SparePartController;
import com.supcon.mes.module_wxgd.controller.WXGDSubmitController;
import com.supcon.mes.module_wxgd.model.api.GenerateAcceptanceAPI;
import com.supcon.mes.module_wxgd.model.api.WXGDListAPI;
import com.supcon.mes.module_wxgd.model.bean.GenerateAcceptanceEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;
import com.supcon.mes.module_wxgd.model.contract.GenerateAcceptanceContract;
import com.supcon.mes.module_wxgd.model.contract.WXGDListContract;
import com.supcon.mes.module_wxgd.model.dto.AcceptanceCheckEntityDto;
import com.supcon.mes.module_wxgd.model.event.ListEvent;
import com.supcon.mes.module_wxgd.presenter.GenerateAcceptancePresenter;
import com.supcon.mes.module_wxgd.presenter.WXGDListPresenter;
import com.supcon.mes.module_wxgd.util.WXGDMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WXGDDispatcherActivity 验收Activity
 * created by zhangwenshuai1 2018/8/15
 * 待验收
 */

@Router(value = Constant.Router.WXGD_ACCEPTANCE)
@Presenter(value = {WXGDListPresenter.class, GenerateAcceptancePresenter.class})
@Controller(value = {SparePartController.class, RepairStaffController.class, LubricateOilsController.class, MaintenanceController.class, AcceptanceCheckController.class, OnlineCameraController.class})
public class WXGDAcceptanceActivity extends BaseRefreshActivity implements WXGDSubmitController.OnSubmitResultListener, WXGDListContract.View, GenerateAcceptanceContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("workSource")
    TextView workSource;

    ImageView eamIc;
    @BindByTag("eamName")
    CustomTextView eamName;
    @BindByTag("eamCode")
    CustomTextView eamCode;
    @BindByTag("eamArea")
    CustomTextView eamArea;
    @BindByTag("discoverer")
    CustomVerticalTextView discoverer;

    @BindByTag("faultInfoType")
    CustomVerticalTextView faultInfoType;
    @BindByTag("repairType")
    CustomVerticalSpinner repairType;
    @BindByTag("priority")
    CustomTextView priority;
    @BindByTag("faultInfoDescribe")
    CustomVerticalTextView faultInfoDescribe;
    @BindByTag("faultInfo")
    LinearLayout faultInfo;
    @BindByTag("repairAdvise")
    CustomVerticalEditText repairAdvise;

    @BindByTag("content")
    CustomTextView content;

    @BindByTag("repairGroup")
    CustomVerticalTextView repairGroup;
    @BindByTag("chargeStaff")
    CustomVerticalTextView chargeStaff;
    @BindByTag("wosource")
    CustomVerticalTextView wosource;
    @BindByTag("planStartTime")
    CustomVerticalDateView planStartTime;
    @BindByTag("planEndTime")
    CustomVerticalDateView planEndTime;
    @BindByTag("realEndTime")
    CustomVerticalDateView realEndTime;
    @BindByTag("dispatcherStaff")
    CustomTextView dispatcherStaff;

    @BindByTag("acceptChkStaff")
    CustomVerticalTextView acceptChkStaff;
    @BindByTag("acceptChkStaffCode")
    CustomVerticalTextView acceptChkStaffCode;
    @BindByTag("acceptChkTime")
    CustomVerticalDateView acceptChkTime;
    @BindByTag("acceptChkResult")
    CustomVerticalSpinner acceptChkResult;
    @BindByTag("commentInput")
    CustomEditText commentInput;
    @BindByTag("transition")
    CustomWorkFlowView transition;


    @BindByTag("workContext")
    CustomVerticalTextView workContext;

    @BindByTag("acceptApplyBtn")
    Button acceptApplyBtn;

    @BindByTag("yhGalleryView")
    CustomGalleryView yhGalleryView;

    private AcceptanceCheckController mAcceptanceCheckController;
    private RepairStaffController mRepairStaffController;
    private SparePartController mSparePartController;
    private LubricateOilsController mLubricateOilsController;
    private MaintenanceController maintenanceController;

    private List<Long> dgDeletedIds_acceptance = new ArrayList<>();

    private WXGDEntity mWXGDEntity;//传入维修工单实体参数
    private WXGDSubmitController wxgdSubmitController;

    private LinkController mLinkController;

    private SinglePickController mSinglePickController;
    private DatePickController mDatePickController;
    private String currentAcceptChkDateTime = ""; // 当前验收时间
    private List<SystemCodeEntity> checkResultList = new ArrayList<>();
    private List<String> checkResultListStr = new ArrayList<>();
    private RoleController roleController;
    private AcceptanceCheckEntity currentAcceptChkEntity = new AcceptanceCheckEntity();  //当前验收数据
    private String acceptanceCheckEntities;
    private String tableNo;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_accptence;
    }

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        EventBus.getDefault().register(this);
        refreshController.setPullDownRefreshEnabled(true);
        refreshController.setAutoPullDownRefresh(true);
        mWXGDEntity = (WXGDEntity) getIntent().getSerializableExtra(Constant.IntentKey.WXGD_ENTITY);
        tableNo = getIntent().getStringExtra(Constant.IntentKey.TABLENO);

        mSparePartController = getController(SparePartController.class);
        mSparePartController.setEditable(false);
        mRepairStaffController = getController(RepairStaffController.class);
        mRepairStaffController.setEditable(false);
        mLubricateOilsController = getController(LubricateOilsController.class);
        mLubricateOilsController.setEditable(false);
        mAcceptanceCheckController = getController(AcceptanceCheckController.class);
        mAcceptanceCheckController.setEditable(false);
        maintenanceController = getController(MaintenanceController.class);
        maintenanceController.setEditable(false);

        mLinkController = new LinkController();
        roleController = new RoleController();  //角色
        roleController.queryRoleList(EamApplication.getUserName());

        registerController(Constant.Controller.ROLE, roleController);

        mSinglePickController = new SinglePickController(this);
        mSinglePickController.setDividerVisible(true);
        mSinglePickController.setCanceledOnTouchOutside(true);

        mDatePickController = new DatePickController(this);
        mDatePickController.setSecondVisible(true);
        mDatePickController.setDividerVisible(true);
        mDatePickController.setCanceledOnTouchOutside(true);

        initCheckResult();
    }

    private void initCheckResult() {
        checkResultList = EamApplication.dao().getSystemCodeEntityDao().queryBuilder().where(SystemCodeEntityDao.Properties.EntityCode.eq(Constant.SystemCode.CHECK_RESULT)).list();
        for (SystemCodeEntity entity : checkResultList) {
            checkResultListStr.add(entity.value);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        eamIc = findViewById(R.id.eamIc);
        updateInitView();
    }

    public void updateInitView() {
        if (mWXGDEntity != null) {
            titleText.setText(mWXGDEntity.pending == null ? "" : mWXGDEntity.pending.taskDescription);
            initTableHeadView();
            // 初始化工作流
            initLink();

            getController(OnlineCameraController.class).init(Constant.IMAGE_SAVE_YHPATH, Constant.PicType.YH_PIC);
            if (mWXGDEntity.attachmentEntities != null) {
                getController(OnlineCameraController.class).setPicData(mWXGDEntity.attachmentEntities);
            }
        }
    }

    @Subscribe
    public void onReceiveImageDeleteEvent(ImageDeleteEvent imageDeleteEvent) {
        getController(OnlineCameraController.class).deleteGalleryBean(yhGalleryView.getGalleryAdapter().getList().get(imageDeleteEvent.getPos()), imageDeleteEvent.getPos());
//        yhGalleryView.deletePic(imageDeleteEvent.getPos());
        EventBus.getDefault().post(new RefreshEvent());
    }

    /**
     * @param
     * @return
     * @description 初始化工作流
     * @author zhangwenshuai1 2018/9/10
     */
    private void initLink() {
        mLinkController.initPendingTransition(transition, mWXGDEntity.pending.id);
    }

    /**
     * @param
     * @return
     * @description 初始化表头显示
     * @author zhangwenshuai1 2018/8/16
     */
    private void initTableHeadView() {
        if (mWXGDEntity.faultInfo == null) {
            faultInfo.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(mWXGDEntity.faultInfo.tableNo)) {
                faultInfo.setVisibility(View.GONE);
            } else {
                faultInfo.setVisibility(View.VISIBLE);
            }
        }
        repairGroup.setEditable(false);
        chargeStaff.setEditable(false);
        planStartTime.setEditable(false);
        planEndTime.setEditable(false);
        realEndTime.setEditable(false);
        realEndTime.setNecessary(false);
        repairType.setEditable(false);
        repairAdvise.setEditable(false);
    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
        wxgdSubmitController = new WXGDSubmitController(this);
        registerController(WXGDSubmitController.class.getName(), wxgdSubmitController);
    }

    @Override
    protected void initData() {
        super.initData();
        updateInitData();
//        presenterRouter.create(WXGDDispatcherAPI.class).getWxgdInfo(1000, 1000);
    }

    public void updateInitData() {
        if (mWXGDEntity != null) {
            initTableHeadData();
        }
    }

    /**
     * @param
     * @return
     * @description 初始化表头数据
     * @author zhangwenshuai1 2018/8/16
     */
    private void initTableHeadData() {
        workSource.setText(mWXGDEntity.workSource == null ? "" : mWXGDEntity.workSource.value);
        if (mWXGDEntity.eamID != null && mWXGDEntity.eamID.id != null) {
            eamName.setValue(mWXGDEntity.eamID.name);
            eamCode.setValue(mWXGDEntity.eamID.code);
            eamArea.setValue(mWXGDEntity.eamID.installPlace == null ? "" : mWXGDEntity.eamID.installPlace.name);

            new EamPicController().initEamPic(eamIc, mWXGDEntity.eamID.id);
        }
        if (mWXGDEntity.faultInfo != null) {
            discoverer.setValue(mWXGDEntity.faultInfo.findStaffID != null ? mWXGDEntity.faultInfo.findStaffID.name : "");
            faultInfoType.setValue(mWXGDEntity.faultInfo.faultInfoType == null ? "" : mWXGDEntity.faultInfo.faultInfoType.value);
            priority.setValue(mWXGDEntity.faultInfo.priority == null ? "" : mWXGDEntity.faultInfo.priority.value);
            faultInfoDescribe.setValue(mWXGDEntity.faultInfo.describe);
        }
        dispatcherStaff.setContent(!TextUtils.isEmpty(mWXGDEntity.getDispatcher().name) ? mWXGDEntity.getDispatcher().name : EamApplication.getAccountInfo().staffName);
        wosource.setContent(mWXGDEntity.workSource != null ? mWXGDEntity.workSource.value : "");
        repairType.setSpinner(mWXGDEntity.repairType != null ? mWXGDEntity.repairType.value : "");
        repairAdvise.setContent(mWXGDEntity.repairAdvise);
        chargeStaff.setValue(Util.strFormat2(mWXGDEntity.getChargeStaff().name));
        repairGroup.setValue(mWXGDEntity.repairGroup != null ? mWXGDEntity.repairGroup.name : "");
        planStartTime.setDate(mWXGDEntity.planStartDate == null ? "" : DateUtil.dateTimeFormat(mWXGDEntity.planStartDate));
        planEndTime.setDate(mWXGDEntity.planEndDate == null ? "" : DateUtil.dateTimeFormat(mWXGDEntity.planEndDate));
        realEndTime.setDate(mWXGDEntity.realEndDate == null ? "" : DateUtil.dateFormat(mWXGDEntity.realEndDate, "yyyy-MM-dd HH:mm:ss"));

        workContext.setContent(mWXGDEntity.workOrderContext);
    }

    @Override
    protected void initListener() {
        super.initListener();

        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map<String, Object> queryParam = new HashMap<>();
                if (mWXGDEntity == null) {
                    queryParam.put(Constant.BAPQuery.TABLE_NO, tableNo);
                } else {
                    queryParam.put(Constant.BAPQuery.TABLE_NO, mWXGDEntity.tableNo);
                }
                presenterRouter.create(WXGDListAPI.class).listWxgds(1, queryParam);
            }
        });

        leftBtn.setOnClickListener(v -> onBackPressed());


        acceptChkStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    currentAcceptChkEntity.checkStaff = null;
                    acceptChkStaffCode.setValue(null);
                } else {
                    IntentRouter.go(context, Constant.Router.STAFF);
                }
            }
        });

        acceptChkTime.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    currentAcceptChkEntity.checkTime = null;
                    currentAcceptChkDateTime = "";
                } else {
                    mDatePickController.listener((year, month, day, hour, minute, second) -> {
                        currentAcceptChkDateTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        acceptChkTime.setDate(currentAcceptChkDateTime);
                        currentAcceptChkEntity.checkTime = DateUtil.dateFormat(currentAcceptChkDateTime, "yyyy-MM-dd HH:mm:ss");
                    }).show("".equals(currentAcceptChkDateTime) ? new Date().getTime() : DateUtil.dateFormat(currentAcceptChkDateTime, "yyyy-MM-dd HH:mm:ss"));
                }
            }
        });

        acceptChkResult.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    currentAcceptChkEntity.checkResult = null;
                } else {
                    mSinglePickController.list(checkResultListStr)
                            .listener((index, item) -> {
                                acceptChkResult.setSpinner(String.valueOf(item));

                                currentAcceptChkEntity.checkResult = checkResultList.get(checkResultListStr.indexOf(String.valueOf(item)));
                            }).show(acceptChkResult.getSpinnerValue());
                }
            }
        });

        transition.setOnChildViewClickListener((childView, action, obj) -> {
            switch (action) {
                case 0:
                    doSave();
                    break;
                case 1:
                case 2:
                    WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                    doSubmit(workFlowVar);
                    break;
                default:
                    break;
            }
        });

        eamName.getCustomValue().setOnClickListener(v -> goSBDA());
        eamIc.setOnClickListener(v -> goSBDA());
        eamCode.getCustomValue().setOnClickListener(v -> goSBDA());

        acceptApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAcceptChkEntity.checkApplyId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.IntentKey.TABLENO, currentAcceptChkEntity.checkApplyId.tableNo);
                    IntentRouter.go(WXGDAcceptanceActivity.this, Constant.Router.ACCEPTANCE_LIST, bundle);
                    back();
                } else {
                    if (mWXGDEntity.eamID != null) {
                        onLoading("处理中...");
                        presenterRouter.create(GenerateAcceptanceAPI.class).generateCheckApply(mWXGDEntity.id != null ? mWXGDEntity.id : 0
                                , mWXGDEntity.eamID.id != null ? mWXGDEntity.eamID.id : 0
                                , Util.strFormat2(mWXGDEntity.faultInfo != null ? mWXGDEntity.faultInfo.describe : "")
                                , mWXGDEntity.eamID.getInstallPlace().id);
                    } else {
                        ToastUtils.show(context, "无设备可生成验收单！");
                    }
                }
            }
        });
    }

    private void goSBDA() {

        if (mWXGDEntity.eamID == null || mWXGDEntity.eamID.id == null) {
            ToastUtils.show(context, "无设备详情可查看！");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putLong(Constant.IntentKey.SBDA_ONLINE_EAMID, mWXGDEntity.eamID.id);
        bundle.putString(Constant.IntentKey.SBDA_ONLINE_EAMCODE, mWXGDEntity.eamID.code);
        IntentRouter.go(context, Constant.Router.SBDA_ONLINE_VIEW, bundle);
    }

    @Override
    public void onBackPressed() {
        if (doCheckChange()) {
            new CustomDialog(context)
                    .twoButtonAlertDialog("单据数据已经被修改，是否要保存?")
                    .bindView(R.id.grayBtn, "离开")
                    .bindView(R.id.redBtn, "保存")
                    .bindClickListener(R.id.grayBtn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new RefreshEvent());
                            finish();
                        }
                    }, true)
                    .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doSave();
                        }
                    }, true)
                    .show();
        } else {
            EventBus.getDefault().post(new RefreshEvent());
            finish();
        }
    }

    /**
     * @param
     * @return
     * @description 保存单据
     * @author zhangwenshuai1 2018/9/10
     */
    private void doSave() {
        try {
            onLoading("工单保存中...");
            Map map = WXGDMapManager.createMap(mWXGDEntity);
            map.put("operateType", Constant.Transition.SAVE);
            generateParamsDtoAndSubmit(map);
        } catch (Exception e) {
            onLoadFailed("保存失败");
        }
    }

    /**
     * @param
     * @param workFlowVar
     * @return
     * @description 提交单据
     * @author zhangwenshuai1 2018/9/10
     */
    private void doSubmit(WorkFlowVar workFlowVar) {
        if ("".equals(acceptChkResult.getSpinnerValue())) {
            ToastUtils.show(context, "请填写验收结论!");
            return;
        }

        try {
            onLoading("工单提交中...");
            Map<String, Object> map = WXGDMapManager.createMap(mWXGDEntity);
            map.put("workRecord.chargeStaff.id", mWXGDEntity.getChargeStaff().id != null ? mWXGDEntity.getChargeStaff().id : EamApplication.getAccountInfo().staffId);
            //封装工作流
            map = generateWorkFlow(map, workFlowVar);

            generateParamsDtoAndSubmit(map);
        } catch (Exception e) {
            onLoadFailed("提交失败");
        }
    }

    private Map<String, Object> generateWorkFlow(Map<String, Object> map, WorkFlowVar workFlowVar) {
//        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
//        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
//        workFlowEntity.type = "normal";
//        workFlowEntity.outcome = currentTransition.outCome;
//        workFlowEntity.dec = currentTransition.transitionDesc;
//
//        workFlowEntities.add(workFlowEntity);

        map.put("operateType", Constant.Transition.SUBMIT);
        List<WorkFlowEntity> outcomeMapJson = workFlowVar.outcomeMapJson;
        outcomeMapJson.get(0).assignUser = "\"\"".equals(outcomeMapJson.get(0).assignUser) ? null : outcomeMapJson.get(0).assignUser;
        map.put("workFlowVar.outcomeMapJson", workFlowVar.outcomeMapJson.toString());
        map.put("workFlowVar.outcome", workFlowVar.outCome);
        map.put("workFlowVarStatus", "");

        return map;
    }

    /**
     * @param
     * @param map
     * @return
     * @description 封装提交传输的参数信息且提交
     * @author zhangwenshuai1 2018/9/6
     */
    private void generateParamsDtoAndSubmit(Map<String, Object> map) {

        //其他参数
        map.put("viewselect", "workCheckEdit");
        map.put("datagridKey", "BEAM2_workList_workRecord_workCheckEdit_datagrids");
        map.put("viewCode", "BEAM2_1.0.0_workList_workCheckEdit");
        map.put("workFlowVar.comment", commentInput.getInput());

        List<AcceptanceCheckEntityDto> acceptanceCheckEntityDtos = WXGDMapManager.translateAcceptChkDto(getCurrentAcceptChk());
//        map = WXGDMapManager.dgDeleted(map, dgDeletedIds_acceptance, "dg1531695961597");
        map.put("dg1531695961597ModelCode", "BEAM2_1.0.0_workList_AccceptanceCheck");
        map.put("dgLists['dg1531695961597']", acceptanceCheckEntityDtos.toString());

        //表头检验结论
        map.put("workRecord.checkResult.id", currentAcceptChkEntity.checkResult == null ? "" : currentAcceptChkEntity.checkResult.id);
        map.put("workRecord.repairSum", mWXGDEntity.repairSum + 1);

        Map<String, Object> attachmentMap = new HashMap<>();
        getController(OnlineCameraController.class).doSave(attachmentMap);
        if (attachmentMap.size() != 0) {
            attachmentMap.put("linkId", String.valueOf(mWXGDEntity.tableInfoId));
        }

        wxgdSubmitController.doAcceptChkSubmit(map, attachmentMap);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void submitSuccess(BapResultEntity bapResultEntity) {
        onLoadSuccessAndExit("处理成功", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                EventBus.getDefault().post(new RefreshEvent());
                finish();
            }
        });
    }

    @Override
    public void submitFailed(String errorMsg) {
        if ("本数据已经被其他人修改或删除，请刷页面后重试".equals(errorMsg)) {
            loaderController.showMsgAndclose(ErrorMsgHelper.msgParse(errorMsg), false, 5000);
        } else if (errorMsg.contains("com.supcon.orchid.BEAM2.entities.BEAM2SparePart")) {
            //error : Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [com.supcon.orchid.BEAM2.entities.BEAM2SparePart#1211]
            loaderController.showMsgAndclose("请刷新备件表体数据！", false, 4000);
        } else {
            onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reSubmit(LoginEvent loginEvent) {
        SnackbarHelper.showMessage(content, "登陆成功，请重新操作!");
    }

    /**
     * @param
     * @return
     * @description 获取当前的验收
     * @author zhangwenshuai1 2018/9/10
     */
    public List<AcceptanceCheckEntity> getCurrentAcceptChk() {

        List<AcceptanceCheckEntity> currentAcceptChkList = new ArrayList<>();
        currentAcceptChkList.add(currentAcceptChkEntity);

        return currentAcceptChkList;
    }

    /**
     * @param
     * @return
     * @description 接收原始数据
     * @author zhangwenshuai1 2018/10/11
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveList(ListEvent listEvent) {
        if ("acceptanceCheckEntity".equals(listEvent.getFlag())) {
            List list = listEvent.getList();
            if (list.size() > 0 && mWXGDEntity.repairSum != null && list.size() == mWXGDEntity.repairSum) {
                // 已存在保存数据
                currentAcceptChkEntity = (AcceptanceCheckEntity) list.get(list.size() - 1); // 获取最后一个为当前可编辑
            } else {
                // 未存在保存数据
                currentAcceptChkEntity.checkTime = new Date().getTime();

                AccountInfo accountInfo = EamApplication.getAccountInfo();
                Staff staff = new Staff();
                staff.id = accountInfo.staffId;
                staff.name = accountInfo.staffName;
                staff.code = accountInfo.staffCode;
                currentAcceptChkEntity.checkStaff = staff;

            }
            setView();
            acceptanceCheckEntities = currentAcceptChkEntity.toString();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAcceptChkStaff(CommonSearchEvent event) {
        if (event.commonSearchEntity instanceof CommonSearchStaff) {
            CommonSearchStaff searchStaff = (CommonSearchStaff) event.commonSearchEntity;
            acceptChkStaff.setValue(searchStaff.name);
            acceptChkStaffCode.setValue(searchStaff.code);
            currentAcceptChkEntity.checkStaff = new Staff();
            currentAcceptChkEntity.checkStaff.id = searchStaff.id;
            currentAcceptChkEntity.checkStaff.code = searchStaff.code;
            currentAcceptChkEntity.checkStaff.name = searchStaff.name;
        }
    }

    public void setView() {
//        if (currentAcceptChkEntity.checkApplyId != null) {
//            acceptApplyBtn.setVisibility(View.GONE);
//        }
        if (currentAcceptChkEntity.checkStaff == null) {
            AccountInfo accountInfo = EamApplication.getAccountInfo();
            Staff staff = new Staff();
            staff.id = accountInfo.staffId;
            staff.name = accountInfo.staffName;
            staff.code = accountInfo.staffCode;
            currentAcceptChkEntity.checkStaff = staff;
        }
        if (currentAcceptChkEntity.checkTime == null) {
            currentAcceptChkEntity.checkTime = new Date().getTime();
        }

        acceptChkStaff.setValue(currentAcceptChkEntity.getCheckStaff().name);
        acceptChkStaffCode.setValue(currentAcceptChkEntity.getCheckStaff().code);
//        if (currentAcceptChkEntity.getCheckResult().value.equals("不合格")) {
//            currentAcceptChkEntity.checkTime = System.currentTimeMillis();
//        }
        acceptChkTime.setDate(DateUtil.dateFormat(currentAcceptChkEntity.checkTime, "yyyy-MM-dd HH:mm:ss"));
        acceptChkResult.setContent(currentAcceptChkEntity.getCheckResult().value);
        currentAcceptChkDateTime = acceptChkTime.getDate();
    }

    /**
     * @param
     * @return
     * @description 验收数据是否改变
     * @author zhangwenshuai1 2018/9/11
     */
    private boolean doCheckChange() {
        if (!TextUtils.isEmpty(acceptanceCheckEntities) && !acceptanceCheckEntities.equals(currentAcceptChkEntity.toString())) {
            return true;
        }
        return false;
    }

    @SuppressLint("CheckResult")
    @Override
    public void listWxgdsSuccess(WXGDListEntity entity) {
        List<WXGDEntity> wxgdEntityList = entity.result;
        if (wxgdEntityList.size() > 0) {
            mWXGDEntity = wxgdEntityList.get(0);
            updateInitView();
            updateInitData();
            mAcceptanceCheckController.setWxgdEntity(mWXGDEntity);
            mRepairStaffController.setWxgdEntity(mWXGDEntity);
            mSparePartController.setWxgdEntity(mWXGDEntity);
            mLubricateOilsController.setWxgdEntity(mWXGDEntity);
            maintenanceController.setWxgdEntity(mWXGDEntity);
            refreshController.refreshComplete();
        } else {
            ToastUtils.show(this, "未查到当前待办");
        }
    }

    @Override
    public void listWxgdsFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshController.refreshComplete();
    }

    @Override
    public void generateCheckApplySuccess(GenerateAcceptanceEntity entity) {
        onLoadSuccessAndExit("生成验收单成功", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.TABLENO, entity.tableNo);
                IntentRouter.go(WXGDAcceptanceActivity.this, Constant.Router.ACCEPTANCE_LIST, bundle);
                back();
            }
        });
    }

    @Override
    public void generateCheckApplyFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

}
