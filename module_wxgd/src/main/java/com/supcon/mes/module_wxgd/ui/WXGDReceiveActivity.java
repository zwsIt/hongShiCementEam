package com.supcon.mes.module_wxgd.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
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
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
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
import com.supcon.mes.module_wxgd.model.contract.WXGDListContract;
import com.supcon.mes.module_wxgd.presenter.WXGDListPresenter;
import com.supcon.mes.module_wxgd.util.FieldHepler;
import com.supcon.mes.module_wxgd.util.WXGDMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/8/28
 * ------------- Description -------------
 * 维修工单接单
 */
@Router(value = Constant.Router.WXGD_RECEIVE)
@Presenter(value = {WXGDListPresenter.class})
@Controller(value = {SparePartController.class, RepairStaffController.class, MaintenanceController.class, LubricateOilsController.class})
public class WXGDReceiveActivity extends BaseRefreshActivity implements WXGDSubmitController.OnSubmitResultListener, WXGDListContract.View {

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
    @BindByTag("wosource")
    CustomVerticalTextView wosource;
    @BindByTag("repairType")
    CustomVerticalSpinner repairType;
    @BindByTag("priority")
    CustomTextView priority;
    @BindByTag("faultInfoDescribe")
    CustomVerticalTextView faultInfoDescribe;
    @BindByTag("faultInfo")
    LinearLayout faultInfo;
    @BindByTag("realEndTime")
    CustomVerticalDateView realEndTime;
    @BindByTag("repairAdvise")
    CustomVerticalEditText repairAdvise;

    @BindByTag("repairGroup")
    CustomVerticalTextView repairGroup;
    @BindByTag("chargeStaff")
    CustomVerticalTextView chargeStaff;
    @BindByTag("planStartTime")
    CustomVerticalDateView planStartTime;
    @BindByTag("planEndTime")
    CustomVerticalDateView planEndTime;
    @BindByTag("commentInput")
    CustomEditText commentInput;
    @BindByTag("transition")
    CustomWorkFlowView transition;
    @BindByTag("dispatcherStaff")
    CustomTextView dispatcherStaff;

    @BindByTag("workContext")
    CustomVerticalTextView workContext;

    private RepairStaffController mRepairStaffController;
    private SparePartController mSparePartController;
    private LubricateOilsController mLubricateOilsController;
    private MaintenanceController maintenanceController;

    private SinglePickController mSinglePickController;
    private WXGDEntity mWXGDEntity;//传入维修工单实体参数
    private LinkController mLinkController;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private RoleController roleController;
    private String tip;
    private WXGDSubmitController wxgdSubmitController;
    private Map<String, SystemCodeEntity> wxTypes;
    private String tableNo;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_receive;
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
        maintenanceController = getController(MaintenanceController.class);
        maintenanceController.setEditable(false);
        mSinglePickController = new SinglePickController<String>(this);
        mSinglePickController.textSize(18);
        mSinglePickController.setCanceledOnTouchOutside(true);
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
        }
    }

    @Override
    protected void initData() {
        super.initData();
        updateInitData();
    }

    public void updateInitData() {
        if (mWXGDEntity != null) {
            List<SystemCodeEntity> wxTypeEntities = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_WX_TYPE);
            wxTypes = initEntities(wxTypeEntities);
            initTableHeadData();
            mLinkController.initPendingTransition(transition, mWXGDEntity.pending != null ? mWXGDEntity.pending.id : 0);
        }
    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
        mLinkController = new LinkController();
        registerController(Constant.Controller.LINK, mLinkController);
        roleController = new RoleController();
        registerController(Constant.Controller.ROLE, roleController);
        roleController.queryRoleList(EamApplication.getUserName());
        wxgdSubmitController = new WXGDSubmitController(this);
        registerController(WXGDSubmitController.class.getName(), wxgdSubmitController);
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
        repairType.setEditable(false);
        realEndTime.setEditable(false);
        realEndTime.setNecessary(false);
        repairAdvise.setEditable(false);
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
        chargeStaff.setValue(!TextUtils.isEmpty(mWXGDEntity.getChargeStaff().name) ? mWXGDEntity.getChargeStaff().name : EamApplication.getAccountInfo().staffName);
        repairGroup.setValue(mWXGDEntity.repairGroup == null ? "" : mWXGDEntity.repairGroup.name);
        planStartTime.setDate(mWXGDEntity.planStartDate == null ? "" : DateUtil.dateFormat(mWXGDEntity.planStartDate, "yyyy-MM-dd HH:mm:ss"));
        planEndTime.setDate(mWXGDEntity.planEndDate == null ? "" : DateUtil.dateFormat(mWXGDEntity.planEndDate, "yyyy-MM-dd HH:mm:ss"));
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

        RxView.clicks(leftBtn).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(v -> {
                    onBackPressed();
                });

        transition.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                switch (action) {
                    case 0:
                        tip = "保存成功";
                        doSave();
                        break;
                    case 1:
                    case 2:
                        doSubmit(workFlowVar);
                        tip = "提交成功";
                        break;
                    default:
                        break;
                }
            }
        });
        repairType.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mWXGDEntity.repairType = null;
            } else {
                List<String> list = new ArrayList<>(wxTypes.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "维修类型列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            SystemCodeEntity wxType = wxTypes.get(item);
                            mWXGDEntity.repairType = wxType;
                            repairType.setSpinner(item);
                        })
                        .show(repairType.getSpinnerValue());
            }
        });

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
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }

    //保存
    private void doSave() {
        onLoading("工单保存中...");
        Map<String, Object> map = WXGDMapManager.createMap(mWXGDEntity);
        map.put("operateType", Constant.Transition.SAVE);
        doSubmit(map);
    }

    //提交
    private void doSubmit(WorkFlowVar workFlowVar) {
        onLoading("工单提交中...");
//        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
//        workFlowEntity.dec = workFlowVar.dec;
//        workFlowEntity.type = "normal";
//        workFlowEntity.outcome = workFlowVar.outCome;
//        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
//        workFlowEntities.add(workFlowEntity);
        Map<String, Object> map = WXGDMapManager.createMap(mWXGDEntity);
        map.put("workRecord.chargeStaff.id", mWXGDEntity.getChargeStaff().id != null ? mWXGDEntity.getChargeStaff().id : EamApplication.getAccountInfo().staffId);
        map.put("operateType", Constant.Transition.SUBMIT);
        map.put("workFlowVar.outcomeMapJson", workFlowVar.outcomeMapJson.toString());
        map.put("workFlowVar.outcome", workFlowVar.outCome);
        doSubmit(map);
    }

    //请求接口
    private void doSubmit(Map<String, Object> map) {
//        RoleEntity roleEntity = roleController.getRoleEntity(0);
//        map.put("workRecord.createPositionId", /*roleEntity.role != null ? roleEntity.role.id :*/ EamApplication.getAccountInfo().roleIds.split(",")[0]);
        map.put("viewselect", "workReceiptEdit");
        map.put("workRecord.receiptInfo", "jiedan");
        map.put("datagridKey", "BEAM2_workList_workRecord_workReceiptEdit_datagrids");
        map.put("viewCode", "BEAM2_1.0.0_workList_workReceiptEdit");
        map.put("taskDescription", "BEAM2_1.0.0.work.task457");
        map.put("workFlowVar.comment", commentInput.getInput());


        map.put("dg1531695879443ModelCode", "BEAM2_1.0.0_workList_LubricateOil");
        map.put("dg1531695879443ListJson", new LinkedList().toString());

        map.put("dg1531695879084ModelCode", "BEAM2_1.0.0_workList_SparePart");
        map.put("dg1531695879084ListJson", new LinkedList().toString());

        map.put("dg1531695879365ModelCode", "BEAM2_1.0.0_workList_RepairStaff");
        map.put("dg1531695879365ListJson", new LinkedList().toString());

        map.put("dg1557994493235ModelCode", "BEAM2_1.0.0_workList_Maintenance");
        map.put("dg1557994493235ListJson", new LinkedList().toString());
        wxgdSubmitController.doReceiveSubmit(map);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        initData();
    }

    @Override
    public void submitSuccess(BapResultEntity bapResultEntity) {
        onLoadSuccessAndExit(tip, new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                EventBus.getDefault().post(new RefreshEvent());
                back();
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
        SnackbarHelper.showMessage(rootView, "登陆成功，请重新操作!");
    }

    @Override
    public void listWxgdsSuccess(WXGDListEntity entity) {
        List<WXGDEntity> wxgdEntityList = entity.result;
        if (wxgdEntityList.size() > 0) {
            mWXGDEntity = wxgdEntityList.get(0);
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
        refreshController.refreshComplete();
    }

    private <T extends BaseEntity> Map<String, T> initEntities(List<T> entities) {
        Map<String, T> map = new LinkedHashMap<>();
        for (T entity : entities) {
            String name = FieldHepler.getFieldValue(entity.getClass(), entity, "name");
            if (!TextUtils.isEmpty(name)) {
                map.put(name, entity);
                continue;
            }
            String value = FieldHepler.getFieldValue(entity.getClass(), entity, "value");
            if (!TextUtils.isEmpty(value)) {
                map.put(value, entity);
            }

        }
        return map;
    }
}
