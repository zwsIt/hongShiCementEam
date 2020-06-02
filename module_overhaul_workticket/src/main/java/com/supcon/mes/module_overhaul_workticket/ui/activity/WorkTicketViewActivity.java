package com.supcon.mes.module_overhaul_workticket.ui.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AttachmentController;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.controller.PcController;
import com.supcon.mes.middleware.controller.TableInfoController;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.ui.view.FlowLayout;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_overhaul_workticket.IntentRouter;
import com.supcon.mes.module_overhaul_workticket.R;
import com.supcon.mes.module_overhaul_workticket.constant.WorkTicketConstant;
import com.supcon.mes.module_overhaul_workticket.controller.SafetyMeasuresController;
import com.supcon.mes.module_overhaul_workticket.controller.WorkTicketCameraController;
import com.supcon.mes.module_overhaul_workticket.model.api.WorkTicketSubmitAPI;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/26
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Router(value = Constant.Router.OVERHAUL_WORKTICKET_VIEW)
@Presenter(value = {WorkTicketSubmitPresenter.class})
@Controller(value = {SafetyMeasuresController.class, LinkController.class, PcController.class, TableInfoController.class
        , WorkTicketCameraController.class, OnlineCameraController.class, AttachmentController.class})
public class WorkTicketViewActivity extends BaseRefreshActivity implements WorkTicketSubmitContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
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
    @BindByTag("riskAssessmentLl")
    LinearLayout riskAssessmentLl;
    @BindByTag("riskAssessmentView")
    CustomTextView riskAssessmentView;
    @BindByTag("hazardCtrlPoint")
    CustomSpinner hazardCtrlPoint;
    @BindByTag("hazardCtrlPointView")
    CustomTextView hazardCtrlPointView;
    @BindByTag("hazardCtrlPointFlowLy")
    FlowLayout hazardCtrlPointFlowLy;
    @BindByTag("workFlowView")
    CustomWorkFlowView workFlowView;
    @BindByTag("riskAssessmentRadioGroup")
    RadioGroup riskAssessmentRadioGroup;
    @BindByTag("centralControlRoom")
    CustomTextView centralControlRoom;
    @BindByTag("securityStaff")
    CustomTextView securityStaff;
    @BindByTag("securityChiefStaff")
    CustomTextView securityChiefStaff;
    @BindByTag("controlDirectorStaff")
    CustomTextView controlDirectorStaff;

    @BindByTag("saferGalleryView")
    CustomGalleryView saferGalleryView;
    @BindByTag("content")
    CustomVerticalEditText content;


    private String __pc__;
    private Long tableId; // 单据ID
    private Long pendingId; // 代办Id
    private WorkTicketEntity mWorkTicketEntity;
    private WorkTicketEntity mWorkTicketEntityOld;
    private String hazardContrlPointValue;
    private List<SystemCodeEntity> mRiskAssessmentList;
    private List<SystemCodeEntity> mHazardList;
    private SinglePickController mSinglePickController;
    private String activityName = ""; // 当前活动名称
    private String tableStatus;
    private ImageView customCameraIv;
    private boolean editable;

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID, -1);
        pendingId = getIntent().getLongExtra(Constant.IntentKey.PENDING_ID, -1);
        tableStatus = getIntent().getStringExtra(Constant.IntentKey.TABLE_STATUS);
        activityName = getIntent().getStringExtra(Constant.IntentKey.ACTIVITY_NAME);
        editable = getIntent().getBooleanExtra(Constant.IntentKey.IS_EDITABLE, false);

        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(true);

        mSinglePickController = new SinglePickController<String>(this);
        mSinglePickController.textSize(18);
        mSinglePickController.setCanceledOnTouchOutside(true);

        getController(SafetyMeasuresController.class).setEditable(false);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_work_ticket_view;
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("检修工作票");

        chargeStaff.setEditable(false);
        eamName.setEditable(false);
        hazardCtrlPoint.setEditable(false);
        riskAssessmentLl.setVisibility(View.GONE);
        riskAssessmentView.setVisibility(View.VISIBLE);
        hazardCtrlPointFlowLy.setVisibility(View.GONE);
        content.setEditable(false);
        // 安全员拍照
        saferGalleryView.setVisibility(View.VISIBLE);
        if (editable) {
            saferGalleryView.setIconVisibility(true);
            saferGalleryView.setEditable(true);
            saferGalleryView.setNecessary(true);
        }

        if (pendingId != -1) {
            if (tableStatus.contains(Constant.TableStatus_CH.NOTIFY)) { // 通知特殊处理
                getController(LinkController.class).setNotify(true, activityName);
            }
            getController(LinkController.class).setCancelShow(true);
            getController(LinkController.class).initPendingTransition(workFlowView, pendingId);
            getSubmitPc(activityName);

        } else {
            workFlowView.setVisibility(View.GONE);
        }

        customCameraIv = saferGalleryView.findViewById(R.id.customCameraIv);
        getController(OnlineCameraController.class).init(Constant.IMAGE_SAVE_WORKTICKETPATH, Constant.PicType.WORK_TICKET_SAFER_PIC);
        getController(OnlineCameraController.class).addGalleryView(0, saferGalleryView);

    }

    /**
     * @param
     * @return 获取单据提交pc
     * @description
     * @author user 2019/10/31
     */
    private void getSubmitPc(String operateCode) {
        getController(PcController.class).queryPc(operateCode, "workTicketFW", new OnAPIResultListener<String>() {
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
                getController(SafetyMeasuresController.class).listSafetyMeas();
            }
        });

        chargeStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mWorkTicketEntity.getChargeStaff().id = null;
                mWorkTicketEntity.getWorkShop().id = null;
                workShop.setContent(null);
            } else {
                IntentRouter.go(context, Constant.Router.STAFF);
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
                    bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, eamName.getTag().toString());
                    bundle.putBoolean(Constant.IntentKey.IS_SELECT,true);
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

        workFlowView.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
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
                    case 1:
                    case 2:
                        doSubmit(workFlowVar);
                        break;
                    default:
                        break;
                }
            }
        });

        customCameraIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController(OnlineCameraController.class).showCustomDialog();
            }
        });

    }

    private void doSave() {
        submit(null);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        if ( !Constant.Transition.REJECT_CN.equals(workFlowVar.dec) && editable && checkTableBlank()) {
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
        map.put("ohworkticket.createPositionId", EamApplication.getAccountInfo().positionId);
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
            if (tableStatus.contains(Constant.TableStatus_CH.NOTIFY)) { // 通知特殊处理
                map.put("pendingActivityType", Constant.Transition.NOTIFICATION);
            }
            if (Constant.Transition.CANCEL_CN.equals(workFlowEntity.dec)) {
                map.put("workFlowVarStatus", Constant.Transition.CANCEL);
            }
        } else {
            map.put("operateType", "save");
        }
        map.put("workFlowVar.comment", Util.strFormat2(workFlowView.getComment()));
//        map.put("taskDescription", "WorkTicket_8.20.3.03.workflow.randon1575618721430.flag");
        map.put("activityName", activityName);
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
        map.put("__file_upload", true);

        // 表单
        List<SafetyMeasuresEntityDto> dgList = WorkTicketHelper.getSafetyMeasuresDto(getController(SafetyMeasuresController.class).getSafetyMeasuresEntityList());
        map.put("dg1575615975095ModelCode", "WorkTicket_8.20.3.03_workTicket_OhwticketPart");
        map.put("dg1575615975095ListJson", dgList.toString());
        map.put("dgLists['dg1575615975095']", dgList.toString());

        // 附件
        Map<String, Object> attachmentMap = new HashMap<>();
        getController(OnlineCameraController.class).doSave(attachmentMap);
        if (attachmentMap.size() != 0) {
            attachmentMap.put("linkId", String.valueOf(mWorkTicketEntity.getTableInfoId()));
        }

        onLoading("单据处理中...");
        presenterRouter.create(WorkTicketSubmitAPI.class).submit("workTicketView", map, attachmentMap,__pc__);
    }

    private boolean checkTableBlank() {
        if (saferGalleryView.getGalleryAdapter().getItemCount() <= 0) {
            ToastUtils.show(context, "请拍摄照片！");
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
                refreshController.refreshComplete();
            }
        });
    }

    public void updateTableInfo(WorkTicketEntity entity) {
        this.mWorkTicketEntity = entity;

        // 工单、停电隐藏
        if (TextUtils.isEmpty(entity.getWorkList().tableNo)) {
            workListTableNo.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(entity.getOffApplyTableNo())) {
            eleOffTableNo.setVisibility(View.GONE);
        }
//        String flowStatus = Optional.ofNullable(entity.getFlowStatus()).map(new Function<SystemCodeEntity, String>() {
//            @Override
//            public String apply(SystemCodeEntity systemCodeEntity) {
//                return systemCodeEntity.id;
//            }
//        }).orElse("");
        String flowStatus = entity.getFlowStatus() == null ? "" : entity.getFlowStatus().id;
        if (flowStatus.equals("WorkTicket_003/03")) { // 安全员审核
            centralControlRoom.setVisibility(View.VISIBLE); // 显示中控室
        } else if (flowStatus.equals("WorkTicket_003/04")) { // 领导审批
            centralControlRoom.setVisibility(View.VISIBLE); // 显示中控室
            securityStaff.setVisibility(View.VISIBLE); // 显示安全员
        } else if (flowStatus.equals("WorkTicket_003/05")) { // 生效
            centralControlRoom.setVisibility(View.VISIBLE); // 显示中控室
            securityStaff.setVisibility(View.VISIBLE); // 显示安全员
            securityChiefStaff.setVisibility(View.VISIBLE); // 显示安保科科长
            controlDirectorStaff.setVisibility(View.VISIBLE); // 显示调度室主任
        } else {

        }

        //回填单据表头信息
        workListTableNo.setContent(entity.getWorkList().tableNo);
        eleOffTableNo.setContent(entity.getOffApplyTableNo());
        chargeStaff.setContent(entity.getChargeStaff().name);
        workShop.setContent(entity.getWorkShop().name);
        eamName.setContent(entity.getEamId().name);
        eamCode.setContent(entity.getEamId().eamAssetCode);
        content.setContent(entity.getContent());
        riskAssessmentView.setContent(entity.getRiskAssessment().value);
        hazardContrlPointValue = "";
        for (SystemCodeEntity systemCodeEntity : mHazardList) {
            if (!TextUtils.isEmpty(entity.getHazardsourContrpoint()) && entity.getHazardsourContrpoint().contains(systemCodeEntity.id)) {
                hazardContrlPointValue = hazardContrlPointValue + systemCodeEntity.value + ",";
            }
        }
        hazardCtrlPoint.setSpinner(hazardContrlPointValue.length() > 0 ? hazardContrlPointValue.substring(0, hazardContrlPointValue.length() - 1) : "");
        centralControlRoom.setContent(entity.getCentContRoom().name);
        securityStaff.setContent(entity.getSecurityStaff().name);
        securityChiefStaff.setContent(entity.getSecurityChiefStaff().name);
        controlDirectorStaff.setContent(entity.getContrDirectorStaff().name);

        initPic();
    }

    /**
     * 加载图片
     */
    private void initPic() {
        if (mWorkTicketEntity != null) {
            getController(AttachmentController.class).refreshGalleryView(new OnAPIResultListener<AttachmentListEntity>() {
                @Override
                public void onFail(String errorMsg) {
                }

                @Override
                public void onSuccess(AttachmentListEntity result) {
                    if (result.result.size() > 0) {
                        getController(OnlineCameraController.class).setPicData(result.result, "WorkTicket_8.20.3.03_workTicket");
                    }
                }
            }, mWorkTicketEntity.getTableInfoId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        EventBus.getDefault().post(new RefreshEvent());
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
     * 全屏展示时监听图片删除
     *
     * @param imageDeleteEvent
     */
    @Subscribe
    public void onReceiveImageDeleteEvent(ImageDeleteEvent imageDeleteEvent) {
        getController(OnlineCameraController.class).deleteGalleryBean(saferGalleryView.getGalleryAdapter().getList().get(imageDeleteEvent.getPos()), imageDeleteEvent.getPos());
        EventBus.getDefault().post(new RefreshEvent());
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

    }

    @Override
    public void retrialFailed(String errorMsg) {

    }
}
