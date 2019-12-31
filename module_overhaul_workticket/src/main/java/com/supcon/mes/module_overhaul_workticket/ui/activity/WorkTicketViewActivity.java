package com.supcon.mes.module_overhaul_workticket.ui.activity;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
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
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.PcController;
import com.supcon.mes.middleware.controller.TableInfoController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
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

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/26
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Router(value = Constant.Router.OVERHAUL_WORKTICKET_VIEW)
@Presenter(value = {WorkTicketSubmitPresenter.class})
@Controller(value = {SafetyMeasuresController.class, LinkController.class, PcController.class, TableInfoController.class, WorkTicketCameraController.class})
public class WorkTicketViewActivity extends BaseRefreshActivity implements WorkTicketSubmitContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("workListTableNo")
    CustomTextView workListTableNo;
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
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;
    @BindByTag("workFlowView")
    CustomWorkFlowView workFlowView;
    @BindByTag("riskAssessmentRadioGroup")
    RadioGroup riskAssessmentRadioGroup;

    @BindByTag("hazardRecyclerView")
    RecyclerView hazardRecyclerView;


    private String __pc__;
    private Long tableId; // 单据ID
    private Long pendingId; // 代办Id
    private WorkTicketEntity mWorkTicketEntity;
    private WorkTicketEntity mWorkTicketEntityOld;
    private String hazardContrlPointValue;
    private List<SystemCodeEntity> mRiskAssessmentList;
    private List<SystemCodeEntity> mHazardList;
    private SinglePickController mSinglePickController;
    private HazardPointAdapter hazardPointAdapter;
    private String name = ""; // 当前活动名称
    private boolean editable;

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID, -1);
        pendingId = getIntent().getLongExtra(Constant.IntentKey.PENDING_ID, -1);
        editable = getIntent().getBooleanExtra(Constant.IntentKey.IS_EDITABLE, false);

//        hazardContrlPointValue = getIntent().getStringExtra(Constant.IntentKey.HAZARD_CONTRL_POINT);
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

        if (editable) {
            getController(LinkController.class).setCancelShow(true);
            getController(LinkController.class).setOnSuccessListener(result -> {
                //获取__pc__
                name = result.toString();
                getSubmitPc(name);
            });
            getController(LinkController.class).initPendingTransition(workFlowView, pendingId);

        } else {
            workFlowView.setVisibility(View.GONE);
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
                getController(SafetyMeasuresController.class).initData();
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
                    IntentRouter.go(context, Constant.Router.EAM);
                }
            }
        });

        riskAssessmentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                mWorkTicketEntity.setRiskAssessment(mRiskAssessmentList.get(checkedId % 1000));
            }
        });

//        riskAssessment.setOnChildViewClickListener(new OnChildViewClickListener() {
//            @Override
//            public void onChildViewClick(View childView, int action, Object obj) {
//                if (action == -1){
//                    mWorkTicketEntity.getRiskAssessment().id = null;
//                }else {
//                    if (mRiskAssessmentList.size() <= 0){
//                        ToastUtils.show(context, "风险评估列表数据为空,请重新加载页面！");
//                        return;
//                    }
//
//                    mSinglePickController
//                    .list(FieldHelper.getSystemCodeValue(mRiskAssessmentList))
//                    .listener(new SinglePicker.OnItemPickListener() {
//                        @Override
//                        public void onItemPicked(int index, Object item) {
//                            riskAssessment.setContent(item.toString());
//                            mWorkTicketEntity.setRiskAssessment(mRiskAssessmentList.get(index));
//                        }
//                    }).show(riskAssessment.getContent());
//                }
//            }
//        });

//        hazardPointAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
//            @Override
//            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
//                List<HazardPointEntity> hazardPointEntityList = (List<HazardPointEntity>) obj;
//                StringBuilder sbValue = new StringBuilder();
//                StringBuilder sbIds = new StringBuilder();
//                for (HazardPointEntity entity : hazardPointEntityList) {
//                    if (entity.checked) {
//                        sbValue.append(entity.value).append(",");
//                        sbIds.append(entity.id).append(",");
//                    }
//                }
//                hazardCtrlPoint.setSpinner(sbValue.length() > 0 ? sbValue.substring(0, sbValue.length() - 1) : "");
//                hazardCtrlPoint.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
//                mWorkTicketEntity.setHazardsourContrpoint(sbIds.length() > 0 ? sbIds.substring(0, sbIds.length() - 1) : "");
//                mWorkTicketEntity.setHazardsourContrpointForDisplay(hazardCtrlPoint.getContent());
//            }
//        });

        workFlowView.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
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

    }

    private void doSave() {
        submit(null);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
//        if (!Constant.Transition.CANCEL_CN.equals(workFlowVar.dec) && checkTableBlank()) {
//            return;
//        }
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
//        map.put("ohworkticket.remark", remark.getContent());
        map.put("ohworkticket.hazardsourContrpoint", mWorkTicketEntity.getHazardsourContrpoint());
        map.put("ohworkticket.value", mWorkTicketEntity.getHazardsourContrpointForDisplay());
        map.put("__file_upload", true);

        // 表单
        List<SafetyMeasuresEntityDto> dgList = WorkTicketHelper.getSafetyMeasuresDto(getController(SafetyMeasuresController.class).getSafetyMeasuresEntityList());
        map.put("dg1575615975095ModelCode", "WorkTicket_8.20.3.03_workTicket_OhwticketPart");
        map.put("dg1575615975095ListJson", dgList.toString());
        map.put("dgLists['dg1575615975095']", dgList.toString());

        // 附件
//        Map<String, Object> attachmentMap = new HashMap<>();
//        getController(OnlineCameraController.class).doSave(attachmentMap);
//        if (attachmentMap.size() != 0) {
//            attachmentMap.put("linkId", String.valueOf(mWorkTicketEntity.getTableInfoId()));
//        }

        onLoading("单据处理中...");
        presenterRouter.create(WorkTicketSubmitAPI.class).submit("workTicketView",map, /*attachmentMap,*/__pc__);
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
        if (TextUtils.isEmpty(hazardCtrlPoint.getContent())) {
            ToastUtils.show(context, "危险源控制点不允许为空！");
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
        //初始化危险控制源
//        hazardPointAdapter.setList(WorkTicketHelper.getHazardPointBySystemCode(mHazardList, mWorkTicketEntity.getHazardsourContrpoint()));
//        hazardPointAdapter.notifyDataSetChanged();

        // 工单来源不可编辑
        if (TextUtils.isEmpty(entity.getWorkList().tableNo)) {
            workListTableNo.setVisibility(View.GONE);
        }
        //回填单据表头信息
        workListTableNo.setContent(entity.getWorkList().tableNo);
        chargeStaff.setContent(entity.getChargeStaff().name);
        workShop.setContent(entity.getWorkShop().name);
        eamName.setContent(entity.getEamId().name);
        eamCode.setContent(entity.getEamId().code);
        riskAssessmentView.setContent(entity.getRiskAssessment().value);
        hazardCtrlPoint.setSpinner(getIntent().getStringExtra(Constant.IntentKey.HAZARD_CONTRL_POINT));

        //初始化风险评估
//        FilterHelper.addRadioView(this, riskAssessmentRadioGroup,
//                FilterHelper.createFilterBySystemCode(Constant.SystemCode.RISK_ASSESSMENT,mWorkTicketEntity.getRiskAssessment().id), 50, WRAP_CONTENT);

//        mWorkTicketEntityOld = GsonUtil.gsonToBean(mWorkTicketEntity.toString(), WorkTicketEntity.class);
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
            chargeStaff.setContent(staff.name);
            workShop.setContent(staff.department);

            mWorkTicketEntity.getChargeStaff().id = staff.id;
            mWorkTicketEntity.getChargeStaff().name = staff.name;
            mWorkTicketEntity.getChargeStaff().code = staff.code;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEam(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity instanceof EamEntity) {
            EamEntity eam = (EamEntity) commonSearchEvent.commonSearchEntity;
            eamName.setContent(eam.name);
            eamCode.setContent(eam.code);

            mWorkTicketEntity.getEamId().id = eam.id;
            mWorkTicketEntity.getEamId().name = eam.name;
            mWorkTicketEntity.getEamId().code = eam.code;
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
}
