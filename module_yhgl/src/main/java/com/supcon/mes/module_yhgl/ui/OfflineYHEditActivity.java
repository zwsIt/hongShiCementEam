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
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.Module;
import com.supcon.mes.middleware.controller.ModifyController;
import com.supcon.mes.middleware.controller.OfflineCameraController;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.AreaDao;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.RepairGroupEntity;
import com.supcon.mes.middleware.model.bean.RepairGroupEntityDao;
import com.supcon.mes.middleware.model.bean.SingleConnectWithDecorator;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.YHEntityVo;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;
import com.supcon.mes.module_yhgl.model.api.OfflineYHSubmitAPI;
import com.supcon.mes.module_yhgl.model.contract.OfflineYHSubmitContract;
import com.supcon.mes.module_yhgl.presenter.OfflineYHSubmitPresenter;
import com.supcon.mes.module_yhgl.util.FieldHepler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.supcon.mes.module_yhgl.util.SystemCodeUtil.getSystemCodeValue;

/**
 * Created by xushiyun on 2018/8/21
 * Email:ciruy.victory@gmail.com
 *
 * @author xushiyun
 */
@Router(Constant.Router.OFFLINE_YH_EDIT)
@Presenter(OfflineYHSubmitPresenter.class)
@Controller(OfflineCameraController.class)
public class OfflineYHEditActivity extends BaseRefreshActivity implements OfflineYHSubmitContract.View, SingleConnectWithDecorator {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    /**
     * 发现时间
     */
    @BindByTag("yhEditFindTime")
    CustomVerticalDateView yhEditFindTime;
    /**
     * 发现人
     */
    @BindByTag("yhEditFindStaff")
    CustomVerticalTextView yhEditFindStaff;
    /**
     * 区域位置
     */
    @BindByTag("yhEditArea")
    CustomVerticalSpinner yhEditArea;
    /**
     * 优先级
     */
    @BindByTag("yhEditPriority")
    CustomVerticalSpinner yhEditPriority;
    /**
     * 设备编码
     */
    @BindByTag("yhEditEamCode")
    CustomVerticalTextView yhEditEamCode;
    /**
     * 设备名称
     */
    @BindByTag("yhEditEamName")
    CustomVerticalTextView yhEditEamName;
    /**
     * 规格型号
     */
    @BindByTag("yhEditEamModel")
    CustomTextView yhEditEamModel;
    /**
     * 隐患类型
     */
    @BindByTag("yhEditType")
    CustomSpinner yhEditType;
    /**
     * 维修类型
     */
    @BindByTag("yhEditWXType")
    CustomVerticalSpinner yhEditWXType;
    /**
     * 维修组
     */
    @BindByTag("yhEditWXGroup")
    CustomVerticalSpinner yhEditWXGroup;
    /**
     * 隐患现象
     */
    @BindByTag("yhEditDescription")
    CustomVerticalEditText yhEditDescription;
    /**
     * 隐患照片
     */
    @BindByTag("yhGalleryView")
    CustomGalleryView yhGalleryView;
    /**
     * 填写备注
     */
    @BindByTag("yhEditMemo")
    CustomVerticalEditText yhEditMemo;
    /**
     * 填写意见
     */
    @BindByTag("yhEditCommentInput")
    CustomEditText yhEditCommentInput;

    /**
     * 转至保存
     */
    @BindByTag("leftBtn_2")
    TextView leftBtn_2;

    /**
     * 转至提交
     */
    @BindByTag("rightBtn_2")
    TextView rightBtn_2;

    @BindByTag("rightBtn")
    CustomImageButton rightBtn;

    /**
     * 界面修改的数据信息,仅此一个
     */
    private YHEntityVo mYHEntityVo;
    private ModifyController<YHEntityVo> mModifyController;

    private SinglePickController<String> mSinglePickController;
    private DatePickController mDatePickController;

    private Map<String, SystemCodeEntity> wxTypes;
    private Map<String, SystemCodeEntity> yhTypes;
    private Map<String, SystemCodeEntity> yhPriorities;
    private Map<String, Area> mAreas;
    private Map<String, RepairGroupEntity> mRepairGroups;

//    private OfflineGalleryDecorator galleryDecorator;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_yh_edit_offline;
    }

    @Override
    protected void initData() {
        super.initData();
        List<SystemCodeEntity> wxTypeEntities = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_WX_TYPE);
        wxTypes = initEntities(wxTypeEntities);

        List<SystemCodeEntity> yhTypeEntities = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.QX_TYPE);
        yhTypes = initEntities(yhTypeEntities);

        List<SystemCodeEntity> yhPriorityEntity = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_PRIORITY);
        yhPriorities = initEntities(yhPriorityEntity);

        List<Area> areaEntities = EamApplication.dao().getAreaDao().queryBuilder().where(AreaDao.Properties.Ip.eq(MBapApp.getIp())).list();
        mAreas = initEntities(areaEntities);

        List<RepairGroupEntity> repairGroupEntities =
                EamApplication.dao().getRepairGroupEntityDao().queryBuilder().where(RepairGroupEntityDao.Properties.IsUse.eq(Boolean.valueOf(true)), RepairGroupEntityDao.Properties.Ip.eq(EamApplication.getIp())).list();
        mRepairGroups = initEntities(repairGroupEntities);

        initViewValues();
    }

    private void initViewValues() {
        // 优先设置可编辑性，否则赋值后，再设置不可编辑删除图标仍会显示
        if (null != mYHEntityVo.getEamId()) {
            yhEditArea.setEditable(false);
        }
        if (Constant.YHWXType.JX_SYSCODE.equals(mYHEntityVo.getRepairType()) || Constant.YHWXType.DX_SYSCODE.equals(mYHEntityVo.getRepairType())) {
            yhEditWXGroup.setEditable(false);
        }
        yhEditFindTime.setDate(mYHEntityVo.getFindDate());
        yhEditFindStaff.setValue(mYHEntityVo.getFindStaffName());
        yhEditArea.setSpinner(mYHEntityVo.getAreaInstallName());
        yhEditPriority.setSpinner(getSystemCodeValue(mYHEntityVo.getPriority()));
        final CommonDeviceEntity commonDeviceEntity = mYHEntityVo.getEamId() == null ? null : DeviceManager.getInstance().getDeviceEntityByEamId(mYHEntityVo.getEamId());
        if (null != commonDeviceEntity) {
            yhEditEamCode.setValue(commonDeviceEntity.eamCode);
            yhEditEamModel.setValue(commonDeviceEntity.eamModel);
        }

        yhEditEamName.setValue(mYHEntityVo.getEamName());
        yhEditType.setSpinner(getSystemCodeValue(mYHEntityVo.getFaultType()));
        yhEditWXType.setSpinner(getSystemCodeValue(mYHEntityVo.getRepairType()));

        yhEditWXGroup.setSpinner(mYHEntityVo.getRepairGroupName());
        yhEditDescription.setInput(mYHEntityVo.getDescription());
        yhEditMemo.setInput(mYHEntityVo.getRemark());
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

    @Override
    protected void onInit() {
        super.onInit();
        //初始化EventBus
        EventBus.getDefault().register(this);
        //初始化时获取传入的entity信息
        mYHEntityVo = (YHEntityVo) getIntent().getSerializableExtra(Constant.IntentKey.YHGL_ENTITY);
        mYHEntityVo = null == mYHEntityVo ? new YHEntityVo() : mYHEntityVo;
        initController();
    }

    /**
     * 摧毁活动的同时,注销EventBus
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        galleryDecorator.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onRegisterController() {
        super.onRegisterController();
//        RoleController roleController = new RoleController();
//        registerController(Constant.Controller.ROLE, roleController);
//        roleController.queryRoleList(EamApplication.getUserName());
    }

    /**
     * 初始化数据,考虑到这里是直接使用了本地的数据所以初始化数据只需要在initData中进行实现就行了
     */
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("隐患编辑");

        initGalleryDecoration();
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.drawable.ic_delete_new);
    }

    /**
     * 进行照片处理器的初始化操作，感觉真的，在这里我对于解耦的理解还完全不够呢
     * 归根结底，两者进行联系的方法就这两个，都执行的是初始化操作
     */
    private void initGalleryDecoration() {
//        galleryDecorator = new OfflineGalleryDecorator(this);
//        galleryDecorator.setPicPaths(mYHEntityVo.getLocalPicPaths());
        getController(OfflineCameraController.class).setPicPaths(mYHEntityVo.getLocalPicPaths());
        getController(OfflineCameraController.class).init(Constant.IMAGE_SAVE_YHPATH, OfflineYHEditActivity.class.getSimpleName());
    }

    /**
     * 初始化监听器
     */
    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        initBackButton();
        initSinglePickListeners();
        initDatePickListeners();
        initEditTextChangeListeners();
        initCommonSearchListeners();
        initCommitButtons();
        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> new CustomDialog(context)
                        .twoButtonAlertDialog("是否要删除当前表单?")
                        .bindView(R.id.grayBtn, "取消")
                        .bindView(R.id.redBtn, "删除")
                        .bindClickListener(R.id.grayBtn, null, true)
                        .bindClickListener(R.id.redBtn, v3 -> delete(), true)
                        .show());
//        galleryDecorator.onListener();
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        galleryDecorator.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @SuppressLint("CheckResult")
    private void initCommitButtons() {
        RxView.clicks(leftBtn_2)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    mYHEntityVo.setStatus(Boolean.FALSE);
                    modify();
                });
        RxView.clicks(rightBtn_2)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    mYHEntityVo.setStatus(Boolean.TRUE);
                    if (checkNecessaryItems()) {
                        modify();
                    }
                });
    }

    @Subscribe
    public void onReceiveImageDeleteEvent(ImageDeleteEvent imageDeleteEvent) {
        getController(OfflineCameraController.class).deleteGalleryBean(yhGalleryView.getGalleryAdapter().getList().get(imageDeleteEvent.getPos()), imageDeleteEvent.getPos());
//        yhGalleryView.deletePic(imageDeleteEvent.getPos());
    }

    /**
     * 点击返回按钮返回到上级界面
     */
    @SuppressLint("CheckResult")
    private void initBackButton() {
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
    }

    private void initCommonSearchListeners() {
        //人员搜索则跳转到人员搜索通用组件
        yhEditFindStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (-1 == action) {
                mYHEntityVo.setFindStaffId(null);
                mYHEntityVo.setFindStaffName(null);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(Constant.IntentKey.COMMON_SAERCH_MODE, Constant.CommonSearchMode.STAFF);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, getTag(yhEditFindStaff));
            IntentRouter.go(context, Constant.Router.COMMON_SEARCH, bundle);
        });
        yhEditEamCode.setOnChildViewClickListener((childView, action, obj) -> {
            if (-1 == action) {
                mYHEntityVo.setEamId(null);
                mYHEntityVo.setAreaInstallName(null);
                mYHEntityVo.setAreaInstallId(null);
                mYHEntityVo.setEamName(null);
                mYHEntityVo.setEamModel(null);
                yhEditArea.setSpinner(null);
                yhEditEamName.setValue(null);
                yhEditEamModel.setValue(null);

                yhEditArea.setEditable(true);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(Constant.IntentKey.COMMON_SAERCH_MODE, Constant.CommonSearchMode.EAM);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, getTag(yhEditEamCode));
            bundle.putString(Constant.IntentKey.ENTITY_CODE, yhEditArea.getSpinnerValue());
            bundle.putString(Constant.IntentKey.MODULE, Module.Fault.name());
            IntentRouter.go(context, Constant.Router.COMMON_SEARCH, bundle);
        });
    }

    @Subscribe
    public void onReceiveCommonEntity(CommonSearchEvent commonSearchEvent) {
        LogUtil.e("id:" + commonSearchEvent.commonSearchEntity.getSearchId() + "\n"
                + "name:" + commonSearchEvent.commonSearchEntity.getSearchName());
        switch (commonSearchEvent.flag) {
            case "yhEditFindStaff":
                mYHEntityVo.setFindStaffId(
                        Long.valueOf(commonSearchEvent.commonSearchEntity.getSearchId()));
                yhEditFindStaff.setValue(commonSearchEvent.commonSearchEntity.getSearchName());
                mYHEntityVo.setFindStaffName(commonSearchEvent.commonSearchEntity.getSearchName());
                break;
            case "yhEditEamCode":
                CommonDeviceEntity commonDeviceEntity = (CommonDeviceEntity) commonSearchEvent.commonSearchEntity;
                if (commonDeviceEntity != null) {
                    yhEditEamName.setValue(commonDeviceEntity.eamName);
                    yhEditEamCode.setValue(commonDeviceEntity.eamCode);
                    yhEditEamModel.setValue(commonDeviceEntity.eamModel);

                    //处理区域位置
                    yhEditArea.setEditable(false);
                    yhEditArea.setSpinner(commonDeviceEntity.installPlace);
                    mYHEntityVo.setAreaInstallName(commonDeviceEntity.installPlace);
                    mYHEntityVo.setAreaInstallId(mAreas.get(commonDeviceEntity.installPlace).id);
                    mYHEntityVo.setEamId(commonDeviceEntity.eamId);
                    mYHEntityVo.setEamName(commonDeviceEntity.eamName);
                    mYHEntityVo.setEamModel(commonDeviceEntity.eamModel);
                }

                break;
            default:
        }
    }

    /**
     * 获取tag字符串用于辨别各个组件的区别
     *
     * @param view 视图
     * @return tag
     */
    private String getTag(View view) {
        return view.getTag().toString();
    }


    /**
     * 初始化文本监听器
     */
    @SuppressLint("CheckResult")
    private void initEditTextChangeListeners() {
        RxTextView.textChanges(yhEditDescription.editText())
                .skipInitialValue()
                .subscribe(charSequence ->
                        mYHEntityVo.setDescription(TextUtils.isEmpty(charSequence.toString().trim()) ?
                                null : charSequence.toString()));
        RxTextView.textChanges(yhEditMemo.editText())
                .skipInitialValue()
                .subscribe(charSequence ->
                        mYHEntityVo.setRemark(TextUtils.isEmpty(charSequence) ?
                                null : charSequence.toString()));
        RxTextView.textChanges(yhEditCommentInput.editText())
                .skipInitialValue()
                .subscribe(charSequence ->
                        mYHEntityVo.setOutCome(TextUtils.isEmpty(charSequence) ?
                                null : charSequence.toString()));
    }


    /**
     * 初始化日期选择监听器
     */
    private void initDatePickListeners() {
        yhEditFindTime.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntityVo.setFindDate(null);
                return;
            }
            mDatePickController
                    .listener((year, month, day, hour, minute, second) -> {
                        LogUtil.e(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
                        String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        mYHEntityVo.setFindDate(dateStr);
                        yhEditFindTime.setDate(dateStr);
                    })
                    .show(null == mYHEntityVo.getFindDate() ?
                            System.currentTimeMillis() :
                            DateUtil.dateFormat(mYHEntityVo.getFindDate(), "yyyy-MM-dd HH:mm:ss"));
        });
    }

    /**
     * 初始化单选监听器
     */
    @SuppressWarnings("unchecked")
    private void initSinglePickListeners() {
        yhEditPriority.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntityVo.setPriority(null);
            } else {
                List list = new ArrayList<>(yhPriorities.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "优先级列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            SystemCodeEntity yhPriority = yhPriorities.get(item);
                            mYHEntityVo.setPriority(yhPriority.id);
                            yhEditPriority.setSpinner(item);
                        })
                        .show(yhEditPriority.getSpinnerValue());
            }
        });
        yhEditArea.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntityVo.setAreaInstallId(null);
            } else {
                List list = new ArrayList<>(mAreas.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "区域位置列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            Area area = mAreas.get(item);
                            mYHEntityVo.setAreaInstallId(area.id);
                            mYHEntityVo.setAreaInstallName(area.name);
                            yhEditArea.setSpinner(item);
                        })
                        .show(yhEditArea.getSpinnerValue());
            }
        });
        yhEditType.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntityVo.setFaultType(null);
            } else {
                List list = new ArrayList<>(yhTypes.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "隐患类型列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            SystemCodeEntity yhType = yhTypes.get(item);
                            mYHEntityVo.setFaultType(yhType.id);
                            yhEditType.setSpinner(item);
                        })
                        .show(yhEditType.getSpinnerValue());
            }
        });
        yhEditWXType.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntityVo.setRepairType(null);
                yhEditWXGroup.setEditable(true);
            } else {
                List list = new ArrayList<>(wxTypes.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "维修类型列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            SystemCodeEntity wxType = wxTypes.get(item);
                            mYHEntityVo.setRepairType(wxType.id);
                            yhEditWXType.setSpinner(item);

                            //大修或检修时，维修组不可编辑，若有值清空
                            if (Constant.YHWXType.DX.equals(item) || Constant.YHWXType.JX.equals(item)) {
                                yhEditWXGroup.setEditable(false);
                                yhEditWXGroup.setSpinner(null);
                                mYHEntityVo.setRepairGroupName(null);
                                mYHEntityVo.setRepiarGroupId(null);
                            } else {
                                yhEditWXGroup.setEditable(true);
                            }
                        })
                        .show(yhEditWXType.getSpinnerValue());
            }
        });
        yhEditWXGroup.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntityVo.setRepiarGroupId(null);
                mYHEntityVo.setRepairGroupName(null);
            } else {
                List list = new ArrayList<>(mRepairGroups.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "维修组列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {

                            RepairGroupEntity repairGroupEntity = mRepairGroups.get(item);
                            mYHEntityVo.setRepiarGroupId(repairGroupEntity.id);
                            yhEditWXGroup.setSpinner(item);
                            mYHEntityVo.setRepairGroupName(repairGroupEntity.name);
                        })
                        .show(yhEditWXGroup.getSpinnerValue());
            }
        });
    }


    /**
     * 退出前校验表单是否修改过
     * 如果修改过, 提示是否保存
     */
    @Override
    public void onBackPressed() {
        if (checkImageModified() || checkIsModified()) {
            new CustomDialog(context)
                    .twoButtonAlertDialog("页面已经被修改，是否要保存?")
                    .bindView(R.id.grayBtn, "保存")
                    .bindView(R.id.redBtn, "离开")
                    .bindClickListener(R.id.grayBtn, v -> {
                        mYHEntityVo.setStatus(Boolean.FALSE);
                        modify();
                    }, true)
                    .bindClickListener(R.id.redBtn, v3 -> back(), true)
                    .show();
        } else {
            back();
        }
    }

    /**
     * 保存数据信息
     */
    private void modify() {
        updateImageField();
        presenterRouter.create(OfflineYHSubmitAPI.class).doSubmit(mYHEntityVo);
    }

    private void updateImageField() {
//        mYHEntityVo.setFaultPicPaths(galleryDecorator.getFileNames());
//        mYHEntityVo.setLocalPicPaths(galleryDecorator.getLocalPaths());
//        mYHEntityVo.setLocalImgNames(galleryDecorator.getFileNames());

        mYHEntityVo.setFaultPicPaths(getController(OfflineCameraController.class).getFileNames());
        mYHEntityVo.setLocalPicPaths(getController(OfflineCameraController.class).getLocalPaths());
        mYHEntityVo.setLocalImgNames(getController(OfflineCameraController.class).getFileNames());
    }

    private boolean checkImageModified() {
//        return !(checkEmptyStringEquals(mYHEntityVo.getFaultPicPaths(), galleryDecorator.getFileNames())
//                && checkEmptyStringEquals(mYHEntityVo.getLocalPicPaths(), galleryDecorator.getLocalPaths())
//                && checkEmptyStringEquals(mYHEntityVo.getLocalImgNames(), galleryDecorator.getFileNames()));

        return !(checkEmptyStringEquals(mYHEntityVo.getFaultPicPaths(), getController(OfflineCameraController.class).getFileNames())
                && checkEmptyStringEquals(mYHEntityVo.getLocalPicPaths(), getController(OfflineCameraController.class).getLocalPaths())
                && checkEmptyStringEquals(mYHEntityVo.getLocalImgNames(), getController(OfflineCameraController.class).getFileNames()));

    }

    private boolean checkEmptyStringEquals(String arg1, String arg2) {
        if (TextUtils.isEmpty(arg1) && TextUtils.isEmpty(arg2)) return true;
        if (null == arg1) return false;
        if (null == arg2) return false;
        return arg1.equals(arg2);
    }


    /**
     * 删除数据信息
     */
    private void delete() {
        presenterRouter.create(OfflineYHSubmitAPI.class).doDelete(mYHEntityVo);
    }

    /**
     * 提交之前校验必填字段,需要按照顺序依次暗转
     *
     * @return 检测结果
     */
    private boolean checkNecessaryItems() {
        if (null == mYHEntityVo.getFindStaffId()) {
            SnackbarHelper.showError(rootView, "请选择发现人！");
            return false;
        }
        if (null == mYHEntityVo.getFindDate()) {
            SnackbarHelper.showError(rootView, "请选择发现时间！");
            return false;
        }
        if (null == mYHEntityVo.getPriority()) {
            SnackbarHelper.showError(rootView, "请选择优先级！");
            return false;
        }

//        if (null == mYHEntityVo.getAreaInstallId()) {
//            SnackbarHelper.showError(rootView, "请选择区域位置！");
//            return false;
//        }

        if (null == mYHEntityVo.getEamId()) {
            SnackbarHelper.showError(rootView, "请选择设备编码！");
            return false;
        }

        if (null == mYHEntityVo.getFaultType()) {
            SnackbarHelper.showError(rootView, "请选择隐患类型！");
            return false;
        }

        if (TextUtils.isEmpty(mYHEntityVo.getDescription())) {
            SnackbarHelper.showError(rootView, "请填写隐患现象！");
            return false;
        }

        if (null == mYHEntityVo.getRepairType()) {
            SnackbarHelper.showError(rootView, "请选择维修类型！");
            return false;
        }

        return true;
    }


    /**
     * 判断是否填写过表单
     */
    private boolean checkIsModified() {
        return mModifyController.isModifyed(mYHEntityVo);
    }

    /**
     * 如果提交变量成功,则需要实现列表的刷新操作
     *
     * @param entity 操作成功返回entity操作信息
     */
    @Override
    public void doSubmitSuccess(BapResultEntity entity) {
        EventBus.getDefault().post(new RefreshEvent());
        back();
    }

    @Override
    public void doSubmitFailed(String errorMsg) {
        onLoadFailed(errorMsg);
    }

    @Override
    public void doDeleteSuccess() {
        EventBus.getDefault().post(new RefreshEvent());
        back();
    }

    @Override
    public void doDeleteFailed(String errorMsg) {
        ToastUtils.show(context, "删除数据失败!");
    }

    /**
     * 初始化众多控制器,其中执行各种控制器的初始化操作
     */
    private void initController() {
        initRefreshController();
        initSinglePickController();
        initDatePickController();
        initModifyController();
    }

    /**
     * 初始化检验变量是否修改的控制器,将进入activity所传输的entity
     * 信息作为初始变量用以核对后续变量是否变化
     */
    private void initModifyController() {
        mModifyController = new ModifyController<>(mYHEntityVo);
    }

    /**
     * 初始化列表刷新控制器
     */
    private void initRefreshController() {
        refreshController.setAutoPullDownRefresh(false);
        refreshController.setPullDownRefreshEnabled(false);
    }

    /**
     * 初始化列表弹出日期选择框控制器
     */
    private void initDatePickController() {
        mDatePickController = new DatePickController(this);
        mDatePickController.textSize(18);
        mDatePickController.setCycleDisable(false);
        mDatePickController.setSecondVisible(true);
        mDatePickController.setCanceledOnTouchOutside(true);
    }

    /**
     * 初始化列表弹出单选框控制器
     */
    private void initSinglePickController() {
        mSinglePickController = new SinglePickController<>(this);
        mSinglePickController.textSize(18);
        mSinglePickController.setCanceledOnTouchOutside(true);
    }

    @Override
    public CustomGalleryView customGalleryView() {
        return yhGalleryView;
    }

    @Override
    public String startName() {
        return this.getClass().getSimpleName();
    }
}
