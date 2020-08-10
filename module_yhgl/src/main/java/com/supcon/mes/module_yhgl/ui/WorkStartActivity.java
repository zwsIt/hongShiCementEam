package com.supcon.mes.module_yhgl.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.ModulePermissonCheckController;
import com.supcon.mes.middleware.controller.ModulePowerController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.controller.UserPowerCheckController;
import com.supcon.mes.middleware.model.api.EamAPI;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.contract.EamContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.presenter.EamPresenter;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;
import com.supcon.mes.module_yhgl.constant.YhConstant;
import com.supcon.mes.module_yhgl.model.api.WorkStartAPI;
import com.supcon.mes.module_yhgl.model.contract.WorkStartContract;
import com.supcon.mes.module_yhgl.model.dto.WorkStartDTO;
import com.supcon.mes.module_yhgl.presenter.WorkStartPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import common.Assert;
import io.reactivex.functions.Consumer;

@Router(value = Constant.Router.WORK_START_EDIT)
@Presenter(value = {WorkStartPresenter.class, EamPresenter.class})
@Controller(value = {UserPowerCheckController.class, OnlineCameraController.class})
public class WorkStartActivity extends BaseControllerActivity implements WorkStartContract.View, EamContract.View {

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("workStartStaff")
    CustomTextView workStartStaff;
    @BindByTag("eamCode")
    CustomTextView eamCode;
    @BindByTag("eamName")
    CustomTextView eamName;
    @BindByTag("workContactStaff")
    CustomTextView workContactStaff;
    @BindByTag("workEndTime")
    CustomDateView workEndTime;
    @BindByTag("workPriority")
    CustomSpinner workPriority;
    @BindByTag("workContent")
    CustomVerticalEditText workContent;
    @BindByTag("galleryView")
    CustomGalleryView galleryView;
    @BindByTag("submitBtn")
    Button submitBtn;

    private NFCHelper nfcHelper;
    private WorkStartDTO mWorkStartDTO;
    private String mWorkStartDTOInit;
    private DatePickController mDatePickController;
    private SinglePickController mSinglePickController;
    private List<SystemCodeEntity> mPriority;  // 优先级
    private List<String> mPriorityList = new ArrayList<>();
    private boolean addPermission; // 添加权限（使用隐患单多页签添加按钮）

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        nfcHelper = NFCHelper.getInstance();
        if (nfcHelper != null) {
            nfcHelper.setup(this);
            nfcHelper.setOnNFCListener(new NFCHelper.OnNFCListener() {
                @Override
                public void onNFCReceived(String nfc) {
                    EventBus.getDefault().post(new NFCEvent(nfc, context.getClass().getName()));
                }
            });
        }

        mWorkStartDTO = new WorkStartDTO();
        mWorkStartDTO.setInitStaffId(EamApplication.getAccountInfo().staffId);
        mWorkStartDTO.setPriority("BEAM2007/001");
        workPriority.setContent("普通");
        mWorkStartDTOInit = GsonUtil.gsonString(mWorkStartDTO);

        mDatePickController = new DatePickController(this);
        mDatePickController.textSize(18);
        mDatePickController.setCanceledOnTouchOutside(true);
        mDatePickController.setCycleDisable(false);
        mDatePickController.setSecondVisible(false);

        mSinglePickController = new SinglePickController<String>(this);
        mSinglePickController.textSize(18);
        mSinglePickController.setCanceledOnTouchOutside(true);

        getController(OnlineCameraController.class).init(Constant.IMAGE_SAVE_YHPATH,Constant.PicType.YH_PIC);
        getController(OnlineCameraController.class).addGalleryView(0,galleryView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcHelper != null)
            nfcHelper.onPauseNFC(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取到Tag对象
        if (nfcHelper != null)
            nfcHelper.dealNFCTag(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fault_ac_work_start;
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText(getResources().getString(R.string.fault_work_start_edit));
        workStartStaff.setContent(EamApplication.getAccountInfo().staffName);

        getController(UserPowerCheckController.class).checkModulePermission(EamApplication.getCid(), YhConstant.OperateCode.CUSTOM_ADD, new OnSuccessListener<Map<String, Boolean>>() {
            @Override
            public void onSuccess(Map<String, Boolean> result) {
                addPermission = result.get(YhConstant.OperateCode.CUSTOM_ADD);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPriority = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_PRIORITY);
        for (SystemCodeEntity systemCodeEntity : mPriority){
            mPriorityList.add(systemCodeEntity.value);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        workStartStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1){
                mWorkStartDTO.setInitStaffId(null);
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
            bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, workStartStaff.getTag().toString());
            IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
        });
        eamCode.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1){
                mWorkStartDTO.setEamId(null);
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
            bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, eamCode.getTag().toString());
            bundle.putBoolean(Constant.IntentKey.IS_SELECT,true);
            IntentRouter.go(context, Constant.Router.EAM_TREE_SELECT, bundle);
//            IntentRouter.go(context, Constant.Router.EAM, bundle);
        });
        workContactStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1){
                mWorkStartDTO.setWorkContactStaffId(null);
            }else {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, workContactStaff.getTag().toString());
                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
            }
        });

        workEndTime.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1){
                mWorkStartDTO.setPlanFinishTime(null);
            }else {
                mDatePickController.listener((year, month, day, hour, minute, second) -> {
                    String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                    workEndTime.setContent(dateStr);
                    mWorkStartDTO.setPlanFinishTime(DateUtil.dateFormat(dateStr,Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN));
                }).show(mWorkStartDTO.getPlanFinishTime() == null ? new Date().getTime() : mWorkStartDTO.getPlanFinishTime());
            }
        });
        workPriority.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1){
                mWorkStartDTO.setPriority(null);
            }
            if (mPriority.size() <= 0) {
                ToastUtils.show(context, "优先级列表数据为空,请退出重新加载页面！");
                return;
            }
            mSinglePickController.list(mPriorityList)
                    .listener((index, item) -> {
                        workPriority.setContent(String.valueOf(item));
                        mWorkStartDTO.setPriority(mPriority.get(index).id);
                    }).show(workPriority.getContent());
        });

        RxTextView.textChanges(workContent.editText())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        mWorkStartDTO.setContent(charSequence.toString());
                    }
                });
        RxView.clicks(submitBtn).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (!addPermission){
                            ToastUtils.show(context, context.getResources().getString(R.string.sorry_no_add_permission));
                            return;
                        }
                        doSubmit();
                    }
                });
    }

    private void doSubmit() {
        if (checkResult()){
            return;
        }
        onLoading(context.getResources().getString(R.string.dealing));

        // 照片
        if (galleryView.getGalleryAdapter() != null && galleryView.getGalleryAdapter().getItemCount() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (GalleryBean galleryBean : galleryView.getGalleryAdapter().getList()){
                stringBuilder.append(galleryBean.url).append(",");
            }
            mWorkStartDTO.setImgServerUrl(stringBuilder.substring(0,stringBuilder.length()-1));
        }

        presenterRouter.create(WorkStartAPI.class).workStartSubmit(GsonUtil.gsonString(mWorkStartDTO));
    }
    private boolean checkResult() {
        if (mWorkStartDTO.getInitStaffId() == null) {
            ToastUtils.show(context, "请填写发起人");
            return true;
        }
        if (mWorkStartDTO.getEamId() == null) {
            ToastUtils.show(context, "请填写设备信息");
            return true;
        }
        if (TextUtils.isEmpty(mWorkStartDTO.getPriority())) {
            ToastUtils.show(context, "请填写优先级");
            return true;
        }
        if (TextUtils.isEmpty(mWorkStartDTO.getContent())) {
            ToastUtils.show(context, "请填写工作内容");
            return true;
        }
        return false;
    }


    /**
     * 全屏展示时监听图片删除
     *
     * @param imageDeleteEvent
     */
    @Subscribe
    public void onReceiveImageDeleteEvent(ImageDeleteEvent imageDeleteEvent) {
        getController(OnlineCameraController.class).deleteGalleryBean(galleryView.getGalleryAdapter().getList().get(imageDeleteEvent.getPos()), imageDeleteEvent.getPos());
        EventBus.getDefault().post(new RefreshEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEntity(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff) {
            CommonSearchStaff staff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;
            if (workStartStaff.getTag().toString().equals(commonSearchEvent.flag)) {
                workStartStaff.setContent(staff.name);
                mWorkStartDTO.setInitStaffId(staff.id);
            } else if (workContactStaff.getTag().toString().equals(commonSearchEvent.flag)) {
                workContactStaff.setContent(staff.name);
                mWorkStartDTO.setWorkContactStaffId(staff.id);
            }
        } else if (commonSearchEvent.commonSearchEntity instanceof EamEntity) {
            EamEntity eamEntity = (EamEntity) commonSearchEvent.commonSearchEntity;
            eamName.setContent(eamEntity.name);
            eamCode.setContent(eamEntity.eamAssetCode);
            mWorkStartDTO.setEamId(eamEntity.id);
        }
    }

    /**
     * @param
     * @description NFC事件
     * @author zhangwenshuai1
     * @date 2018/6/28
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCEvent nfcEvent) {
        if (!TextUtils.isEmpty(nfcEvent.getTag()) && nfcEvent.getTag().equals(context.getClass().getName())) {
            Map<String, Object> nfcJson = Util.gsonToMaps(nfcEvent.getNfc());
            if (nfcJson.get("textRecord") == null) {
                ToastUtils.show(context, "标签内容空！");
                return;
            }
            Map<String, Object> params = new HashMap<>();
            params.put(Constant.IntentKey.EAM_CODE, nfcJson.get("textRecord"));
            presenterRouter.create(EamAPI.class).getEam(params, true,1,20);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mWorkStartDTOInit.equals(GsonUtil.gsonString(mWorkStartDTO))){
            new CustomDialog(this).twoButtonAlertDialog("页面内容已修改，是否提交？")
                    .bindView(R.id.redBtn,context.getResources().getString(R.string.submit))
                    .bindClickListener(R.id.grayBtn, v -> finish(), true)
                    .bindClickListener(R.id.redBtn, v -> doSubmit(), true)
                    .show();
        }else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (nfcHelper != null) {
            nfcHelper.release();
        }
    }

    @Override
    public void workStartSubmitSuccess(CommonEntity entity) {
        onLoadSuccess(getResources().getString(R.string.deal_success));
        finish();
    }

    @Override
    public void workStartSubmitFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void getEamSuccess(CommonListEntity entity) {
        if (entity.result.size() == 0){
            ToastUtils.show(context,"未查询到当前设备信息");
            return;
        }
        EamEntity eamEntity = (EamEntity) entity.result.get(0);
        eamName.setContent(eamEntity.name);
        eamCode.setContent(eamEntity.eamAssetCode);
        mWorkStartDTO.setEamId(eamEntity.id);

    }

    @Override
    public void getEamFailed(String errorMsg) {
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
    }
}
