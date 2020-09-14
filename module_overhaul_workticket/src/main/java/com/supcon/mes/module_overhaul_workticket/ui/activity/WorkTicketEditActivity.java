package com.supcon.mes.module_overhaul_workticket.ui.activity;

import android.annotation.SuppressLint;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.annotation.MainThread;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.text.format.Time;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.beans.SheetEntity;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DealInfoController;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.PcController;
import com.supcon.mes.middleware.controller.TableInfoController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.ListEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.ui.view.FlowLayout;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.FieldHelper;
import com.supcon.mes.middleware.util.FilterHelper;
import com.supcon.mes.middleware.util.ProcessKeyUtil;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_overhaul_workticket.IntentRouter;
import com.supcon.mes.module_overhaul_workticket.R;
import com.supcon.mes.module_overhaul_workticket.constant.WorkTicketConstant;
import com.supcon.mes.module_overhaul_workticket.controller.SafetyMeasuresController;
import com.supcon.mes.module_overhaul_workticket.controller.WorkTicketCameraController;
import com.supcon.mes.module_overhaul_workticket.model.api.WorkTicketSubmitAPI;
import com.supcon.mes.module_overhaul_workticket.model.bean.HazardPointEntity;
import com.supcon.mes.module_overhaul_workticket.model.bean.WorkTicketEntity;
import com.supcon.mes.module_overhaul_workticket.model.contract.WorkTicketSubmitContract;
import com.supcon.mes.module_overhaul_workticket.model.dto.SafetyMeasuresEntityDto;
import com.supcon.mes.module_overhaul_workticket.presenter.WorkTicketSubmitPresenter;
import com.supcon.mes.module_overhaul_workticket.ui.adapter.HazardPointAdapter;
import com.supcon.mes.module_overhaul_workticket.util.WorkTicketHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/10
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Router(value = Constant.Router.OVERHAUL_WORKTICKET_EDIT)
@Presenter(value = {WorkTicketSubmitPresenter.class})
@Controller(value = {SafetyMeasuresController.class, LinkController.class, PcController.class, TableInfoController.class, WorkTicketCameraController.class})
public class WorkTicketEditActivity extends BaseRefreshActivity implements WorkTicketSubmitContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("rightTv")
    TextView rightTv;
    @BindByTag("workListTableNo")
    CustomTextView workListTableNo;
    @BindByTag("eleOffTableNo")
    CustomTextView eleOffTableNo;
    @BindByTag("chargeStaff")
    CustomTextView chargeStaff;
    @BindByTag("workShop")
    CustomTextView workShop;
    @BindByTag("eamName")
    CustomTextView eamName;
    @BindByTag("eamCode")
    CustomTextView eamCode;
    @BindByTag("riskAssessment")
    CustomTextView riskAssessment;
    @BindByTag("hazardCtrlPoint")
    CustomSpinner hazardCtrlPoint;
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;
    @BindByTag("workFlowView")
    CustomWorkFlowView workFlowView;
    @BindByTag("riskAssessmentRadioGroup")
    RadioGroup riskAssessmentRadioGroup;

    @BindByTag("drawer_layout")
    DrawerLayout drawer_layout;
    @BindByTag("hazardRecyclerView")
    RecyclerView hazardRecyclerView;

    @BindByTag("hazardCtrlPointFlowLy")
    FlowLayout hazardCtrlPointFlowLy;
    @BindByTag("content")
    CustomVerticalEditText content;
    @BindByTag("recyclerView")
    RecyclerView recyclerView;


    private String __pc__;
    private Long tableId; // 单据ID
    private Long pendingId; // 代办Id
    private WorkTicketEntity mWorkTicketEntity;
    private WorkTicketEntity mWorkTicketEntityOld;
    private List<SystemCodeEntity> mRiskAssessmentList;
    private List<SystemCodeEntity> mHazardList;
    private SinglePickController mSinglePickController;
    private String name = ""; // 当前活动名称
    private SafetyMeasuresController safetyMeasuresController;
    private String oldSafetyMeasDetailListStr;
    private DealInfoController mDealInfoController;

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID, -1);
        pendingId = getIntent().getLongExtra(Constant.IntentKey.PENDING_ID, -1);
        // 手工制定无需刷新
        if (!tableId.equals(-1L) && !pendingId.equals(-1L)) {
            refreshController.setAutoPullDownRefresh(true);
            refreshController.setPullDownRefreshEnabled(true);
        } else {
            refreshController.setAutoPullDownRefresh(false);
            refreshController.setPullDownRefreshEnabled(false);
        }

        mSinglePickController = new SinglePickController<String>(this);
        mSinglePickController.textSize(18);
        mSinglePickController.setCanceledOnTouchOutside(true);

        safetyMeasuresController = getController(SafetyMeasuresController.class);
        safetyMeasuresController.setEditable(true);

        mDealInfoController = new  DealInfoController(context,recyclerView,null);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_work_ticket_edit;
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("检修工作票编辑");
        getController(LinkController.class).setCancelShow(true);
        if (pendingId.equals(-1L)) {
            // 制定单据工作流
            getController(LinkController.class).initStartTransition(workFlowView, ProcessKeyUtil.WORK_TICKET);
            getSubmitPc("start_op3wj2a"); // 通过pc端菜单管理中相应菜单获取制定 操作编码
        } else {
            getController(LinkController.class).setOnSuccessListener(result -> {
                //获取__pc__
                name = result.toString();
                getSubmitPc(name);
            });
            getController(LinkController.class).initPendingTransition(workFlowView, pendingId);
        }

        //初始化危险控制源
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//        hazardRecyclerView.setLayoutManager(linearLayoutManager); // 水平线性布局
//        hazardPointAdapter = new HazardPointAdapter(context);
//        hazardRecyclerView.setAdapter(hazardPointAdapter);

    }

    /**
     * @param
     * @return 获取单据提交pc
     * @description
     * @author user 2019/10/31
     */
    private void getSubmitPc(String operateCode) {
        getController(PcController.class).queryPc(operateCode, ProcessKeyUtil.WORK_TICKET, new OnAPIResultListener<String>() {
            @Override
            public void onFail(String errorMsg) {
                ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
            }

            @Override
            public void onSuccess(String result) {
                __pc__ = result;
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mRiskAssessmentList = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.RISK_ASSESSMENT);
        mHazardList = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.HAZARD_CON_POINT);

        // 手工制定时，初始化页面
        if (tableId.equals(-1L) && pendingId.equals(-1L)) {
            mWorkTicketEntity = new WorkTicketEntity();
            mWorkTicketEntity.setDeploymentId(getIntent().getLongExtra(Constant.IntentKey.DEPLOYMENT_ID, -1));
            Staff chargeStaff = new Staff();
            chargeStaff.id = EamApplication.getAccountInfo().staffId;
            chargeStaff.code = EamApplication.getAccountInfo().staffCode;
            chargeStaff.name = EamApplication.getAccountInfo().staffName;
            chargeStaff.getMainPosition().name = EamApplication.getAccountInfo().positionName;
            chargeStaff.mainPosition.getDepartment().name = EamApplication.getAccountInfo().departmentName;
            mWorkTicketEntity.setCreateTime(new Date().getTime());
            mWorkTicketEntity.setCreateStaff(chargeStaff);
            mWorkTicketEntity.setCreateStaffId(chargeStaff.id);
            mWorkTicketEntity.setChargeStaff(chargeStaff);
            mWorkTicketEntity.setWorkShop(chargeStaff.getMainPosition().department);
            mWorkTicketEntity.setCreatePositionId(EamApplication.getAccountInfo().positionId);

            updateTableInfo(mWorkTicketEntity);
        }

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initTableInfoData();
                safetyMeasuresController.listSafetyMeas();
            }
        });

        chargeStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mWorkTicketEntity.getChargeStaff().id = null;
                mWorkTicketEntity.getWorkShop().id = null;
                workShop.setContent(null);
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, chargeStaff.getTag().toString());
                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
            }
        });
        eamName.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    mWorkTicketEntity.getEamId().id = null;
                    eamCode.setContent(null);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
                    bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                    bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, eamCode.getTag().toString());
                    bundle.putBoolean(Constant.IntentKey.IS_SELECT, true);
                    IntentRouter.go(context, Constant.Router.EAM_TREE_SELECT, bundle);
                }
            }
        });

        riskAssessmentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                mWorkTicketEntity.setRiskAssessment(mRiskAssessmentList.get(checkedId % 1000));
            }
        });

        hazardCtrlPointFlowLy.setOnChildViewClickListener(new OnChildViewClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                StringBuilder sbValue = new StringBuilder();
                StringBuilder sbIds = new StringBuilder();
                for (int i = 0; i < hazardCtrlPointFlowLy.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) hazardCtrlPointFlowLy.getChildAt(i);
                    if (checkBox.isChecked()) {
                        sbValue.append(checkBox.getText()).append(",");
                        sbIds.append(mHazardList.get(checkBox.getId() % 1000).id).append(",");
                    }
                }
                mWorkTicketEntity.setHazardsourContrpoint(sbIds.length() > 0 ? sbIds.substring(0, sbIds.length() - 1) : "");
                mWorkTicketEntity.setHazardsourContrpointForDisplay(sbValue.length() > 0 ? sbValue.substring(0, sbValue.length() - 1) : "");
            }
        });

        workFlowView.setOnChildViewClickListener((childView, action, obj) -> {
            if ("selectPeopleInput".equals(childView.getTag())) {//选人
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, "selectPeopleInput");
                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
                return;
            }
            WorkFlowVar workFlowVar = (WorkFlowVar) obj;
            switch (action) {
                case 0:
                    doSave();
                    break;
                case 1: //作废提示
                    if (Constant.Transition.CANCEL_CN.equals(workFlowVar.dec)){
                        showConfirmDialog(workFlowVar);
                    }
                    break;
                case 2:
                    doSubmit(workFlowVar);
                    break;
                default:
                    break;
            }
        });

        RxTextView.textChanges(content.editText())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        mWorkTicketEntity.setContent(charSequence.toString());
                    }
                });
        RxView.clicks(rightTv).throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        retrialEleOff();
                    }
                });

    }

    /**
     * 作废提示
     * @param workFlowVar
     */
    private void showConfirmDialog(WorkFlowVar workFlowVar) {
        new CustomDialog(context).twoButtonAlertDialog(context.getResources().getString(R.string.confirm_exe_operate) + workFlowVar.dec + "？")
                .bindClickListener(R.id.grayBtn,null,true)
                .bindClickListener(R.id.redBtn, v -> doSubmit(workFlowVar), true)
                .show();
    }

    private void retrialEleOff() {
        new CustomDialog(this)
                .twoButtonAlertDialog("确定弃审停电票？")
                .bindClickListener(R.id.grayBtn, null, true)
                .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLoading(context.getResources().getString(R.string.dealing));
                        presenterRouter.create(WorkTicketSubmitAPI.class).retrial(mWorkTicketEntity.getOffApplyTableNo());
                    }
                }, true)
                .show();
    }

    private void doSave() {
        submit(null);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        if (!Constant.Transition.CANCEL_CN.equals(workFlowVar.dec) && checkTableBlank()) {
            return;
        }
        List<WorkFlowEntity> workFlowEntityList = new ArrayList<>();
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        workFlowEntity.dec = workFlowVar.dec;
        workFlowEntity.type = workFlowVar.outcomeMapJson.get(0).type;
        workFlowEntity.outcome = workFlowVar.outCome;
        workFlowEntityList.add(workFlowEntity);

        submit(workFlowEntityList);
    }

    /**
     * 提交参数
     *
     * @param workFlowEntities
     */
    private void submit(List<WorkFlowEntity> workFlowEntities) {
        WorkFlowEntity workFlowEntity;
        if (workFlowEntities != null && workFlowEntities.size() != 0) {
            workFlowEntity = workFlowEntities.get(0);
        } else {
            workFlowEntity = new WorkFlowEntity();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("bap_validate_user_id", String.valueOf(EamApplication.getAccountInfo().userId));
        map.put("ohworkticket.createStaffId", mWorkTicketEntity.getCreateStaffId());
        map.put("ohworkticket.createTime", DateUtil.dateTimeFormat(mWorkTicketEntity.getCreateTime()));
        map.put("ohworkticket.createPositionId", mWorkTicketEntity.getCreatePositionId());
        map.put("viewCode", "WorkTicket_8.20.3.03_workTicket_workTicketEdit");
        map.put("modelName", "Ohworkticket");
        map.put("datagridKey", "WorkTicket_workTicket_ohworkticket_workTicketEdit_datagrids");
        map.put("viewselect", "workTicketEdit");
        map.put("id", tableId.equals(-1L) ? "" : tableId);
        map.put("deploymentId", mWorkTicketEntity.getDeploymentId());
        map.put("ohworkticket.version", mWorkTicketEntity.getVersion());
        if (workFlowEntities != null) {//保存为空
            map.put("workFlowVar.outcomeMapJson", workFlowEntities);
            map.put("workFlowVar.dec", workFlowEntity.dec);
            map.put("workFlowVar.outcome", workFlowEntity.outcome);
            map.put("operateType", "submit");
            if (Constant.Transition.CANCEL_CN.equals(workFlowEntity.dec)) {
                map.put("workFlowVarStatus", Constant.Transition.CANCEL);
            }
        } else {
            map.put("operateType", "save");
        }
        map.put("workFlowVar.comment", Util.strFormat2(workFlowView.getComment()));
//        map.put("taskDescription", "WorkTicket_8.20.3.03.workflow.randon1575618721430.flag");
        map.put("activityName", name);
        if (!pendingId.equals(-1L)) {
            map.put("pendingId", pendingId);
        }
        //表头信息,取修改后最新数据
        map.put("ohworkticket.chargeStaff.id", Util.strFormat2(mWorkTicketEntity.getChargeStaff().id));
        map.put("ohworkticket.workList.id", Util.strFormat2(mWorkTicketEntity.getWorkList().id));
        map.put("ohworkticket.eamId.id", Util.strFormat2(mWorkTicketEntity.getEamId().id));
        map.put("ohworkticket.workShop.id", Util.strFormat2(mWorkTicketEntity.getChargeStaff().getMainPosition().getDepartment().id));
        map.put("ohworkticket.riskAssessment.id", mWorkTicketEntity.getRiskAssessment().id);
        map.put("ohworkticket.content", content.getContent());
        map.put("ohworkticket.hazardsourContrpoint", mWorkTicketEntity.getHazardsourContrpoint());
        map.put("ohworkticket.value", mWorkTicketEntity.getHazardsourContrpointForDisplay());
        map.put("ohworkticket.offApplyId", mWorkTicketEntity.getOffApplyId() == null ? "" : mWorkTicketEntity.getOffApplyId());
        map.put("ohworkticket.offApplyTableno", mWorkTicketEntity.getOffApplyTableNo() == null ? "" : mWorkTicketEntity.getOffApplyTableNo());
        map.put("__file_upload", true);

        // 表单
        List<SafetyMeasuresEntityDto> dgList = WorkTicketHelper.getSafetyMeasuresDto(safetyMeasuresController.getSafetyMeasuresEntityList());
        map.put("dg1575615975095ModelCode", "WorkTicket_8.20.3.03_workTicket_OhwticketPart");
        map.put("dg1575615975095ListJson", dgList.toString());
        map.put("dgLists['dg1575615975095']", dgList.toString());

        // 附件
        Map<String, Object> attachmentMap = new HashMap<>();
//        getController(OnlineCameraController.class).doSave(attachmentMap);
//        if (attachmentMap.size() != 0) {
//            attachmentMap.put("linkId", String.valueOf(mWorkTicketEntity.getTableInfoId()));
//        }

        onLoading("单据处理中...");
        presenterRouter.create(WorkTicketSubmitAPI.class).submit("workTicketEdit", map, attachmentMap, __pc__);
    }

    private boolean checkTableBlank() {
        if (TextUtils.isEmpty(chargeStaff.getValue())) {
            ToastUtils.show(context, "负责人不允许为空！");
            return true;
        }
        if (TextUtils.isEmpty(eamName.getValue())) {
            ToastUtils.show(context, "设备不允许为空！");
            return true;
        }
        if (TextUtils.isEmpty(mWorkTicketEntity.getRiskAssessment().id)) {
            ToastUtils.show(context, "风险评估不允许为空！");
            return true;
        }
//        if (TextUtils.isEmpty(mWorkTicketEntity.getHazardsourContrpoint())) {
//            ToastUtils.show(context, "危险源控制点不允许为空！");
//            return true;
//        }
        if (TextUtils.isEmpty(mWorkTicketEntity.getContent())) {
            ToastUtils.show(context, "内容不允许为空！");
            return true;
        }
        return false;
    }

    protected void initTableInfoData() {
        getController(TableInfoController.class).getTableInfo(WorkTicketConstant.URL.PRE_URL, tableId, WorkTicketConstant.HeaderData.HEADER_DATA_INCLUDES, new OnAPIResultListener() {
            @Override
            public void onFail(String errorMsg) {
                refreshController.refreshComplete();
                ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
            }

            @Override
            public void onSuccess(Object result) {
                WorkTicketEntity entity = GsonUtil.gsonToBean(GsonUtil.gsonString(result), WorkTicketEntity.class);
                updateTableInfo(entity);
                // 加载处理意见
                if (mWorkTicketEntity.getTableInfoId() != null){
                    mDealInfoController.listTableDealInfo(WorkTicketConstant.URL.PRE_URL,mWorkTicketEntity.getTableInfoId());
                }
                refreshController.refreshComplete();
            }
        });
    }

    public void updateTableInfo(WorkTicketEntity entity) {
        this.mWorkTicketEntity = entity;

        // 工单来源不可编辑
        if (!TextUtils.isEmpty(entity.getWorkList().tableNo)) {
            eamName.setEditable(false);
        } else {
            workListTableNo.setVisibility(View.GONE);
        }
        // 停电隐藏
        if (TextUtils.isEmpty(entity.getOffApplyTableNo())) {
            eleOffTableNo.setVisibility(View.GONE);
        } else {
            rightTv.setVisibility(View.VISIBLE);
            rightTv.setText("停电弃审");
        }

        //回填单据表头信息
        workListTableNo.setContent(entity.getWorkList().tableNo);
        eleOffTableNo.setContent(entity.getOffApplyTableNo());
        chargeStaff.setContent(entity.getChargeStaff().name);
        workShop.setContent(entity.getWorkShop().name);
        eamName.setContent(entity.getEamId().name);
        eamCode.setContent(entity.getEamId().eamAssetCode);
        content.setContent(entity.getContent());

        //初始化风险评估
//        if (riskAssessmentRadioGroup.getChildCount() <= 0){
        if (mWorkTicketEntity.getOffApplyId() != null) { // 若关联停电票，风险等级无“低”选项
            for (SystemCodeEntity systemCodeEntity : mRiskAssessmentList) {
                if (systemCodeEntity.value.contains("低")) {
                    mRiskAssessmentList.remove(systemCodeEntity);
                    break;
                }
            }
        }
        riskAssessmentRadioGroup.removeAllViews();
        FilterHelper.addRadioView(this, riskAssessmentRadioGroup,
                FilterHelper.createFilterBySystemCode(mRiskAssessmentList, mWorkTicketEntity.getRiskAssessment().id, false), 50, WRAP_CONTENT);
//        }

        // 初始化危险源控制点
//        if (hazardCtrlPointFlowLy.getChildCount() <= 0){
        hazardCtrlPointFlowLy.removeAllViews();
        FilterHelper.addCheckBoxView(this, hazardCtrlPointFlowLy,
                FilterHelper.createFilterBySystemCode(mHazardList, mWorkTicketEntity.getHazardsourContrpoint(), true), WRAP_CONTENT, WRAP_CONTENT);
//        }

        mWorkTicketEntityOld = GsonUtil.gsonToBean(mWorkTicketEntity.toString(), WorkTicketEntity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
//        if (drawer_layout.isDrawerOpen(Gravity.START)) {
//            drawer_layout.closeDrawers();
//        } else {
        if (isUpdated()) {
            CustomDialog customDialog = new CustomDialog(context);
            customDialog.getDialog().setCanceledOnTouchOutside(true);
            customDialog.twoButtonAlertDialog("单据数据已经被修改，是否要保存?")
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
            finish();
            EventBus.getDefault().post(new RefreshEvent());
        }
//        }
    }

    /**
     * 是否单据发生更新
     *
     * @return
     */
    private boolean isUpdated() {
        if (mWorkTicketEntityOld != null && mWorkTicketEntity != null && !mWorkTicketEntityOld.toString().equals(mWorkTicketEntity.toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(oldSafetyMeasDetailListStr) && !oldSafetyMeasDetailListStr.equals(safetyMeasuresController.getSafetyMeasuresEntityList().toString())) {
            return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveStaff(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff) {
            CommonSearchStaff staff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;

            if (chargeStaff.getTag().toString().equals(commonSearchEvent.flag)) {
                chargeStaff.setContent(staff.name);
                workShop.setContent(staff.department);

                mWorkTicketEntity.getChargeStaff().id = staff.id;
                mWorkTicketEntity.getChargeStaff().name = staff.name;
                mWorkTicketEntity.getChargeStaff().code = staff.code;
            } else if ("selectPeopleInput".equals(commonSearchEvent.flag)) {
                workFlowView.addStaff(staff.name, staff.userId);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEam(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity instanceof EamEntity) {
            EamEntity eam = (EamEntity) commonSearchEvent.commonSearchEntity;
            eamName.setContent(eam.name);
            eamCode.setContent(eam.eamAssetCode);

            mWorkTicketEntity.setEamId(eam);
        }
    }

    /**
     * 接收初始化的检修工作票明细PT
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveList(ListEvent listEvent) {
        if (Constant.EventFlag.WORK_TICKET_PT.equals(listEvent.getFlag())) {
            oldSafetyMeasDetailListStr = listEvent.getList().toString();
        }
    }

    @Override
    public void submitSuccess(BapResultEntity entity) {
        onLoadSuccessAndExit("处理成功！", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                finish();
                EventBus.getDefault().post(new RefreshEvent());
            }
        });
    }

    @Override
    public void submitFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void retrialSuccess(CommonEntity entity) {
        onLoadSuccess(context.getResources().getString(R.string.deal_success));
        refreshController.refreshBegin();
    }

    @Override
    public void retrialFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }
}
