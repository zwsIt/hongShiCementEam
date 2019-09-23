package com.supcon.mes.module_yhgl.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
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
import com.supcon.mes.middleware.constant.Module;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.UserInfo;
import com.supcon.mes.middleware.model.bean.WXGDEam;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.DeviceAddEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;
import com.supcon.mes.module_yhgl.model.api.YHSubmitAPI;
import com.supcon.mes.module_yhgl.model.contract.YHSubmitContract;
import com.supcon.mes.module_yhgl.presenter.YHSubmitPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangshizhan on 2018/8/21
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.YH_VIEW)
@Presenter(YHSubmitPresenter.class)
@Controller(value = {OnlineCameraController.class, LinkController.class})
public class YHViewActivity extends BaseRefreshActivity implements YHSubmitContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;


    @BindByTag("yhViewFindTime")
    CustomVerticalDateView yhViewFindTime;

    @BindByTag("yhViewFindStaff")
    CustomVerticalTextView yhViewFindStaff;

    @BindByTag("yhViewArea")
    CustomVerticalSpinner yhViewArea;

    @BindByTag("yhViewPriority")
    CustomVerticalSpinner yhViewPriority;


    @BindByTag("yhViewEamCode")
    CustomVerticalTextView yhViewEamCode;

    @BindByTag("yhViewEamName")
    CustomVerticalTextView yhViewEamName;

    @BindByTag("yhViewEamModel")
    CustomTextView yhViewEamModel;

    @BindByTag("yhViewType")
    CustomSpinner yhViewType;

    @BindByTag("yhViewWXType")
    CustomVerticalSpinner yhViewWXType;

    @BindByTag("yhViewWXGroup")
    CustomVerticalSpinner yhViewWXGroup;

    @BindByTag("yhViewDescription")
    CustomVerticalTextView yhViewDescription;

    @BindByTag("yhGalleryView")
    CustomGalleryView yhViewGalleryView;

    @BindByTag("yhViewMemo")
    CustomVerticalEditText yhViewMemo;

    @BindByTag("yhViewCommentInput")
    CustomEditText yhViewCommentInput;

    @BindByTag("yhViewTransition")
    CustomWorkFlowView yhViewTransition;

    private YHEntity mYHEntity, mOriginalEntity;


    @Override
    protected int getLayoutID() {
        return R.layout.ac_yh_view;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshController.setAutoPullDownRefresh(false);
        refreshController.setPullDownRefreshEnabled(false);
        mYHEntity = (YHEntity) getIntent().getSerializableExtra(Constant.IntentKey.YHGL_ENTITY);
        mOriginalEntity = mYHEntity;

    }


    @Override
    protected void onRegisterController() {
        super.onRegisterController();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("隐患审核");
        yhViewFindStaff.setValue(mYHEntity.findStaffID != null ? mYHEntity.findStaffID.name : "");
        yhViewFindTime.setDate(DateUtil.dateTimeFormat(mYHEntity.findTime));
        yhViewPriority.setSpinner(mYHEntity.priority != null ? mYHEntity.priority.value : "");
        yhViewArea.setSpinner(mYHEntity.areaInstall != null ? mYHEntity.areaInstall.name : "");

        if (mYHEntity.eamID != null) {
            yhViewEamCode.setValue(mYHEntity.eamID.code);
            yhViewEamName.setValue(mYHEntity.eamID.name);
            yhViewEamModel.setValue(mYHEntity.eamID.model);
        }

        yhViewType.setSpinner(mYHEntity.faultInfoType != null ? mYHEntity.faultInfoType.value : "");
        yhViewWXType.setSpinner(mYHEntity.repairType != null ? mYHEntity.repairType.value : "");
        yhViewWXGroup.setSpinner(mYHEntity.repiarGroup != null ? mYHEntity.repiarGroup.name : "");
        if (!TextUtils.isEmpty(mYHEntity.describe)) {
            yhViewDescription.setContent(mYHEntity.describe);
        }
        getController(OnlineCameraController.class).init(Constant.IMAGE_SAVE_YHPATH, Constant.PicType.YH_PIC);
        if (mYHEntity.attachmentEntities != null) {
            getController(OnlineCameraController.class).setPicData(mYHEntity.attachmentEntities);
        }

        if (!TextUtils.isEmpty(mYHEntity.remark)) {
            yhViewMemo.setInput(mYHEntity.remark);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());


        /*yhViewGalleryView.setOnChildViewClickListener((childView, action, obj) -> {

            int position = (int) obj;

            if (position == -1) {
                return;
            }
            List<GalleryBean> galleryBeans = yhViewGalleryView.getGalleryAdapter().getList();

            if (action == CustomGalleryView.ACTION_VIEW) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", (ArrayList) FaultPicHelper.getImagePathList(galleryBeans));//非必须
                bundle.putInt("position", position);
                int[] location = new int[2];
                childView.getLocationOnScreen(location);
                bundle.putInt("locationX", location[0]);//必须
                bundle.putInt("locationY", location[1]);//必须

                bundle.putInt("width", DisplayUtil.dip2px(100, context));//必须
                bundle.putInt("height", DisplayUtil.dip2px(100, context));//必须
                bundle.putBoolean("isEditable", false);

                getWindow().setWindowAnimations(R.style.fadeStyle);
                IntentRouter.go(context, Constant.Router.IMAGE_VIEW, bundle);
            }

        });*/

        yhViewTransition.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                switch (action) {
                    case 0:
                        doSave();
                        break;
                    case 1:
                        if (checkBeforeSubmit()){
                            doSubmit((WorkFlowVar)obj);
                        }
                        break;
                    case 2:
                        if (checkBeforeSubmit()){
                            doSubmit((WorkFlowVar)obj);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

//        yhViewTransition.setOnChildViewClickListener((childView, action, obj) -> {
//            String tag = (String) childView.getTag();
//            Transition transition = yhViewTransition.currentTransition();
//
//            switch (tag) {
//
//                case Constant.Transition.SUBMIT_BTN:
//                    if(checkBeforeSubmit()) {
//                        doSubmit(transition);
//                    }
//                    break;
//                case Constant.Transition.SAVE:
//
//                    doSave();
//
//                    break;
//
//                default:
//
//                    break;
//
//            }
//
//        });

        yhViewEamCode.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.MODULE, Module.Fault.name());
                IntentRouter.go(context, Constant.Router.ADD_DEVICE, bundle);
            }
        });

        yhViewEamName.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.MODULE, Module.Fault.name());
                IntentRouter.go(context, Constant.Router.ADD_DEVICE, bundle);
            }
        });

        yhViewFindStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                IntentRouter.go(context, Constant.Router.STAFF);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addDeviceEvent(DeviceAddEvent deviceAddEvent) {

        String add = deviceAddEvent.getDeviceEntity();
        if (TextUtils.isEmpty(add)) {
            return;
        }
        List<CommonDeviceEntity> deviceEntities = GsonUtil.jsonToList(add, CommonDeviceEntity.class);
        if (deviceEntities != null && deviceEntities.size() != 0) {
            CommonDeviceEntity commonDeviceEntity = deviceEntities.get(0);
            yhViewEamName.setValue(commonDeviceEntity.eamName);
            yhViewEamCode.setValue(commonDeviceEntity.eamCode);
            yhViewEamModel.setValue(commonDeviceEntity.eamModel);
            WXGDEam wxgdEam = new WXGDEam();
            wxgdEam.name = commonDeviceEntity.eamName;
            wxgdEam.code = commonDeviceEntity.eamCode;
            wxgdEam.model = commonDeviceEntity.eamModel;
            wxgdEam.id = commonDeviceEntity.eamId;
            mYHEntity.eamID = wxgdEam;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMaintenanceStaff(CommonSearchEvent event) {
        if (event.commonSearchEntity instanceof CommonSearchStaff) {
            CommonSearchStaff searchStaff = (CommonSearchStaff) event.commonSearchEntity;
            yhViewFindStaff.setValue(searchStaff.name);
            Staff staff = new Staff();
            staff.id = searchStaff.id;
            staff.code = searchStaff.code;
            staff.name = searchStaff.name;
            mYHEntity.findStaffID = staff;
        }

    }

    @Override
    protected void initData() {
        super.initData();
        getController(LinkController.class).initPendingTransition(yhViewTransition, mYHEntity.pending.id);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setWindowAnimations(R.style.activityAnimation);
    }

    @Override
    public void onBackPressed() {

        //退出前校验表单是否修改过
        //如果修改过, 提示是否保存
        if (checkIsModified()) {
            new CustomDialog(context)
                    .twoButtonAlertDialog("页面已经被修改，是否要保存?")
                    .bindView(R.id.grayBtn, "保存")
                    .bindView(R.id.redBtn, "离开")
                    .bindClickListener(R.id.grayBtn, view ->doSave(), true)
                    .bindClickListener(R.id.redBtn, v3 -> {
                        //关闭页面
                        finish();
                    }, true)
                    .show();
        } else {
            finish();
        }

    }

    /**
     * 提交之前校验必填字段
     *
     * @return
     */
    private boolean checkBeforeSubmit() {


//        if (TextUtils.isEmpty(yhViewTransition.currentTransition().outCome)) {
//            SnackbarHelper.showError(rootView, "请选择操作！");
//            return false;
//        }

        return true;
    }

    private void doSave() {
        submit(null);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        workFlowEntity.dec = workFlowVar.dec;
        workFlowEntity.type = workFlowVar.outcomeMapJson.get(0).type;
        workFlowEntity.outcome = workFlowVar.outCome;
        workFlowEntities.add(workFlowEntity);

        submit(workFlowEntities);
    }

    private void submit(List<WorkFlowEntity> workFlowEntities) {
        WorkFlowEntity workFlowEntity = null;
        if (workFlowEntities != null && workFlowEntities.size() != 0) {
            workFlowEntity = workFlowEntities.get(0);
        } else {
            workFlowEntity = new WorkFlowEntity();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("bap_validate_user_id", String.valueOf(EamApplication.getAccountInfo().userId));
        map.put("faultInfo.createStaffId", mYHEntity.findStaffID.id);
        map.put("faultInfo.createTime", DateUtil.dateTimeFormat(mYHEntity.findTime));
        map.put("faultInfo.findStaffID.id", mYHEntity.findStaffID.id);
        map.put("faultInfo.findTime", DateUtil.dateTimeFormat(mYHEntity.findTime));
        map.put("faultInfo.createPositionId", EamApplication.getAccountInfo().positionId);


        if (mYHEntity.id != 0) {
            map.put("id", mYHEntity.id);
            map.put("faultInfo.id", mYHEntity.id);
        } else {
            map.put("id", "");
            map.put("faultInfo.id", "");
        }

        if (mYHEntity.pending != null && mYHEntity.pending.id != null) {

            map.put("pendingId", mYHEntity.pending.id);
            map.put("deploymentId", mYHEntity.pending.deploymentId);
        } else {
            map.put("deploymentId", 1040);
            map.put("faultInfo.version", 1);
        }


        if (mYHEntity.eamID != null && mYHEntity.eamID.id != null) {
            map.put("faultInfo.eamID.id", mYHEntity.eamID.id);
        } else {
            map.put("faultInfo.eamID.id", "");
        }


        if (mYHEntity.areaInstall != null && mYHEntity.areaInstall.id != 0) {
            map.put("faultInfo.areaInstall.id", mYHEntity.areaInstall.id);
        } else {
            map.put("faultInfo.areaInstall.id", "");
        }

        if (mYHEntity.repiarGroup != null && mYHEntity.repiarGroup.id != null) {
            map.put("faultInfo.repiarGroup.id", mYHEntity.repiarGroup.id);
        } else {
            map.put("faultInfo.repiarGroup.id", "");
        }

        if (mYHEntity.faultInfoType != null) {
            map.put("faultInfo.faultInfoType.id", mYHEntity.faultInfoType.id);
            map.put("faultInfo.faultInfoType.value", mYHEntity.faultInfoType.value);
        } else {
            map.put("faultInfo.faultInfoType.id", "");
            map.put("faultInfo.faultInfoType.value", "");
        }

        if (mYHEntity.priority != null) {
            map.put("faultInfo.priority.id", mYHEntity.priority.id);
        } else {
            map.put("faultInfo.priority.id", "");
        }

        if (mYHEntity.repairType != null) {
            map.put("faultInfo.repairType.id", mYHEntity.repairType.id);
        } else {
            map.put("faultInfo.repairType.id", "");
        }

        if (mYHEntity.describe != null)
            map.put("faultInfo.describe", mYHEntity.describe);
        if (mYHEntity.remark != null)
            map.put("faultInfo.remark", mYHEntity.remark);

        map.put("linkId", mYHEntity.tableInfoId != 0 ? mYHEntity.tableInfoId : "");
//        map.put("dlTableInfoId",                mYHEntity.tableInfoId);
        map.put("tableInfoId", mYHEntity.tableInfoId != 0 ? mYHEntity.tableInfoId : "");
        if (workFlowEntities != null) {//保存为空
            map.put("workFlowVar.outcomeMapJson", workFlowEntities);
            map.put("workFlowVar.dec", workFlowEntity.dec);
            map.put("workFlowVar.outcome", workFlowEntity.outcome);
            map.put("operateType", "submit");
        } else {
//            map.put("workFlowVar.dec", "");
//            map.put("workFlowVar.outcome", "");
            map.put("operateType", "save");
        }
        map.put("taskDescription", "BEAM2_1.0.0.faultInfoFW.task342");
        map.put("workFlowVar.comment", yhViewCommentInput.getInput());

        map.put("viewCode", "BEAM2_1.0.0_faultInfo_faultInfoEdit");

        map.put("modelName", "FaultInfo");
        map.put("datagridKey", "BEAM2_faultInfo_faultInfo_faultInfoEdit_datagrids");
        map.put("__file_upload", true);


        LogUtil.d(GsonUtil.gsonString(map));
        onLoading("正在提交...");
        presenterRouter.create(YHSubmitAPI.class).doSubmit(map, null, false);

    }

    /**
     * 判断是否填写过表单
     */
    private boolean checkIsModified() {
        if (!GsonUtil.gsonString(mYHEntity).equals(mOriginalEntity.toString())) {
            return true;
        }

        return !TextUtils.isEmpty(yhViewCommentInput.getInput());
    }


    @Override
    public void doSubmitSuccess(BapResultEntity entity) {
        LogUtil.d("entity:" + entity);

        RefreshEvent refreshEvent = new RefreshEvent();
        EventBus.getDefault().post(refreshEvent);

        onLoadSuccessAndExit("处理成功！", this::finish);
    }

    @Override
    public void doSubmitFailed(String errorMsg) {
        LogUtil.e("errorMsg:" + errorMsg);
        onLoadFailed(errorMsg);
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
    }
}
