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
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
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
import com.supcon.mes.middleware.controller.RoleController;
import com.supcon.mes.middleware.controller.YHCloseController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.RepairGroupEntity;
import com.supcon.mes.middleware.model.bean.RepairGroupEntityDao;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.controller.LubricateOilsController;
import com.supcon.mes.module_wxgd.controller.MaintenanceController;
import com.supcon.mes.module_wxgd.controller.RepairStaffController;
import com.supcon.mes.module_wxgd.controller.SparePartController;
import com.supcon.mes.module_wxgd.controller.WXGDSubmitController;
import com.supcon.mes.module_wxgd.model.api.WXGDListAPI;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDTableInfoEntity;
import com.supcon.mes.module_wxgd.model.contract.WXGDDispatcherContract;
import com.supcon.mes.module_wxgd.model.contract.WXGDListContract;
import com.supcon.mes.module_wxgd.model.dto.LubricateOilsEntityDto;
import com.supcon.mes.module_wxgd.model.dto.MaintainDto;
import com.supcon.mes.module_wxgd.model.dto.RepairStaffDto;
import com.supcon.mes.module_wxgd.model.dto.SparePartEntityDto;
import com.supcon.mes.module_wxgd.model.event.ListEvent;
import com.supcon.mes.module_wxgd.model.event.LubricateOilsEvent;
import com.supcon.mes.module_wxgd.model.event.MaintenanceEvent;
import com.supcon.mes.module_wxgd.model.event.RepairStaffEvent;
import com.supcon.mes.module_wxgd.model.event.SparePartEvent;
import com.supcon.mes.module_wxgd.model.event.VersionRefreshEvent;
import com.supcon.mes.module_wxgd.presenter.WXGDDispatcherPresenter;
import com.supcon.mes.module_wxgd.presenter.WXGDListPresenter;
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

/**
 * WXGDDispatcherActivity
 * created by zhangwenshuai1 2018/8/15
 * 待派单
 */
@Router(value = Constant.Router.WXGD_DISPATCHER)
@Presenter(value = {WXGDDispatcherPresenter.class, WXGDListPresenter.class})
@Controller(value = {SparePartController.class, RepairStaffController.class, MaintenanceController.class, LubricateOilsController.class})
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
    @BindByTag("discoverer")
    CustomVerticalTextView discoverer;
    @BindByTag("faultInfoType")
    CustomVerticalTextView faultInfoType;
    @BindByTag("priority")
    CustomTextView priority;
    @BindByTag("faultInfoDescribe")
    CustomVerticalTextView faultInfoDescribe;
    @BindByTag("faultInfo")
    LinearLayout faultInfo;

    @BindByTag("repairLl")
    LinearLayout repairLl;
    @BindByTag("repairGroup")
    CustomVerticalTextView repairGroup;
    @BindByTag("chargeStaff")
    CustomVerticalTextView chargeStaff;
    @BindByTag("wosource")
    CustomVerticalTextView wosource;
    @BindByTag("repairType")
    CustomVerticalSpinner repairType;
    @BindByTag("planStartTime")
    CustomVerticalDateView planStartTime;
    @BindByTag("planEndTime")
    CustomVerticalDateView planEndTime;
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
    CustomVerticalTextView workContext;

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

    private WXGDSubmitController mWxgdSubmitController;
    private RoleController roleController;
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

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_dispatcher;
    }

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        EventBus.getDefault().register(this);

        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(true);

        mWXGDEntity = (WXGDEntity) getIntent().getSerializableExtra(Constant.IntentKey.WXGD_ENTITY);
        tableNo = getIntent().getStringExtra(Constant.IntentKey.TABLENO);

        mSparePartController = getController(SparePartController.class);
        mSparePartController.setEditable(true);
        mRepairStaffController = getController(RepairStaffController.class);
        mRepairStaffController.setEditable(true);
        mLubricateOilsController = getController(LubricateOilsController.class);
        mLubricateOilsController.setEditable(true);
        maintenanceController = getController(MaintenanceController.class);
        maintenanceController.setEditable(true);


        roleController = new RoleController();  //角色
        roleController.queryRoleList(EamApplication.getUserName());

        mWxgdSubmitController = new WXGDSubmitController(this);  //工作流提交Controller

        mLinkController = new LinkController();

        datePickController = new DatePickController(this);
        datePickController.setCycleDisable(false);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(true);
        datePickController.textSize(18);

        mSinglePickController = new SinglePickController(this);
        mSinglePickController.setDividerVisible(false);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.textSize(18);
        yhCloseController = new YHCloseController();

        //获取维修组
        initRepairGroup();

        registerController(Constant.Controller.ROLE, roleController);
        registerController(WXGDSubmitController.class.getName(), mWxgdSubmitController);
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
        updateInitView();
    }

    public void updateInitView() {
        if (mWXGDEntity != null) {
            titleText.setText(mWXGDEntity.pending == null ? "" : mWXGDEntity.pending.taskDescription);
            initTableHeadView();
            // 初始化工作流
            initLink();
        }
    }

    /**
     * @param
     * @return
     * @description 初始化工作流
     * @author zhangwenshuai1 2018/9/1
     */
    private void initLink() {
        mLinkController.setCancelShow(mWXGDEntity.workSource != null ? mWXGDEntity.faultInfo != null && TextUtils.isEmpty(mWXGDEntity.faultInfo.tableNo) ? true : false : false);
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
//            if (TextUtils.isEmpty(mWXGDEntity.faultInfo.tableNo)) {
//                repairLl.setVisibility(View.GONE);
//                faultInfo.setVisibility(View.GONE);
//            } else {
//                repairLl.setVisibility(View.VISIBLE);
//                faultInfo.setVisibility(View.VISIBLE);
//            }
        }
        realEndTime.setVisibility(View.GONE);
        dispatcherLayout.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        updateInitData();
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

        wosource.setContent(mWXGDEntity.workSource != null ? mWXGDEntity.workSource.value : "");
        repairType.setSpinner(mWXGDEntity.repairType != null ? mWXGDEntity.repairType.value : "");
        repairAdvise.setContent(mWXGDEntity.repairAdvise);
        chargeStaff.setValue(Util.strFormat2(mWXGDEntity.getChargeStaff().name));
        repairGroup.setValue(mWXGDEntity.repairGroup == null ? "" : mWXGDEntity.repairGroup.name);
        planStartTime.setDate(mWXGDEntity.planStartDate == null ? "" : DateUtil.dateFormat(mWXGDEntity.planStartDate, "yyyy-MM-dd HH:mm:ss"));
        planEndTime.setDate(mWXGDEntity.planEndDate == null ? "" : DateUtil.dateFormat(mWXGDEntity.planEndDate, "yyyy-MM-dd HH:mm:ss"));

        workContext.setContent(mWXGDEntity.workOrderContext);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        /*refreshFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put(Constant.BAPQuery.TABLE_NO, mWXGDEntity.tableNo);
                presenterRouter.create(WXGDListAPI.class).listWxgds(1, queryParam);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (scrollView.getY() - 100 > 0){
                    return false;
                }
                return super.checkCanDoRefresh(frame, content, header);
            }
        });*/

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

        bigRepair.setOnClickListener(v -> new CustomDialog(context)
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
                        doSave(Constant.YHWXType.DX_SYSCODE);
                    }
                }, true)
                .bindClickListener(R.id.grayBtn, null, true)
                .show());
        checkRepair.setOnClickListener(v -> new CustomDialog(context)
                .twoButtonAlertDialog("确定转为检修计划?")
                .bindView(R.id.redBtn, "确定")
                .bindView(R.id.grayBtn, "取消")
                .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v12) {
                        onLoading("正在处理中...");
//                        presenterRouter.create(WXGDDispatcherAPI.class).translateRepair(mWXGDEntity.faultInfo.id, Constant.YHWXType.JX_SYSCODE);

                        doSave(Constant.YHWXType.JX_SYSCODE);
                    }
                }, true)
                .bindClickListener(R.id.grayBtn, null, true)
                .show());

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
                    IntentRouter.go(context, Constant.Router.STAFF);
                }
            }
        });

        planStartTime.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mWXGDEntity.planStartDate = null;
            } else {
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    long select = DateUtil.dateFormat(dateStr, "yyyy-MM-dd HH:mm:ss");

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
                    String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    long select = DateUtil.dateFormat(dateStr, "yyyy-MM-dd HH:mm:ss");
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
        transition.setOnChildViewClickListener(new OnChildViewClickListener() {

            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                isCancel = false;
                switch (action) {
                    case 0:
                        doSave("");
                        break;
                    case 1:
                        isCancel = true;
                        doSubmit(workFlowVar);
                        break;
                    case 2:
                        doSubmit(workFlowVar);
                        break;
                    default:
                        break;
                }
            }
        });

        RxTextView.textChanges(repairAdvise.editText())
                .skipInitialValue()
                .debounce(1, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence.toString())) {
                        mWXGDEntity.repairAdvise = "";
                    } else {
                        mWXGDEntity.repairAdvise = charSequence.toString();
                    }
                });
//        transition.setOnChildViewClickListener(new OnChildViewClickListener() {
//            @Override
//            public void onChildViewClick(View childView, int action, Object obj) {
//                String tag = (String) childView.getTag();
//                Transition currentTransition = transition.currentTransition();
//                switch (tag) {
//                    case Constant.Transition.SUBMIT_BTN:
//                        doSubmit(currentTransition);
//                        break;
//                    case Constant.Transition.SAVE:
//                        doSave();
//                        break;
//                }
//            }
//        });

        eamName.getCustomValue().setOnClickListener(v -> goSBDA());
        eamIc.setOnClickListener(v -> goSBDA());
        eamCode.getCustomValue().setOnClickListener(v -> goSBDA());
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
        }

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

        mWxgdSubmitController.doDispatcherSubmit(map);

    }

    /**
     * @param
     * @param workFlowVar
     * @return
     * @description 工作流迁移线封装
     * @author zhangwenshuai1 2018/9/4
     */
    private List<WorkFlowEntity> generateWorkFlowMigrationLine(WorkFlowVar workFlowVar) {
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        if (workFlowVar == null) {  //转为大修或检修直接作废单据工作流
            List<LinkEntity> linkEntities = mLinkController.getLinkEntities();
            for (LinkEntity linkEntity : linkEntities) {
                if (linkEntity.destination.contains("cancel")) {
                    workFlowEntity.dec = linkEntity.description;
                    workFlowEntity.outcome = linkEntity.name;
                    break;
                }
            }
        } else {  //正常流程工作流
            workFlowEntity.dec = workFlowVar.dec;
            workFlowEntity.outcome = workFlowVar.outCome;
        }

        workFlowEntity.type = "normal";
        workFlowEntities.add(workFlowEntity);
        return workFlowEntities;
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
            chargeStaff.setValue(searchStaff.name);
            mWXGDEntity.getChargeStaff().id = searchStaff.id;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void versionRefresh(VersionRefreshEvent versionRefreshEvent) {
        refreshController.refreshBegin();
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
            oldWxgdEntity = GsonUtil.gsonToBean(mWXGDEntity.toString(), WXGDEntity.class);
            updateInitView();
            updateInitData();
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
    }

    @Override
    public void translateRepairSuccess(ResultEntity entity) {
        onLoadSuccessAndExit("处理成功", () -> {
            onLoading("工单作废中...");

            //封装公共参数
            Map map = WXGDMapManager.createMap(mWXGDEntity);
            //封装工作流参数
            map = generateWorkFlow(null, map);
            generateParamsDtoAndSubmit(map);

        });

    }

    @Override
    public void translateRepairFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void submitSuccess(BapResultEntity bapResultEntity) {
        if (isCancel) {
            yhCloseController.closeWorkAndSaveReason(mWXGDEntity.faultInfo.id, "作废", result -> onLoadSuccessAndExit("处理成功", () -> {
                EventBus.getDefault().post(new RefreshEvent());
                finish();
            }), errorMsg -> onLoadFailed(ErrorMsgHelper.msgParse(errorMsg)));
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
        SnackbarHelper.showMessage(rootView, "登陆成功，请重新操作!");
    }

    /**
     * @param
     * @return
     * @description 列表数据是否改变
     * @author zhangwenshuai1 2018/9/11
     */
    private boolean doCheckChange() {
        if (mWXGDEntity != null && !mWXGDEntity.toString().equals(oldWxgdEntity.toString())) {
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
        if (TextUtils.isEmpty(repairGroup.getValue()) && TextUtils.isEmpty(chargeStaff.getValue())) {
            SnackbarHelper.showError(rootView, "维修组和负责人不允许同时为空！");
            return true;
        }
        return false;
    }

}
