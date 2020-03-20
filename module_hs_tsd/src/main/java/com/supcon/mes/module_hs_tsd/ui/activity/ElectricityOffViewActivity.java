package com.supcon.mes.module_hs_tsd.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomListWidget;
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
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_hs_tsd.IntentRouter;
import com.supcon.mes.module_hs_tsd.R;
import com.supcon.mes.module_hs_tsd.constant.ElectricityConstant;
import com.supcon.mes.module_hs_tsd.controller.OperateItemOffController;
import com.supcon.mes.module_hs_tsd.model.api.ElectricitySubmitAPI;
import com.supcon.mes.module_hs_tsd.model.bean.EleOffOnTemplate;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnEntity;
import com.supcon.mes.module_hs_tsd.model.contract.ElectricitySubmitContract;
import com.supcon.mes.module_hs_tsd.presenter.ElectricityOffSubmitPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/27
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Router(value = Constant.Router.HS_ELE_OFF_VIEW)
@Presenter(value = {ElectricityOffSubmitPresenter.class})
@Controller(value = {OperateItemOffController.class, LinkController.class, PcController.class, TableInfoController.class, OnlineCameraController.class, AttachmentController.class})
public class ElectricityOffViewActivity extends BaseRefreshActivity implements ElectricitySubmitContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("workListTableNo")
    CustomTextView workListTableNo;
    @BindByTag("applyStaff")
    CustomTextView applyStaff;
    @BindByTag("department")
    CustomTextView department;
    @BindByTag("eamName")
    CustomTextView eamName;
    @BindByTag("eamCode")
    CustomTextView eamCode;
    @BindByTag("applyDate")
    CustomDateView applyDate;
    @BindByTag("operateStaff")
    CustomTextView operateStaff;
    @BindByTag("workTask")
    CustomVerticalEditText workTask;
    @BindByTag("galleryView")
    CustomGalleryView galleryView;
    @BindByTag("operateItemWidget")
    CustomListWidget operateItemWidget;
    @BindByTag("contentView")
    ScrollView contentView;
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;
    @BindByTag("workFlowView")
    CustomWorkFlowView workFlowView;
    @BindByTag("finishBtn")
    Button finishBtn;

    private String __pc__;
    private Long tableId; // 单据ID
    private Long pendingId; // 代办Id
    private ElectricityOffOnEntity mElectricityOffOnEntity = new ElectricityOffOnEntity();
    private ElectricityOffOnEntity mElectricityOffOnEntityOld;
    private DatePickController mDatePickController;
    private String activityName = ""; // 当前活动名称
    private String tableStatus;

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID, -1);
        pendingId = getIntent().getLongExtra(Constant.IntentKey.PENDING_ID, -1);
        tableStatus = getIntent().getStringExtra(Constant.IntentKey.TABLE_STATUS);
        activityName = getIntent().getStringExtra(Constant.IntentKey.ACTIVITY_NAME);

        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(true);

        mDatePickController = new DatePickController(this);
        mDatePickController.textSize(18);
        mDatePickController.setCanceledOnTouchOutside(true);
        mDatePickController.setCycleDisable(false);
        mDatePickController.setSecondVisible(false);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_elec_off_edit;
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("停电申请查看");
        finishBtn.setVisibility(View.GONE);

        applyStaff.setEditable(false);
        eamName.setEditable(false);
        applyDate.setEditable(false);
        operateStaff.setEditable(false);
        workTask.setEditable(false);
        galleryView.setEditable(false);
        galleryView.setIconVisibility(false);

        if (pendingId != -1) {
            workFlowView.setVisibility(View.VISIBLE);
            if (tableStatus.contains(Constant.TableStatus_CH.NOTIFY)) { // 通知特殊处理
                getController(LinkController.class).setNotify(true, activityName);
            }
            getController(LinkController.class).setCancelShow(true);
            getController(LinkController.class).initPendingTransition(workFlowView, pendingId);
            getSubmitPc(activityName);

        } else {
            workFlowView.setVisibility(View.GONE);
        }

        getController(OnlineCameraController.class).init(Constant.IMAGE_SAVE_ELE_PATH,Constant.PicType.ELE_OFF_PIC);
        getController(OnlineCameraController.class).addGalleryView(0,galleryView);

    }

    /**
     * @param
     * @return 获取单据提交pc
     * @description
     * @author user 2019/12/27
     */
    private void getSubmitPc(String operateCode) {
        getController(PcController.class).queryPc(operateCode, "EleOnWorkFlow", new OnAPIResultListener<String>() {
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
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        refreshController.setOnRefreshListener(() -> {
            initTableInfoData();
            getController(OperateItemOffController.class).listOperateItems();
        });

        applyStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mElectricityOffOnEntity.getApplyStaff().id = null;
                department.setContent(null);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG,applyStaff.getTag().toString());
                IntentRouter.go(context, Constant.Router.STAFF,bundle);
            }
        });
        eamName.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    mElectricityOffOnEntity.getEamID().id = null;
                    eamCode.setContent(null);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.ELE_OFF_ON_TEMPLATE,false); // 停电模板
                    IntentRouter.go(context, Constant.Router.ELE_OFF_TEMPLATE,bundle);
                }
            }
        });
        applyDate.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    mElectricityOffOnEntity.setApplyDate(null);
                }else {
                    mDatePickController.listener((year, month, day, hour, minute, second) -> {
                        String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        applyDate.setContent(dateStr);
                        mElectricityOffOnEntity.setApplyDate(DateUtil.dateFormat(dateStr,Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC));
                    }).show(TextUtils.isEmpty(applyDate.getContent()) ? new Date().getTime() : mElectricityOffOnEntity.getApplyDate());
                }
            }
        });
        operateStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mElectricityOffOnEntity.getOperateStaff().id = null;
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG,operateStaff.getTag().toString());
                IntentRouter.go(context, Constant.Router.STAFF,bundle);
            }
        });
        RxTextView.textChanges(workTask.editText()).skipInitialValue()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Consumer<CharSequence>() {
                   @Override
                   public void accept(CharSequence charSequence) throws Exception {
                       mElectricityOffOnEntity.setWorkTask(charSequence.toString());
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
        map.put("onoroff.createStaffId", mElectricityOffOnEntity.getCreateStaffId());
        map.put("onoroff.createTime", DateUtil.dateTimeFormat(mElectricityOffOnEntity.getCreateTime()));
        map.put("onoroff.createPositionId", EamApplication.getAccountInfo().positionId);
        map.put("viewCode", "BEAMEle_1.0.0_onOrOff_eleOffEdit");
        map.put("modelName", "Onoroff");
        map.put("datagridKey", "BEAMEle_onOrOff_onoroff_eleOffEdit_datagrids");
        map.put("viewselect", "eleOffEdit");
        map.put("id", tableId.equals(-1L)? "" : tableId);
        map.put("deploymentId", mElectricityOffOnEntity.getDeploymentId());
        map.put("onoroff.version", mElectricityOffOnEntity.getVersion());
        if (workFlowEntities != null) {//保存为空
            map.put("workFlowVar.outcomeMapJson", workFlowEntities);
            map.put("workFlowVar.dec", workFlowEntity.dec);
            map.put("workFlowVar.outcome", workFlowEntity.outcome);
            map.put("operateType", "submit");
            if (Constant.Transition.CANCEL_CN.equals(workFlowEntity.dec)) {
                map.put("workFlowVarStatus",Constant.Transition.CANCEL);
            }
        } else {
            map.put("operateType", "save");
        }
        map.put("workFlowVar.comment", Util.strFormat2(workFlowView.getComment()));
//        map.put("taskDescription", "WorkTicket_8.20.3.03.workflow.randon1575618721430.flag");
        map.put("activityName", activityName);
        if (!pendingId.equals(-1L)){
            map.put("pendingId",pendingId);
        }
        //表头信息,取修改后最新数据
        map.put("onoroff.applyStaff.id", Util.strFormat2(mElectricityOffOnEntity.getApplyStaff().id));
        map.put("onoroff.eamID.id",Util.strFormat2(mElectricityOffOnEntity.getEamID().id));
        map.put("onoroff.eleTemplateId.id",Util.strFormat2(mElectricityOffOnEntity.getEleTemplateId().id));
        map.put("onoroff.operateStaff.id",Util.strFormat2(mElectricityOffOnEntity.getOperateStaff().id));
        map.put("onoroff.applyDate",applyDate.getContent());
        map.put("onoroff.workTask",workTask.getContent());
        map.put("__file_upload", true);

        // 表单
//        List<SafetyMeasuresEntityDto> dgList = WorkTicketHelper.getSafetyMeasuresDto(safetyMeasuresController.getSafetyMeasuresEntityList());
//        map.put("dg1575615975095ModelCode", "WorkTicket_8.20.3.03_workTicket_OhwticketPart");
//        map.put("dg1575615975095ListJson", dgList.toString());
//        map.put("dgLists['dg1575615975095']", dgList.toString());

        // 附件
        Map<String, Object> attachmentMap = new HashMap<>();
        getController(OnlineCameraController.class).doSave(attachmentMap);
        if (attachmentMap.size() != 0) {
            attachmentMap.put("linkId", mElectricityOffOnEntity.getTableInfoId() == null ? "" : String.valueOf(mElectricityOffOnEntity.getTableInfoId()));
        }

        onLoading("单据处理中...");
        presenterRouter.create(ElectricitySubmitAPI.class).submit("eleOffMainView",map, attachmentMap,__pc__);
    }

    private boolean checkTableBlank() {
        if (TextUtils.isEmpty(applyStaff.getValue())) {
            ToastUtils.show(context, "发令人不允许为空！");
            return true;
        }
        if (TextUtils.isEmpty(eamName.getValue())) {
            ToastUtils.show(context, "设备不允许为空！");
            return true;
        }
        if (TextUtils.isEmpty(applyDate.getContent())) {
            ToastUtils.show(context, "申请时间不允许为空！");
            return true;
        }
        if (TextUtils.isEmpty(operateStaff.getValue())) {
            ToastUtils.show(context, "操作人不允许为空！");
            return true;
        }
        return false;
    }

    protected void initTableInfoData() {
        getController(TableInfoController.class).getTableInfo(ElectricityConstant.URL.PRE_URL, tableId, ElectricityConstant.HeaderData.HEADER_DATA_INCLUDES, new OnAPIResultListener() {
            @Override
            public void onFail(String errorMsg) {
                refreshController.refreshComplete();
                ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
            }

            @Override
            public void onSuccess(Object result) {
                ElectricityOffOnEntity entity = GsonUtil.gsonToBean(GsonUtil.gsonString(result), ElectricityOffOnEntity.class);
                updateTableInfo(entity);
                refreshController.refreshComplete();
            }
        });
    }

    public void updateTableInfo(ElectricityOffOnEntity entity) {
        this.mElectricityOffOnEntity = entity;

        if (mElectricityOffOnEntity.getWorkRecordId() == null){
            workListTableNo.setVisibility(View.GONE);
        }
        //回填单据表头信息
        workListTableNo.setContent(entity.getWorkRecordTableNo());
        applyStaff.setContent(entity.getApplyStaff().name);
        department.setContent(entity.getApplyStaff().getMainPosition().getDepartment().name);
        eamName.setContent(entity.getEamID().name);
        eamCode.setContent(entity.getEamID().code);
        applyDate.setContent(entity.getApplyDate() == null ? "" : DateUtil.dateTimeFormat(entity.getApplyDate()));
        operateStaff.setContent(entity.getOperateStaff().name);
        workTask.setContent(entity.getWorkTask());

        mElectricityOffOnEntityOld = GsonUtil.gsonToBean(mElectricityOffOnEntity.toString(), ElectricityOffOnEntity.class);

        initPic();
    }

    /**
     * 加载图片
     */
    private void initPic() {
        if (mElectricityOffOnEntity != null) {
            getController(AttachmentController.class).refreshGalleryView(new OnAPIResultListener<AttachmentListEntity>() {
                @Override
                public void onFail(String errorMsg) {}

                @Override
                public void onSuccess(AttachmentListEntity result) {
                    if (result.result.size() > 0) {
                        mElectricityOffOnEntity.attachmentEntities = result.result;
                        getController(OnlineCameraController.class).setPicData(mElectricityOffOnEntity.attachmentEntities,"BEAMEle_1.0.0_onOrOff");
                    }
                }
            }, mElectricityOffOnEntity.getTableInfoId());
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
            if (applyStaff.getTag().toString().equals(commonSearchEvent.flag)){
                applyStaff.setContent(staff.name);
                department.setContent(staff.department);

                mElectricityOffOnEntity.getApplyStaff().id = staff.id;
                mElectricityOffOnEntity.getApplyStaff().name = staff.name;
                mElectricityOffOnEntity.getApplyStaff().code = staff.code;
            }else if (operateStaff.getTag().toString().equals(commonSearchEvent.flag)){
                operateStaff.setContent(staff.name);

                mElectricityOffOnEntity.getOperateStaff().id = staff.id;
                mElectricityOffOnEntity.getOperateStaff().name = staff.name;
                mElectricityOffOnEntity.getOperateStaff().code = staff.code;
            }else if ("selectPeopleInput".equals(commonSearchEvent.flag)){
                workFlowView.addStaff(staff.name,staff.id);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEam(EleOffOnTemplate eleOffOnTemplate) {
        if (eleOffOnTemplate != null) {
            eamName.setContent(eleOffOnTemplate.eamId.name);
            eamCode.setContent(eleOffOnTemplate.eamId.code);

            mElectricityOffOnEntity.setEamID(eleOffOnTemplate.eamId);
            mElectricityOffOnEntity.setEleTemplateId(eleOffOnTemplate);
        }
    }

    /**
     * 全屏展示时监听图片删除
     * @param imageDeleteEvent
     */
    @Subscribe
    public void onReceiveImageDeleteEvent(ImageDeleteEvent imageDeleteEvent) {
        getController(OnlineCameraController.class).deleteGalleryBean(galleryView.getGalleryAdapter().getList().get(imageDeleteEvent.getPos()), imageDeleteEvent.getPos());
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
}
