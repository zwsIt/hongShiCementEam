package com.supcon.mes.module_hs_tsd.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AttachmentController;
import com.supcon.mes.middleware.controller.DealInfoController;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.controller.PcController;
import com.supcon.mes.middleware.controller.TableInfoController;
import com.supcon.mes.middleware.controller.WorkFlowKeyController;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.ProcessKeyUtil;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_hs_tsd.R;
import com.supcon.mes.module_hs_tsd.constant.ElectricityConstant;
import com.supcon.mes.module_hs_tsd.controller.OperateItemOnController;
import com.supcon.mes.module_hs_tsd.model.api.ElectricitySubmitAPI;
import com.supcon.mes.module_hs_tsd.model.bean.EleOffOnTemplate;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnEntity;
import com.supcon.mes.module_hs_tsd.model.contract.ElectricitySubmitContract;
import com.supcon.mes.module_hs_tsd.model.dto.OperateItemEntityDto;
import com.supcon.mes.module_hs_tsd.presenter.ElectricityOffSubmitPresenter;
import com.supcon.mes.module_hs_tsd.util.OperateItemHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/27
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Router(value = Constant.Router.HS_ELE_ON_EDIT)
@Presenter(value = {ElectricityOffSubmitPresenter.class})
@Controller(value = {OperateItemOnController.class, LinkController.class, PcController.class, TableInfoController.class,
        OnlineCameraController.class, AttachmentController.class, WorkFlowKeyController.class})
public class ElectricityOnEditActivity extends BaseRefreshActivity implements ElectricitySubmitContract.View {

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
    @BindByTag("chargeStaff")
    CustomTextView chargeStaff;
    @BindByTag("electrician")
    CustomTextView electrician;
    @BindByTag("securityStaff")
    CustomTextView securityStaff;
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
    @BindByTag("recyclerView")
    RecyclerView recyclerView;

    private String __pc__;
    private Long tableId; // 单据ID
    private Long pendingId; // 代办Id
    private ElectricityOffOnEntity mElectricityOffOnEntity = new ElectricityOffOnEntity();
    private ElectricityOffOnEntity mElectricityOffOnEntityOld;
    private DatePickController mDatePickController;
    private String name = ""; // 当前活动名称
    private ImageView customCameraIv;
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

        mDatePickController = new DatePickController(this);
        mDatePickController.textSize(18);
        mDatePickController.setCanceledOnTouchOutside(true);
        mDatePickController.setCycleDisable(false);
        mDatePickController.setSecondVisible(false);
        mDealInfoController = new  DealInfoController(context,recyclerView,null);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_elec_on_edit;
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("送电申请编辑");
        operateStaff.setVisibility(View.GONE);
        getController(LinkController.class).setCancelShow(true);
        if (pendingId.equals(-1L)) {
            // 制定单据工作流
            getController(WorkFlowKeyController.class).queryWorkFlowKeyOnly(Constant.EntityCode.ELE_ON_OFF, Constant.EntityCodeType.ELE_ON, new OnAPIResultListener<Object>() {
                @Override
                public void onFail(String errorMsg) {
                    ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
                }

                @Override
                public void onSuccess(Object result) {
                    getController(LinkController.class).initStartTransition(workFlowView, String.valueOf(result));
                }
            });

            getSubmitPc("start376"); // 通过pc端菜单管理中相应菜单获取制定 操作编码
        } else {
            getController(LinkController.class).setOnSuccessListener(result -> {
                //获取__pc__
                name = result.toString();
                getSubmitPc(name);
            });
            getController(LinkController.class).initPendingTransition(workFlowView, pendingId);
        }

        getController(OnlineCameraController.class).init(Constant.IMAGE_SAVE_ELE_PATH,Constant.PicType.ELE_ON_PIC);
        getController(OnlineCameraController.class).addGalleryView(0,galleryView);

        customCameraIv = galleryView.findViewById(R.id.customCameraIv);
        galleryView.setNecessary(false);
    }

    /**
     * @param
     * @return 获取单据提交pc
     * @description
     * @author user 2019/12/27
     */
    private void getSubmitPc(String operateCode) {
        getController(WorkFlowKeyController.class).queryWorkFlowKeyToPc(operateCode,Constant.EntityCode.ELE_ON_OFF, Constant.EntityCodeType.ELE_ON, new OnAPIResultListener<Object>() {
            @Override
            public void onFail(String errorMsg) {
                ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
            }

            @Override
            public void onSuccess(Object result) {
                __pc__ = String.valueOf(result);
            }
        });
//        getController(PcController.class).queryPc(operateCode, ProcessKeyUtil.ELE_ON, new OnAPIResultListener<String>() {
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

    @Override
    protected void initData() {
        super.initData();

        // 手工制定时，初始化页面
        if (tableId.equals(-1L) && pendingId.equals(-1L)) {

            mElectricityOffOnEntity.setDeploymentId(getIntent().getLongExtra(Constant.IntentKey.DEPLOYMENT_ID, -1));
            Staff applyStaff = new Staff();
            applyStaff.id = EamApplication.getAccountInfo().staffId;
            applyStaff.code = EamApplication.getAccountInfo().staffCode;
            applyStaff.name = EamApplication.getAccountInfo().staffName;
            applyStaff.getMainPosition().name = EamApplication.getAccountInfo().positionName;
            applyStaff.mainPosition.getDepartment().name = EamApplication.getAccountInfo().departmentName;
            mElectricityOffOnEntity.setCreateTime(new Date().getTime());
            mElectricityOffOnEntity.setCreateStaff(applyStaff);
            mElectricityOffOnEntity.setCreateStaffId(applyStaff.id);
            mElectricityOffOnEntity.setApplyStaff(applyStaff);
            mElectricityOffOnEntity.setApplyDate(mElectricityOffOnEntity.getCreateTime());
            mElectricityOffOnEntity.setOperateStaff(applyStaff); // 停电操作人默认当前账号

            updateTableInfo(mElectricityOffOnEntity);
        }

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        refreshController.setOnRefreshListener(() -> {
            initTableInfoData();
            getController(OperateItemOnController.class).listOperateItems();
        });

        applyStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mElectricityOffOnEntity.getApplyStaff().id = null;
                department.setContent(null);
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG,applyStaff.getTag().toString());
                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);

//                Bundle bundle = new Bundle();
//                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG,applyStaff.getTag().toString());
//                IntentRouter.go(context, Constant.Router.STAFF,bundle);
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
                    bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
                    bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                    bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, eamName.getTag().toString());
                    bundle.putBoolean(Constant.IntentKey.IS_SELECT,true);
                    IntentRouter.go(context, Constant.Router.EAM_TREE_SELECT, bundle);
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
        chargeStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mElectricityOffOnEntity.getChargeStaff().id = null;
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG,chargeStaff.getTag().toString());
                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
            }
        });
        electrician.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mElectricityOffOnEntity.getElectrician().id = null;
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG,electrician.getTag().toString());
                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
            }
        });
        securityStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mElectricityOffOnEntity.getSecurityStaff().id = null;
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG,securityStaff.getTag().toString());
                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
            }
        });

        RxTextView.textChanges(workTask.editText()).skipInitialValue()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(charSequence -> mElectricityOffOnEntity.setWorkTask(charSequence.toString()));

        workFlowView.setOnChildViewClickListener((childView, action, obj) -> {
            WorkFlowVar workFlowVar = (WorkFlowVar) obj;
            switch (action) {
                case 0:
                    doSave();
                    break;
                case 1:
                case 2:
                    doSubmit(workFlowVar);
                    break;
                case 4:
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.IS_MULTI, true);
                    bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                    bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, "selectPeopleInput");
                    com.supcon.mes.module_hs_tsd.IntentRouter.go(context,Constant.Router.STAFF,bundle);
                    break;
                default:
                    break;
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
        if (!Constant.Transition.CANCEL_CN.equals(workFlowVar.dec) && checkTableBlank()) {
            return;
        }
//        List<WorkFlowEntity> workFlowEntityList = new ArrayList<>();
//        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
//        workFlowEntity.dec = workFlowVar.dec;
//        workFlowEntity.type = workFlowVar.outcomeMapJson.get(0).type;
//        workFlowEntity.outcome = workFlowVar.outCome;
//        workFlowEntityList.add(workFlowEntity);

        submit(workFlowVar.outcomeMapJson);
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
        map.put("viewCode", "BEAMEle_1.0.0_onOrOff_eleOnEdit");
        map.put("modelName", "Onoroff");
        map.put("datagridKey", "BEAMEle_onOrOff_onoroff_eleOnEdit_datagrids");
        map.put("viewselect", "eleOnEdit");
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
        map.put("activityName", name);
        if (pendingId.equals(-1L)){
            map.put("onoroff.applyType.id","BEAMEle001/02");
            map.put("onoroff.applyStatus.id","BEAMEle002/01");
        }else {
            map.put("pendingId",pendingId);
        }
        //表头信息,取修改后最新数据
        map.put("onoroff.applyStaff.id", Util.strFormat2(mElectricityOffOnEntity.getApplyStaff().id));
        map.put("onoroff.eamID.id",Util.strFormat2(mElectricityOffOnEntity.getEamID().id));
        map.put("onoroff.eleTemplateId.id",Util.strFormat2(mElectricityOffOnEntity.getEleTemplateId().id));
//        map.put("onoroff.operateStaff.id",Util.strFormat2(mElectricityOffOnEntity.getOperateStaff().id));
        map.put("onoroff.securityStaff.id",Util.strFormat2(mElectricityOffOnEntity.getSecurityStaff().id));
        map.put("onoroff.electrician.id",Util.strFormat2(mElectricityOffOnEntity.getElectrician().id));
        map.put("onoroff.chargeStaff.id",Util.strFormat2(mElectricityOffOnEntity.getChargeStaff().id));
        map.put("onoroff.applyDate",applyDate.getContent());
        map.put("onoroff.operateDate",applyDate.getContent()); // 操作时间默认申请时间
        map.put("onoroff.workTask",workTask.getContent());
        map.put("__file_upload", true);

         //表单
        List<OperateItemEntityDto> dgList = OperateItemHelper.getOperateItemEntityDto(getController(OperateItemOnController.class).getOperateItemEntityList());
        map.put("dg1545369078671ModelCode", "BEAMEle_1.0.0_onOrOff_Caution");
        map.put("dg1545369078671ListJson", dgList.toString());
        map.put("dgLists['dg1545369078671']", dgList.toString());

        // 附件
        Map<String, Object> attachmentMap = new HashMap<>();
        getController(OnlineCameraController.class).doSave(attachmentMap);
        if (attachmentMap.size() != 0) {
            attachmentMap.put("linkId", mElectricityOffOnEntity.getTableInfoId() == null ? "" : String.valueOf(mElectricityOffOnEntity.getTableInfoId()));
        }

        onLoading("单据处理中...");
        presenterRouter.create(ElectricitySubmitAPI.class).submit("eleOnEdit",map, attachmentMap,__pc__);
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
        if (TextUtils.isEmpty(chargeStaff.getValue())) {
            ToastUtils.show(context, "检修负责人不允许为空！");
            return true;
        }
        if (TextUtils.isEmpty(electrician.getValue())) {
            ToastUtils.show(context, "电工不允许为空！");
            return true;
        }
        if (TextUtils.isEmpty(securityStaff.getValue())) {
            ToastUtils.show(context, "安全员不允许为空！");
            return true;
        }
        if (workTask.isNecessary() && TextUtils.isEmpty(workTask.getContent())) {
            ToastUtils.show(context, "内容不允许为空！");
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
                // 加载处理意见
                if (mElectricityOffOnEntity.getTableInfoId() != null){
                    mDealInfoController.listTableDealInfo(ElectricityConstant.URL.PRE_URL,mElectricityOffOnEntity.getTableInfoId());
                }
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
        eamCode.setContent(entity.getEamID().eamAssetCode);
        applyDate.setContent(entity.getApplyDate() == null ? "" : DateUtil.dateTimeFormat(entity.getApplyDate()));
        operateStaff.setContent(entity.getOperateStaff().name);
        workTask.setContent(entity.getWorkTask());

        mElectricityOffOnEntityOld = GsonUtil.gsonToBean(mElectricityOffOnEntity.toString(), ElectricityOffOnEntity.class);

        if (!tableId.equals(-1L) && !pendingId.equals(-1L)) {
            // 加载图片
            initPic();
        }
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
        if (isUpdated()){
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
        }else {
            finish();
            EventBus.getDefault().post(new RefreshEvent());
        }
    }

    /**
     * 是否单据发生更新
     * @return
     */
    private boolean isUpdated() {
        if (mElectricityOffOnEntityOld != null && !mElectricityOffOnEntityOld.toString().equals(mElectricityOffOnEntity.toString())){
            return true;
        }
        return false;
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
            }else if (chargeStaff.getTag().toString().equals(commonSearchEvent.flag)){
                chargeStaff.setContent(staff.name);

                mElectricityOffOnEntity.getChargeStaff().id = staff.id;
                mElectricityOffOnEntity.getChargeStaff().name = staff.name;
                mElectricityOffOnEntity.getChargeStaff().code = staff.code;
            }else if (electrician.getTag().toString().equals(commonSearchEvent.flag)){
                electrician.setContent(staff.name);

                mElectricityOffOnEntity.getElectrician().id = staff.id;
                mElectricityOffOnEntity.getElectrician().name = staff.name;
                mElectricityOffOnEntity.getElectrician().code = staff.code;
            }else if (securityStaff.getTag().toString().equals(commonSearchEvent.flag)){
                securityStaff.setContent(staff.name);

                mElectricityOffOnEntity.getSecurityStaff().id = staff.id;
                mElectricityOffOnEntity.getSecurityStaff().name = staff.name;
                mElectricityOffOnEntity.getSecurityStaff().code = staff.code;
            }else if ("selectPeopleInput".equals(commonSearchEvent.flag)){
                workFlowView.addStaff(staff.name,staff.userId);
            }
        }else if (commonSearchEvent.mCommonSearchEntityList != null){ // 多选
            if ("selectPeopleInput".equals(commonSearchEvent.flag)){
                List<CommonSearchEntity> mCommonSearchEntityList = commonSearchEvent.mCommonSearchEntityList;
                CommonSearchStaff staff;
                for (CommonSearchEntity commonSearchEntity : mCommonSearchEntityList){
                    staff = (CommonSearchStaff) commonSearchEntity;
                    workFlowView.addStaff(staff.name, staff.userId);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEam(EleOffOnTemplate eleOffOnTemplate) {
        if (eleOffOnTemplate != null) {
            eamName.setContent(eleOffOnTemplate.eamId.name);
            eamCode.setContent(eleOffOnTemplate.eamId.eamAssetCode);

            mElectricityOffOnEntity.setEamID(eleOffOnTemplate.eamId);
            mElectricityOffOnEntity.setEleTemplateId(eleOffOnTemplate);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEam(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity instanceof EamEntity) {
            EamEntity eam = (EamEntity) commonSearchEvent.commonSearchEntity;
            eamName.setContent(eam.name);
            eamCode.setContent(eam.eamAssetCode);
            mElectricityOffOnEntity.setEamID(eam);
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
