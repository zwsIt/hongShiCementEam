package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.listener.OnTextListener;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DealInfoController;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.controller.PcController;
import com.supcon.mes.middleware.controller.RoleController;
import com.supcon.mes.middleware.controller.TableInfoController;
import com.supcon.mes.middleware.controller.WorkFlowKeyController;
import com.supcon.mes.middleware.controller.YHCloseController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.LubricatingPartEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.RepairGroupEntity;
import com.supcon.mes.middleware.model.bean.RepairGroupEntityDao;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.PositionEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.ProcessKeyUtil;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.constant.WXGDConstant;
import com.supcon.mes.module_wxgd.controller.LubricateOilsController;
import com.supcon.mes.module_wxgd.controller.MaintenanceController;
import com.supcon.mes.module_wxgd.controller.RepairStaffController;
import com.supcon.mes.module_wxgd.controller.SparePartController;
import com.supcon.mes.module_wxgd.controller.WXGDSubmitController;
import com.supcon.mes.module_wxgd.model.api.WXGDDispatcherAPI;
import com.supcon.mes.module_wxgd.model.api.WXGDListAPI;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDTableInfoEntity;
import com.supcon.mes.module_wxgd.model.contract.WXGDDispatcherContract;
import com.supcon.mes.module_wxgd.model.contract.WXGDListContract;
import com.supcon.mes.module_wxgd.model.dto.LubricateOilsEntityDto;
import com.supcon.mes.module_wxgd.model.dto.MaintainDto;
import com.supcon.mes.module_wxgd.model.dto.RepairStaffDto;
import com.supcon.mes.module_wxgd.model.dto.SparePartEntityDto;
import com.supcon.mes.middleware.model.event.ListEvent;
import com.supcon.mes.module_wxgd.model.event.LubricateOilsEvent;
import com.supcon.mes.module_wxgd.model.event.MaintenanceEvent;
import com.supcon.mes.module_wxgd.model.event.RepairStaffEvent;
import com.supcon.mes.module_wxgd.model.event.SparePartEvent;
import com.supcon.mes.module_wxgd.model.event.VersionRefreshEvent;
import com.supcon.mes.module_wxgd.presenter.WXGDDispatcherPresenter;
import com.supcon.mes.module_wxgd.presenter.WXGDListPresenter;
import com.supcon.mes.module_wxgd.util.WXGDFaultInfoPicHelper;
import com.supcon.mes.module_wxgd.util.WXGDMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * WXGDDispatcherActivity
 * created by zhangwenshuai1 2018/8/15
 * 待派单
 */
@Router(value = Constant.Router.WXGD_DISPATCHER)
@Presenter(value = {WXGDDispatcherPresenter.class, WXGDListPresenter.class})
@Controller(value = {PcController.class, SparePartController.class, RepairStaffController.class, MaintenanceController.class,
        LubricateOilsController.class, OnlineCameraController.class, WorkFlowKeyController.class})
public class WXGDDispatcherActivity extends BaseRefreshActivity implements WXGDDispatcherContract.View, WXGDListContract.View, WXGDSubmitController.OnSubmitResultListener {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("bigRepair")
    Button bigRepair;
    @BindByTag("checkRepair")
    Button checkRepair;
    @BindByTag("workSource")
    TextView workSource;

    ImageView eamIc;
    @BindByTag("eamName")
    CustomTextView eamName;
    @BindByTag("eamCode")
    CustomTextView eamCode;
    @BindByTag("eamArea")
    CustomTextView eamArea;
    @BindByTag("eamNameEdit")
    CustomTextView eamNameEdit;
    @BindByTag("eamCodeEdit")
    CustomTextView eamCodeEdit;
    @BindByTag("eamAreaEdit")
    CustomTextView eamAreaEdit;
    @BindByTag("discoverer")
    CustomTextView discoverer;
    @BindByTag("faultInfoType")
    CustomTextView faultInfoType;
    @BindByTag("priority")
    CustomTextView priority;
    @BindByTag("faultInfoDescribe")
    CustomVerticalTextView faultInfoDescribe;
    @BindByTag("faultInfo")
    LinearLayout faultInfo;
    @BindByTag("eamInfoLl")
    LinearLayout eamInfoLl;
    @BindByTag("eamInfoEditLl")
    LinearLayout eamInfoEditLl;
    @BindByTag("repairLl")
    LinearLayout repairLl;

    @BindByTag("workTicketTable")
    CustomTextView workTicketTable;
    @BindByTag("eleOffTable")
    CustomTextView eleOffTable;
    @BindByTag("dispatcherStaff")
    CustomTextView dispatcherStaff;

    @BindByTag("repairGroup")
    CustomTextView repairGroup;
    @BindByTag("chargeStaff")
    CustomTextView chargeStaff;
    @BindByTag("wosource")
    CustomTextView wosource;
    @BindByTag("repairType")
    CustomSpinner repairType;
    @BindByTag("planStartTime")
    CustomDateView planStartTime;
    @BindByTag("planEndTime")
    CustomDateView planEndTime;
    @BindByTag("realEndTime")
    CustomVerticalDateView realEndTime;
    @BindByTag("repairAdvise")
    CustomVerticalEditText repairAdvise;
    @BindByTag("dispatcherLayout")
    LinearLayout dispatcherLayout;

    @BindByTag("commentInput")
    CustomEditText commentInput;
    @BindByTag("transition")
    CustomWorkFlowView transition;

    @BindByTag("workContext")
    CustomVerticalEditText workContext;

    @BindByTag("eleOffRadioGroup")
    RadioGroup eleOffRadioGroup; // 是否生成停电票

    @BindByTag("eleOff")
    CustomTextView eleOff;

    @BindByTag("recyclerView")
    RecyclerView recyclerView;

    @BindByTag("yhGalleryView")
    CustomGalleryView yhGalleryView;

    private LinkController mLinkController;

    private WXGDEntity mWXGDEntity;//传入维修工单实体参数
    private WXGDEntity oldWxgdEntity;//原维修工单

    private DatePickController datePickController;
    private SinglePickController mSinglePickController;
    private List<RepairGroupEntity> mRepairGroups;
    private List<String> repairGroupList = new ArrayList<>();
    private SparePartController mSparePartController;
    private RepairStaffController mRepairStaffController;
    private LubricateOilsController mLubricateOilsController;
    private MaintenanceController maintenanceController;
    private DealInfoController mDealInfoController;

    private WXGDSubmitController mWxgdSubmitController;
//    private RoleController roleController;
    //表体修改前的列表数据
    private String sparePartListStr; // 备件列表
    private String repairStaffListStr; // 维修人员列表
    private String lubricateOilsListStr; // 润滑油列表
    private String maintenanceListStr;

    //dataGrid删除数据id
    private List<Long> dgDeletedIds_sparePart = new ArrayList<>();
    private List<Long> dgDeletedIds_repairStaff = new ArrayList<>();
    private List<Long> dgDeletedIds_lubricateOils = new ArrayList<>();
    private List<Long> dgDeletedIds_maintenance = new ArrayList<>();
    private String tableNo;
    private YHCloseController yhCloseController;
    private boolean isCancel;
    private String __pc__;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_dispatcher;
    }

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        EventBus.getDefault().register(this);

        mWXGDEntity = (WXGDEntity) getIntent().getSerializableExtra(Constant.IntentKey.WXGD_ENTITY);
        tableNo = getIntent().getStringExtra(Constant.IntentKey.TABLENO);

        if (mWXGDEntity.id == null || mWXGDEntity.id == -1) { // 制定
            refreshController.setAutoPullDownRefresh(false);
            refreshController.setPullDownRefreshEnabled(false);
        } else {
            refreshController.setAutoPullDownRefresh(true);
            refreshController.setPullDownRefreshEnabled(true);
        }

        mSparePartController = getController(SparePartController.class);
        mSparePartController.setEditable(true);
        mRepairStaffController = getController(RepairStaffController.class);
        mRepairStaffController.setEditable(true);
        mLubricateOilsController = getController(LubricateOilsController.class);
        mLubricateOilsController.setEditable(true);
        maintenanceController = getController(MaintenanceController.class);
        maintenanceController.setEditable(true);

        mWxgdSubmitController = new WXGDSubmitController(this);  //工作流提交Controller

        mLinkController = new LinkController();

        datePickController = new DatePickController(this);
        datePickController.setCycleDisable(false);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(false);
        datePickController.textSize(18);

        mSinglePickController = new SinglePickController(this);
        mSinglePickController.setDividerVisible(false);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.textSize(18);
        yhCloseController = new YHCloseController();

        //获取维修组
        initRepairGroup();

        registerController(WXGDSubmitController.class.getName(), mWxgdSubmitController);

        mDealInfoController = new  DealInfoController(context,recyclerView,null);
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

    @Override
    protected void initView() {
        super.initView();
        eamIc = findViewById(R.id.eamIc);
        if (mWXGDEntity.id == null || mWXGDEntity.id == -1){
            repairLl.setVisibility(View.GONE);
            workContext.setNecessary(true);
            workContext.setEditable(true);
        }
    }

    /**
     * @param
     * @return
     * @description 初始化工作流
     * @author zhangwenshuai1 2018/9/1
     */
    private void initLink() {
        // 工单来源为巡检或其他、手工添加时显示作废按钮
        if (mWXGDEntity.workSource != null && (Constant.WxgdWorkSource.patrolcheck.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.other.equals(mWXGDEntity.workSource.id)
                || Constant.WxgdWorkSource.manual_start.equals(mWXGDEntity.workSource.id))) {
            mLinkController.setCancelShow(true);
        } else {
            mLinkController.setCancelShow(false);
        }
        if (mWXGDEntity.id == null || mWXGDEntity.id == -1) { // 制定

            getController(WorkFlowKeyController.class).queryWorkFlowKeyOnly(Constant.EntityCode.WORK, null, new OnAPIResultListener<Object>() {
                @Override
                public void onFail(String errorMsg) {
                    ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
                }

                @Override
                public void onSuccess(Object result) {
                    mLinkController.initStartTransition(transition, String.valueOf(result));
                }
            });

            getSubmitPc("start310work"); // 通过pc端菜单管理中相应菜单获取制定 操作编码
        } else {
//            mLinkController.setOnSuccessListener(result -> {
//                //获取__pc__
//                getSubmitPc(result.toString());
//            });
            mLinkController.initPendingTransition(transition, mWXGDEntity.pending.id);
            getSubmitPc(mWXGDEntity.pending.activityName);
        }
    }

    /**
     * @param
     * @return 获取单据提交pc
     * @description
     * @author user 2019/10/31
     */
    private void getSubmitPc(String operateCode) {
        getController(WorkFlowKeyController.class).queryWorkFlowKeyToPc(operateCode,Constant.EntityCode.WORK, null, new OnAPIResultListener<Object>() {
            @Override
            public void onFail(String errorMsg) {
                ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
            }

            @Override
            public void onSuccess(Object result) {
                __pc__ = String.valueOf(result);
            }
        });



//        getController(PcController.class).queryPc(operateCode, ProcessKeyUtil.WORK, new OnAPIResultListener<String>() {
//            @Override
//            public void onFail(String errorMsg) {
//                ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                __pc__ = result;
//            }
//        });
    }

    /**
     * @param
     * @return
     * @description 初始化表头显示
     * @author zhangwenshuai1 2018/8/16
     */
    private void initTableHeadView() {
        if (mWXGDEntity == null) return;
        if (mWXGDEntity.id == null || mWXGDEntity.id == -1 || "BEAM2003/08".equals(mWXGDEntity.workSource.id)){ // 制定
            eamInfoLl.setVisibility(View.GONE);
            eamInfoEditLl.setVisibility(View.VISIBLE);
        }
        if (mWXGDEntity.workSource != null && (Constant.WxgdWorkSource.repair.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.big_repair.equals(mWXGDEntity.workSource.id))){
            repairLl.setVisibility(View.GONE);
        }
        if (mWXGDEntity.faultInfo == null) {
            faultInfo.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(mWXGDEntity.faultInfo.tableNo)) {
                faultInfo.setVisibility(View.GONE);
            } else {
                faultInfo.setVisibility(View.VISIBLE);
            }
        }
        realEndTime.setVisibility(View.GONE);
        dispatcherLayout.setVisibility(View.GONE);
        chargeStaff.setNecessary(true);
        workContext.setNecessary(true);
        workContext.setEditable(true);
    }

    @Override
    protected void initData() {
        super.initData();
        // 制定
        if (mWXGDEntity.id == null || mWXGDEntity.id == -1) {
            WXGDListEntity wxgdListEntity = new WXGDListEntity();
            wxgdListEntity.result = new ArrayList<>();
            wxgdListEntity.result.add(mWXGDEntity);

            listWxgdsSuccess(wxgdListEntity);
        }
    }

    /**
     * @param
     * @return
     * @description 初始化表头数据
     * @author zhangwenshuai1 2018/8/16
     */
    private void initTableHeadData() {
        if (mWXGDEntity == null) return;
        titleText.setText(mWXGDEntity.pending == null ? "" : mWXGDEntity.pending.taskDescription);
        workSource.setText(mWXGDEntity.workSource == null ? "" : mWXGDEntity.workSource.value);
        if (mWXGDEntity.eamID != null && mWXGDEntity.eamID.id != null) {
            eamName.setValue(mWXGDEntity.eamID.name);
            eamCode.setValue(mWXGDEntity.eamID.eamAssetCode);
            eamArea.setValue(mWXGDEntity.eamID.installPlace == null ? "" : mWXGDEntity.eamID.installPlace.name);
            eamNameEdit.setValue(mWXGDEntity.eamID.name);
            eamCodeEdit.setValue(mWXGDEntity.eamID.code);
            eamAreaEdit.setValue(mWXGDEntity.eamID.installPlace == null ? "" : mWXGDEntity.eamID.installPlace.name);

            new EamPicController().initEamPic(eamIc, mWXGDEntity.eamID.id);
        }

        if (mWXGDEntity.faultInfo != null) {
            discoverer.setValue(mWXGDEntity.faultInfo.findStaffID != null ? mWXGDEntity.faultInfo.findStaffID.name : "");
            faultInfoType.setValue(mWXGDEntity.faultInfo.faultInfoType == null ? "" : mWXGDEntity.faultInfo.faultInfoType.value);
            priority.setValue(mWXGDEntity.faultInfo.priority == null ? "" : mWXGDEntity.faultInfo.priority.value);
            faultInfoDescribe.setValue(mWXGDEntity.faultInfo.describe);
        }

        wosource.setContent(mWXGDEntity.workSource != null ? mWXGDEntity.workSource.value : "");
        repairType.setSpinner(mWXGDEntity.repairType != null ? mWXGDEntity.repairType.value : "");
        repairAdvise.setContent(mWXGDEntity.repairAdvise);
        workTicketTable.setContent(mWXGDEntity.ohWorkTicket == null ? "" : mWXGDEntity.ohWorkTicket.tableNo);
        eleOffTable.setContent(mWXGDEntity.offApply == null ? "" : mWXGDEntity.offApply.tableNo);
        dispatcherStaff.setContent(mWXGDEntity.getDispatcher().name);
        chargeStaff.setValue(Util.strFormat2(mWXGDEntity.getChargeStaff().name));
        repairGroup.setValue(mWXGDEntity.repairGroup == null ? "" : mWXGDEntity.repairGroup.name);
        planStartTime.setDate(mWXGDEntity.planStartDate == null ? DateUtil.dateTimeFormat(new Date().getTime()) : DateUtil.dateFormat(mWXGDEntity.planStartDate, "yyyy-MM-dd HH:mm:ss"));
        planEndTime.setDate(mWXGDEntity.planEndDate == null ? "" : DateUtil.dateFormat(mWXGDEntity.planEndDate, "yyyy-MM-dd HH:mm:ss"));

        workContext.setContent(mWXGDEntity.workOrderContext);

        if (mWXGDEntity.isPowerCut != null){
            if (mWXGDEntity.isPowerCut.id.equals(WXGDConstant.EleOff.yes)){
                eleOffRadioGroup.check(R.id.yesRadioButton);
            }else {
                eleOffRadioGroup.check(R.id.noRadioButton);
            }
        }else {
            eleOffRadioGroup.clearCheck();
        }

        if (mWXGDEntity.offApply != null && mWXGDEntity.offApply.id != null) { // 存在停电票不可编辑
            RadioButton radioButton;
            for (int i = 0; i < eleOffRadioGroup.getChildCount(); i++) {
                radioButton = (RadioButton) eleOffRadioGroup.getChildAt(i);
                radioButton.setEnabled(false);
                if (radioButton.isChecked()){
                    radioButton.setButtonDrawable(R.drawable.ic_check_box_true_small_gray);
                }
            }
        } else {
            for (int i = 0; i < eleOffRadioGroup.getChildCount(); i++) {
                eleOffRadioGroup.getChildAt(i).setEnabled(true);
            }
        }
    }

    @SuppressLint("CheckResult")
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
                presenterRouter.create(WXGDListAPI.class).listWxgds(1, queryParam,false);
            }
        });

        leftBtn.setOnClickListener(v -> onBackPressed());

        RxView.clicks(bigRepair).throttleFirst(200,TimeUnit.MILLISECONDS)
                .filter(o -> {
                    if (mWXGDEntity.eamID == null || mWXGDEntity.eamID.id == null){
                        ToastUtils.show(context,"设备信息不允许为空");
                        return false;
                    }
                    return true;
                }).subscribe(o -> new CustomDialog(context)
                        .twoButtonAlertDialog("确定转为大修计划?")
                        .bindView(R.id.redBtn, "确定")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v12) {
                                onLoading("正在处理中...");
                                // 产品
//                        presenterRouter.create(WXGDDispatcherAPI.class).translateRepair(mWXGDEntity.faultInfo.id, Constant.YHWXType.DX_SYSCODE);
                                // 水泥(红狮/海螺)项目
//                        doSave(Constant.YHWXType.DX_SYSCODE);
                                presenterRouter.create(WXGDDispatcherAPI.class).translateRepair(mWXGDEntity.id, Constant.YHWXType.DX_SYSCODE);
                            }
                        }, true)
                        .bindClickListener(R.id.grayBtn, null, true)
                        .show());
        RxView.clicks(checkRepair).throttleFirst(200,TimeUnit.MILLISECONDS)
                .filter(o -> {
                    if (mWXGDEntity.eamID == null || mWXGDEntity.eamID.id == null){
                        ToastUtils.show(context,"设备信息不允许为空");
                        return false;
                    }
                    return true;
                }).subscribe(o -> new CustomDialog(context)
                        .twoButtonAlertDialog("确定转为检修计划?")
                        .bindView(R.id.redBtn, "确定")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v12) {
                                if (mWXGDEntity.eamID == null || mWXGDEntity.eamID.id == null){
                                    ToastUtils.show(context,"设备信息不允许为空");
                                    return;
                                }
                                onLoading("正在处理中...");
                                presenterRouter.create(WXGDDispatcherAPI.class).translateRepair(mWXGDEntity.id, Constant.YHWXType.JX_SYSCODE);
//                        doSave(Constant.YHWXType.JX_SYSCODE);
                            }
                        }, true)
                        .bindClickListener(R.id.grayBtn, null, true)
                        .show());

        eamNameEdit.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    mWXGDEntity.eamID.id = null;
                    eamCodeEdit.setContent(null);
                    eamAreaEdit.setContent(null);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
                    bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                    bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, eamNameEdit.getTag().toString());
                    bundle.putBoolean(Constant.IntentKey.IS_SELECT,true);
                    IntentRouter.go(context, Constant.Router.EAM_TREE_SELECT, bundle);
                }
            }
        });

        repairGroup.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    mWXGDEntity.repairGroup.id = null;
                } else {
                    if (repairGroupList.size() <= 0) {
                        ToastUtils.show(context, "维修组列表为空！");
                        return;
                    }
                    mSinglePickController.list(repairGroupList)
                            .listener((index, item) -> {
                                repairGroup.setValue(item.toString());
                                mWXGDEntity.repairGroup = mRepairGroups.get(index);
                            }).show(repairGroup.getValue());
                }
            }
        });
        chargeStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    mWXGDEntity.getChargeStaff().id = null;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                    bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                    bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG,chargeStaff.getTag().toString());
                    IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
                }
            }
        });

        planStartTime.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mWXGDEntity.planStartDate = null;
            } else {
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    String dateStr;
                    long select;
                    if (datePickController.isSecondVisible()){
                        dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        select = DateUtil.dateFormat(dateStr, Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC);
                    }else {
                        dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                        select = DateUtil.dateFormat(dateStr, Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN);
                    }

                    //计算大小
                    if (mWXGDEntity.planEndDate != null && select > mWXGDEntity.planEndDate) {
                        ToastUtils.show(context, "计划开始时间不能大于计划结束时间", 3000);
                        return;
                    }
                    mWXGDEntity.planStartDate = select;
                    planStartTime.setDate(dateStr);
                }).show(mWXGDEntity.planStartDate == null ? new Date().getTime() : mWXGDEntity.planStartDate);
            }

        });
        planEndTime.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mWXGDEntity.planEndDate = null;
            } else {
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    String dateStr;
                    long select;
                    if (datePickController.isSecondVisible()){
                        dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        select = DateUtil.dateFormat(dateStr, Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC);
                    }else {
                        dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                        select = DateUtil.dateFormat(dateStr, Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN);
                    }
                    //计算大小
                    if (mWXGDEntity.planStartDate != null && select < mWXGDEntity.planStartDate) {
                        ToastUtils.show(context, "计划结束时间不能小于计划开始时间", 3000);
                        return;
                    }
                    mWXGDEntity.planEndDate = select;
                    planEndTime.setDate(dateStr);
                }).show((mWXGDEntity.planEndDate == null) ? new Date().getTime() : mWXGDEntity.planEndDate);
            }

        });
        transition.setOnChildViewClickListener((childView, action, obj) -> {
            WorkFlowVar workFlowVar = (WorkFlowVar) obj;
            isCancel = false;
            switch (action) {
                case 0:
                    doSave("");
                    break;
                case 1:
                    isCancel = true;
                    // 填写关闭原因
                    showCancelDialog(workFlowVar);
//                        doSubmit(workFlowVar);
                    break;
                case 2:
                    doSubmit(workFlowVar);
                    break;
                case 4:
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.IS_MULTI, true);
                    bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                    bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, "selectPeopleInput");
                    IntentRouter.go(context,Constant.Router.STAFF,bundle);
                    break;
                default:
                    break;
            }
        });

        RxTextView.textChanges(repairAdvise.editText())
                .skipInitialValue()
                .debounce(1, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence.toString())) {
                        mWXGDEntity.repairAdvise = null;
                    } else {
                        mWXGDEntity.repairAdvise = charSequence.toString();
                    }
                });
        RxTextView.textChanges(workContext.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    mWXGDEntity.workOrderContext = charSequence.toString();
                });

        eamName.getCustomValue().setOnClickListener(v -> goSBDA());
        eamIc.setOnClickListener(v -> goSBDA());
        eamCode.getCustomValue().setOnClickListener(v -> goSBDA());
        eleOffRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1)return;
            SystemCodeEntity systemCodeEntity = new SystemCodeEntity();
            if (checkedId == R.id.noRadioButton){
                systemCodeEntity.id = WXGDConstant.EleOff.no;
            }else {
                systemCodeEntity.id = WXGDConstant.EleOff.yes;
            }
            mWXGDEntity.isPowerCut = systemCodeEntity;
        });

    }

    String closeReason; // 关闭原因

    private void showCancelDialog(WorkFlowVar workFlowVar) {
        CustomDialog customDialog = new CustomDialog(context);
        customDialog.layout(R.layout.ac_cancel_dialog, DisplayUtil.getScreenWidth(context) * 4 / 5, WRAP_CONTENT);
        customDialog.bindTextChangeListener(R.id.closeReason, new OnTextListener() {
            @Override
            public void onText(String text) {
                closeReason = text.trim();
            }
        })
                .bindClickListener(R.id.okBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(closeReason)) {
                            ToastUtils.show(context, "请输入关闭原因");
                            return;
                        }
                        customDialog.dismiss();
                        doSubmit(workFlowVar);
                    }
                }, false)
                .bindClickListener(R.id.cancelBtn, null, true)
                .show();
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
                            doSave("");
                        }
                    }, true)
                    .show();
        } else {
            EventBus.getDefault().post(new RefreshEvent());
            super.onBackPressed();
        }

    }

    /**
     * @param
     * @return
     * @description 单据保存
     * @author zhangwenshuai1 2018/9/6
     */
    private void doSave(String str) {

        //封装公共参数
        Map<String, Object> map = WXGDMapManager.createMap(mWXGDEntity);
        // 水泥(红狮/海螺)项目
        if (Constant.YHWXType.DX_SYSCODE.equals(str)) {
            map.put("workRecord.repairType.id", Constant.YHWXType.DX_SYSCODE);
        } else if (Constant.YHWXType.JX_SYSCODE.equals(str)) {
            map.put("workRecord.repairType.id", Constant.YHWXType.JX_SYSCODE);
        }else {
            map.put("workRecord.repairType.id", Constant.YHWXType.RC_SYSCODE);
        }
        map.put("workRecord.chargeStaff.id", Util.strFormat2(mWXGDEntity.getChargeStaff().id)); // 负责人

        map.put("operateType", Constant.Transition.SAVE);

        onLoading("工单保存中...");
        generateParamsDtoAndSubmit(map);
    }

    /**
     * @param
     * @param workFlowVar
     * @return
     * @description 单据提交
     * @author zhangwenshuai1 2018/9/6
     */
    private void doSubmit(WorkFlowVar workFlowVar) {

        if (!Constant.Transition.CANCEL_CN.equals(workFlowVar.dec) && checkTableBlank()) {
            return;
        }
        onLoading("工单提交中...");
        //封装公共参数
        Map<String, Object> map = WXGDMapManager.createMap(mWXGDEntity);
        map.put("workRecord.chargeStaff.id", Util.strFormat2(mWXGDEntity.getChargeStaff().id));
        //封装工作流
        map = generateWorkFlow(workFlowVar, map);

        generateParamsDtoAndSubmit(map);
    }

    /**
     * @param
     * @param workFlowVar
     * @return
     * @description 封装工作流参数信息
     * @author zhangwenshuai1 2018/9/6
     */
    private Map<String, Object> generateWorkFlow(WorkFlowVar workFlowVar, Map<String, Object> map) {
        List<WorkFlowEntity> workFlowEntities = generateWorkFlowMigrationLine(workFlowVar);

        WorkFlowEntity workFlowEntity = workFlowEntities.get(0);
//        if (workFlowVar != null){
//            List<WorkFlowEntity> outcomeMapJson = workFlowVar.outcomeMapJson;
//            if (!TextUtils.isEmpty(outcomeMapJson.get(0).assignUser)){
//                outcomeMapJson.get(0).assignUser = ("\"\"".equals(outcomeMapJson.get(0).assignUser)) ? null : outcomeMapJson.get(0).assignUser.replace("\"","");
//            }
//        }
        map.put("workFlowVar.outcomeMapJson", workFlowEntities.toString());
        map.put("workFlowVar.outcome", workFlowEntity.outcome);
        map.put("workFlowVarStatus", "");
        if (Constant.Transition.REJECT_CN.equals(workFlowEntity.dec)) {
            map.put("workFlowVarStatus", Constant.Transition.REJECT);
        }
        if (Constant.Transition.CANCEL_CN.equals(workFlowEntity.dec)) {
            map.put("workFlowVarStatus", Constant.Transition.CANCEL);
        }
        map.put("operateType", Constant.Transition.SUBMIT);

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
        map.put("viewselect", "workEdit");
        map.put("workRecord.receiptInfo", "paidan"); //派单、接单环节必传字段
        map.put("datagridKey", "BEAM2_workList_workRecord_workEdit_datagrids");
        map.put("viewCode", "BEAM2_1.0.0_workList_workEdit");
        map.put("workFlowVar.comment", commentInput.getInput());
        map.put("taskDescription", "BEAM2_1.0.0.work.task338");
        //表体dataGrids
        LinkedList<RepairStaffDto> repairStaffDtos = WXGDMapManager.translateStaffDto(mRepairStaffController.getRepairStaffEntities());
        LinkedList<SparePartEntityDto> sparePartEntityDtos = WXGDMapManager.translateSparePartDto(mSparePartController.getSparePartEntities());
        LinkedList<LubricateOilsEntityDto> lubricateOilsEntityDtos = WXGDMapManager.translateLubricateOilsDto(mLubricateOilsController.getLubricateOilsEntities());
        List<MaintainDto> maintainDtos = WXGDMapManager.translateMaintainDto(maintenanceController.getMaintenanceEntities());
        if (Constant.Transition.SUBMIT.equals(map.get("operateType")) && TextUtils.isEmpty(map.get("workFlowVarStatus").toString())) {
            for (RepairStaffDto repairStaffDto : repairStaffDtos) {
                if (TextUtils.isEmpty(repairStaffDto.repairStaff.id)) {
                    onLoadFailed("维修人员列表禁止存在空人员信息");
                    return;
                }
            }
            for (SparePartEntityDto sparePartEntityDto : sparePartEntityDtos) {
                if (TextUtils.isEmpty(sparePartEntityDto.productID.id)) {
                    onLoadFailed("备件列表禁止存在空备件信息");
                    return;
                }
            }
            for (LubricateOilsEntityDto lubricateOilsEntityDto : lubricateOilsEntityDtos) {
                if (TextUtils.isEmpty(lubricateOilsEntityDto.lubricate.id)) {
                    onLoadFailed("润滑油列表禁止存在空润滑油信息");
                    return;
                }
            }
        }

        map = WXGDMapManager.dgDeleted(map, dgDeletedIds_repairStaff, "dg1530830249182");
        map.put("dg1530830249182ModelCode", "BEAM2_1.0.0_workList_RepairStaff");
        map.put("dgLists['dg1530830249182']", repairStaffDtos);

        map = WXGDMapManager.dgDeleted(map, dgDeletedIds_sparePart, "dg1530827900504");
        map.put("dg1530827900504ModelCode", "BEAM2_1.0.0_workList_SparePart");
        map.put("dgLists['dg1530827900504']", sparePartEntityDtos);

        map = WXGDMapManager.dgDeleted(map, dgDeletedIds_lubricateOils, "dg1531680481787");
        map.put("dg1531680481787ModelCode", "BEAM2_1.0.0_workList_LubricateOil");
        map.put("dgLists['dg1531680481787']", lubricateOilsEntityDtos);

        map = WXGDMapManager.dgDeleted(map, dgDeletedIds_maintenance, "dg1557832434442");
        map.put("dg1557832434442ModelCode", "BEAM2_1.0.0_workList_Maintenance");
        map.put("dg1557832434442ListJson", maintainDtos);
        map.put("dgLists['dg1557832434442']", maintainDtos);

        mWxgdSubmitController.doDispatcherSubmit(map,__pc__);

    }

    /**
     * @param
     * @param workFlowVar
     * @return
     * @description 工作流迁移线封装
     * @author zhangwenshuai1 2018/9/4
     */
    private List<WorkFlowEntity> generateWorkFlowMigrationLine(WorkFlowVar workFlowVar) {

        if (workFlowVar == null) {  //转为大修或检修直接作废单据工作流
            WorkFlowEntity workFlowEntity = new WorkFlowEntity();
            List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
            List<LinkEntity> linkEntities = mLinkController.getLinkEntities();
            for (LinkEntity linkEntity : linkEntities) {
                if (linkEntity.destination.contains("cancel")) {
                    workFlowEntity.dec = linkEntity.description;
                    workFlowEntity.outcome = linkEntity.name;
                    workFlowEntity.type = "normal";
                    break;
                }
            }
            workFlowEntities.add(workFlowEntity);
            return workFlowEntities;

        } else {  //正常流程工作流
            return workFlowVar.outcomeMapJson;
//            workFlowEntity.dec = workFlowVar.dec;
//            workFlowEntity.outcome = workFlowVar.outCome;
        }

//        workFlowEntity.type = "normal";

//        return workFlowEntities;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @param
     * @return
     * @description 接收原始数据
     * @author zhangwenshuai1 2018/10/11
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveList(ListEvent listEvent) {
        if ("lubricateOils".equals(listEvent.getFlag())) {
            lubricateOilsListStr = listEvent.getList().toString();
        } else if ("repairStaff".equals(listEvent.getFlag())) {
            repairStaffListStr = listEvent.getList().toString();
        } else if ("sparePart".equals(listEvent.getFlag())) {
            sparePartListStr = listEvent.getList().toString();
        } else if ("maintenance".equals(listEvent.getFlag())) {
            maintenanceListStr = listEvent.getList().toString();
        }
    }

    /**
     * @param
     * @return
     * @description 更新备件表体数据
     * @author zhangwenshuai1 2018/10/30
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshSparePart(SparePartEvent event) {
        mSparePartController.updateSparePartEntities(event.getList());

        //若列表数据为list.size=0，CustomListWidget.setData(list) 方法会return导致数据没有清空，现widget.clear()
        if (event.getList().size() <= 0) {
//            sparePartListWidget.clear();
            mSparePartController.clear();
        }
        dgDeletedIds_sparePart = event.getDgDeletedIds();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshRepairStaff(RepairStaffEvent event) {
        mRepairStaffController.updateRepairStaffEntiies(event.getList());
        if (event.getList().size() <= 0) {
//            repairStaffListWidget.clear();
            mRepairStaffController.clear();
        }
        dgDeletedIds_repairStaff = event.getDgDeletedIds();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshLubricateOils(LubricateOilsEvent event) {
        mLubricateOilsController.updateLubricateOilsEntities(event.getList());
        if (event.getList().size() <= 0) {
//            lubricateOilsListWidget.clear();
            mLubricateOilsController.clear();
        }
        dgDeletedIds_lubricateOils = event.getDgDeletedIds();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshRepairStaff(MaintenanceEvent event) {
        maintenanceController.updateMaintenanceEntities(event.getList());
        if (event.getList().size() <= 0) {
            maintenanceController.clear();
        }
        dgDeletedIds_maintenance = event.getDgDeletedIds();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStaffInfo(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff) {
            CommonSearchStaff searchStaff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;
            if (commonSearchEvent.flag.equals(chargeStaff.getTag().toString())){
                chargeStaff.setValue(searchStaff.name);
                mWXGDEntity.getChargeStaff().id = searchStaff.id;
            }else if ("selectPeopleInput".equals(commonSearchEvent.flag)){
                transition.addStaff(searchStaff.name,searchStaff.userId);
            }
        }else if (commonSearchEvent.mCommonSearchEntityList != null){ // 多选
            if ("selectPeopleInput".equals(commonSearchEvent.flag)){
                List<CommonSearchEntity> mCommonSearchEntityList = commonSearchEvent.mCommonSearchEntityList;
                CommonSearchStaff staff;
                for (CommonSearchEntity commonSearchEntity : mCommonSearchEntityList){
                    staff = (CommonSearchStaff) commonSearchEntity;
                    transition.addStaff(staff.name, staff.userId);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void versionRefresh(VersionRefreshEvent versionRefreshEvent) {
        refreshController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLubPart(PositionEvent positionEvent) {
        if (positionEvent.getPosition() == -1) return;
        if (positionEvent.getObj() instanceof LubricatingPartEntity) {
            mLubricateOilsController.getLubricateOilsEntities().get(positionEvent.getPosition()).lubricatingPart = ((LubricatingPartEntity) positionEvent.getObj()).getLubPart();
            mLubricateOilsController.getLubricateOilsAdapter().notifyItemChanged(positionEvent.getPosition());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEam(CommonSearchEvent commonSearchEvent){
        if (!(commonSearchEvent.commonSearchEntity instanceof EamEntity)) {
            return;
        }
        EamEntity eamEntity = (EamEntity) commonSearchEvent.commonSearchEntity;
        eamCodeEdit.setContent(eamEntity.eamAssetCode);
        eamNameEdit.setContent(eamEntity.name);
        eamAreaEdit.setContent(eamEntity.installPlace == null ? "" : eamEntity.installPlace.name);

        mWXGDEntity.eamID = eamEntity;

    }

    @Override
    @Deprecated
    public void getWxgdInfoSuccess(WXGDTableInfoEntity entity) {
    }

    @Override
    @Deprecated
    public void getWxgdInfoFailed(String errorMsg) {

    }

    @Override
    public void listWxgdsSuccess(WXGDListEntity entity) {
        List<WXGDEntity> wxgdEntityList = entity.result;
        if (wxgdEntityList.size() > 0) {
            mWXGDEntity = wxgdEntityList.get(0);
            if (mWXGDEntity.planStartDate == null){ mWXGDEntity.planStartDate = new Date().getTime();} // 计划开始时间默认当前系统时间
            initTableHeadView();
            initLink();
            initTableHeadData();
            oldWxgdEntity = GsonUtil.gsonToBean(mWXGDEntity.toString(), WXGDEntity.class);
            mRepairStaffController.setWxgdEntity(mWXGDEntity);
            mSparePartController.setWxgdEntity(mWXGDEntity);
            mLubricateOilsController.setWxgdEntity(mWXGDEntity);
            maintenanceController.setWxgdEntity(mWXGDEntity);

            initFaultInfoPic();
            // 加载处理意见
            if (mWXGDEntity.tableInfoId != null){
                mDealInfoController.listTableDealInfo(WXGDConstant.URL.PRE_URL,mWXGDEntity.tableInfoId);
            }
        } else {
            ToastUtils.show(this, "未查到当前待办");
        }
        refreshController.refreshComplete();
    }

    private void initFaultInfoPic() {
        WXGDFaultInfoPicHelper.initPic(mWXGDEntity.id, yhGalleryView);
    }

    @Override
    public void listWxgdsFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context, errorMsg);
    }

    @Override
    public void translateRepairSuccess(ResultEntity entity) {
        // 工单作废中...
        //封装公共参数
        Map map = WXGDMapManager.createMap(mWXGDEntity);
        //封装工作流参数
        map = generateWorkFlow(null, map);
        generateParamsDtoAndSubmit(map);

    }

    @Override
    public void translateRepairFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void submitSuccess(BapResultEntity bapResultEntity) {
        if (isCancel) {
            if (mWXGDEntity.faultInfo.id != null) {
                yhCloseController.closeWorkAndSaveReason((mWXGDEntity.faultInfo.id), closeReason, result -> onLoadSuccessAndExit("处理成功", () -> {
                    EventBus.getDefault().post(new RefreshEvent());
                    finish();
                }), errorMsg -> onLoadFailed(ErrorMsgHelper.msgParse(errorMsg)));
            }
        } else {
            onLoadSuccessAndExit("处理成功", new OnLoaderFinishListener() {
                @Override
                public void onLoaderFinished() {
                    EventBus.getDefault().post(new RefreshEvent());
                    finish();
                }
            });
        }

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
        ToastUtils.show(context, "登陆成功，请重新操作!");
    }

    /**
     * @param
     * @return
     * @description 列表数据是否改变
     * @author zhangwenshuai1 2018/9/11
     */
    private boolean doCheckChange() {
        if (mWXGDEntity != null && oldWxgdEntity != null && !mWXGDEntity.toString().equals(oldWxgdEntity.toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(sparePartListStr) && !sparePartListStr.equals(mSparePartController.getSparePartEntities().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(repairStaffListStr) && !repairStaffListStr.equals(mRepairStaffController.getRepairStaffEntities().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(lubricateOilsListStr) && !lubricateOilsListStr.equals(mLubricateOilsController.getLubricateOilsEntities().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(maintenanceListStr) && !maintenanceListStr.equals(maintenanceController.getMaintenanceEntities().toString())) {
            return true;
        }
        return false;
    }

    /**
     * @param
     * @return
     * @description 判断维修组和负责人必须填其中之一
     * @author zhangwenshuai1 2018/10/23
     */
    private boolean checkTableBlank() {
        if (mWXGDEntity.id == -1 && TextUtils.isEmpty(eamNameEdit.getValue())) { // 制单
            ToastUtils.show(context, "设备不允许为空！");
            return true;
        }
        if (mWXGDEntity.isPowerCut == null) {
            ToastUtils.show(context, "请选择是否生成停电票！");
            return true;
        }
        if (TextUtils.isEmpty(chargeStaff.getValue())) {
            ToastUtils.show(context, "负责人不允许为空！");
            return true;
        }
        if (TextUtils.isEmpty(planStartTime.getContent())) {
            ToastUtils.show(context, "计划开始时间不允许为空！");
            return true;
        }
        if (TextUtils.isEmpty(planEndTime.getContent())) {
            ToastUtils.show(context, "计划结束时间不允许为空！");
            return true;
        }
        if (TextUtils.isEmpty(mWXGDEntity.workOrderContext)){
            ToastUtils.show(context,"工单内容不允许为空！");
            return true;
        }
//        if (TextUtils.isEmpty(repairGroup.getValue()) && TextUtils.isEmpty(chargeStaff.getValue())) {
//            SnackbarHelper.showError(rootView, "维修组和负责人不允许同时为空！");
//            return true;
//        }
        return false;
    }

}
