package com.supcon.mes.module_overhaul_workticket.ui.activity;

import android.annotation.SuppressLint;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.SinglePicker;
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
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.FieldHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_overhaul_workticket.IntentRouter;
import com.supcon.mes.module_overhaul_workticket.R;
import com.supcon.mes.module_overhaul_workticket.constant.WorkTicketConstant;
import com.supcon.mes.module_overhaul_workticket.controller.SafetyMeasuresController;
import com.supcon.mes.module_overhaul_workticket.model.bean.WorkTicketEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/10
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Router(value = Constant.Router.OVERHAUL_WORKTICKET_EDIT)
@Controller(value = {SafetyMeasuresController.class, LinkController.class,PcController.class, TableInfoController.class})
public class WorkTicketEditActivity extends BaseRefreshActivity {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("titleBarLayout")
    RelativeLayout titleBarLayout;
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
    @BindByTag("riskAssessment")
    CustomSpinner riskAssessment;
    @BindByTag("hazardCtrlPoint")
    CustomSpinner hazardCtrlPoint;
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;
    @BindByTag("workFlowView")
    CustomWorkFlowView workFlowView;

    private String __pc__;
    private Long tableId; // 单据ID
    private Long pendingId; // 代办Id
    private WorkTicketEntity mWorkTicketEntity;
    private WorkTicketEntity mWorkTicketEntityOld;
    private String hazardContrlPointValue;
    private List<SystemCodeEntity> mRiskAccmentList;
    private List<SystemCodeEntity> mHazardList;
    private SinglePickController mSinglePickController;

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);

        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID,-1);
        pendingId = getIntent().getLongExtra(Constant.IntentKey.PENDING_ID,-1);
        hazardContrlPointValue = getIntent().getStringExtra(Constant.IntentKey.HAZARD_CONTRL_POINT);
        // 手工制定无需刷新
        if (!tableId.equals(-1L) && !pendingId.equals(-1L)){
            refreshController.setAutoPullDownRefresh(true);
            refreshController.setPullDownRefreshEnabled(true);
        }else {
            refreshController.setAutoPullDownRefresh(false);
            refreshController.setPullDownRefreshEnabled(false);
        }

        mSinglePickController = new SinglePickController<String>(this);
        mSinglePickController.textSize(18);
        mSinglePickController.setCanceledOnTouchOutside(true);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_work_ticket_edit;
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("检修工作票");
        if (pendingId.equals(-1L)){
            // 制定单据工作流
            getController(LinkController.class).initStartTransition(workFlowView,"workTicketFW");
            getSubmitPc("start_op3wj2a"); // 通过pc端菜单管理中相应菜单获取制定 操作编码
        }else {
            getController(LinkController.class).setOnSuccessListener(result -> {
                //获取__pc__
                getSubmitPc(result.toString());
            });
            getController(LinkController.class).initPendingTransition(workFlowView,pendingId);
        }
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
        // 手工制定时，初始化页面
        if (tableId.equals(-1L) && pendingId.equals(-1L)){
            mWorkTicketEntity = new WorkTicketEntity();
            mWorkTicketEntity.setDeploymentId(getIntent().getLongExtra(Constant.IntentKey.DEPLOYMENT_ID,-1));
            Staff chargeStaff = new Staff();
            chargeStaff.id = EamApplication.getAccountInfo().staffId;
            chargeStaff.code = EamApplication.getAccountInfo().staffCode;
            chargeStaff.name = EamApplication.getAccountInfo().staffName;
            chargeStaff.getMainPosition().name = EamApplication.getAccountInfo().positionName;
            chargeStaff.mainPosition.getDepartment().name = EamApplication.getAccountInfo().departmentName;
            mWorkTicketEntity.setCreateTime(new Date().getTime());
            mWorkTicketEntity.setCreateStaff(chargeStaff);
            mWorkTicketEntity.setChargeStaff(chargeStaff);

            updateTableInfo(mWorkTicketEntity);
        }

        mRiskAccmentList = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.RISK_ACCEMENT);
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
            if (action == -1){
                mWorkTicketEntity.getChargeStaff().id = null;
                mWorkTicketEntity.getWorkShop().id = null;
            }else {
                IntentRouter.go(context,Constant.Router.STAFF);
            }
        });
        eamName.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    mWorkTicketEntity.getEamId().id = null;
                    eamCode.setContent(null);
                }else {
                    IntentRouter.go(context,Constant.Router.EAM);
                }
            }
        });

        riskAssessment.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    mWorkTicketEntity.getRiskAssessment().id = null;
                }else {
                    if (mRiskAccmentList.size() <= 0){
                        ToastUtils.show(context, "风险评估列表数据为空,请重新加载页面！");
                        return;
                    }

                    mSinglePickController
                    .list(FieldHelper.getSystemCodeValue(mRiskAccmentList))
                    .listener(new SinglePicker.OnItemPickListener() {
                        @Override
                        public void onItemPicked(int index, Object item) {
                            riskAssessment.setContent(item.toString());
                            mWorkTicketEntity.setRiskAssessment(mRiskAccmentList.get(index));
                        }
                    }).show(riskAssessment.getContent());
                }
            }
        });
        hazardCtrlPoint.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    mWorkTicketEntity.setHazardsourContrpoint(null);
                    mWorkTicketEntity.setHazardsourContrpointForDisplay(null);
                }else {
                    if (mHazardList.size() <= 0){
                        ToastUtils.show(context, "危险源控制点列表数据为空,请重新加载页面！");
                        return;
                    }

                    mSinglePickController
                            .list(FieldHelper.getSystemCodeValue(mHazardList))
                            .listener(new SinglePicker.OnItemPickListener() {
                                @Override
                                public void onItemPicked(int index, Object item) {
                                    hazardCtrlPoint.setContent(item.toString());
//                                    mWorkTicketEntity.setRiskAssessment(mHazardList.get(index));
//                                    mWorkTicketEntity.setHazardsourContrpoint(item.toString());
//                                    mWorkTicketEntity.setHazardsourContrpointForDisplay(item.toString());
                                }
                            }).show(hazardCtrlPoint.getContent());
                }
            }
        });

    }

    protected void initTableInfoData() {
        getController(TableInfoController.class).getTableInfo(WorkTicketConstant.URL.PRE_URL,tableId, WorkTicketConstant.HeaderData.HEADER_DATA_INCLUDES, new OnAPIResultListener() {
            @Override
            public void onFail(String errorMsg) {
                refreshController.refreshComplete();
                ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
            }

            @Override
            public void onSuccess(Object result) {
                WorkTicketEntity entity = GsonUtil.gsonToBean(GsonUtil.gsonString(result),WorkTicketEntity.class);
                updateTableInfo(entity);
                refreshController.refreshComplete();
            }
        });
    }

    public void updateTableInfo(WorkTicketEntity entity){
        this.mWorkTicketEntity = entity;

        // 工单来源不可编辑
        if (!TextUtils.isEmpty(entity.getWorkList().tableNo)){
//            chargeStaff.setEditable(false);
            eamName.setEditable(false);
//            workShop.setEditable(false);
        }else {
            workListTableNo.setVisibility(View.GONE);
        }
        //回填单据表头信息
        workListTableNo.setContent(entity.getWorkList().tableNo);
        chargeStaff.setContent(entity.getChargeStaff().name);
        workShop.setContent(entity.getWorkShop().name);
        eamName.setContent(entity.getEamId().name);
        eamCode.setContent(entity.getEamId().code);
        riskAssessment.setContent(entity.getRiskAssessment().value);
        hazardCtrlPoint.setContent(hazardContrlPointValue);

        mWorkTicketEntityOld = GsonUtil.gsonToBean(mWorkTicketEntity.toString(),WorkTicketEntity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveStaff(CommonSearchEvent commonSearchEvent){
        if (commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff){
            CommonSearchStaff staff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;
            chargeStaff.setContent(staff.name);
            workShop.setContent(staff.department);

            mWorkTicketEntity.getChargeStaff().id = staff.id;
            mWorkTicketEntity.getChargeStaff().name = staff.name;
            mWorkTicketEntity.getChargeStaff().code = staff.code;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEam(CommonSearchEvent commonSearchEvent){
        if (commonSearchEvent.commonSearchEntity instanceof EamEntity){
            EamEntity eam = (EamEntity) commonSearchEvent.commonSearchEntity;
            eamName.setContent(eam.name);
            eamCode.setContent(eam.code);

            mWorkTicketEntity.getEamId().id = eam.id;
            mWorkTicketEntity.getEamId().name = eam.name;
            mWorkTicketEntity.getEamId().code = eam.code;
        }
    }


}
