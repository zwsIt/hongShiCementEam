package com.supcon.mes.module_yhgl.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.AreaDao;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.RepairGroupEntity;
import com.supcon.mes.middleware.model.bean.RepairGroupEntityDao;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntityDao;
import com.supcon.mes.middleware.model.bean.WXGDEam;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;
import com.supcon.mes.module_yhgl.controller.LubricateOilsController;
import com.supcon.mes.module_yhgl.controller.MaintenanceController;
import com.supcon.mes.module_yhgl.controller.RepairStaffController;
import com.supcon.mes.module_yhgl.controller.SparePartController;
import com.supcon.mes.module_yhgl.model.api.YHListAPI;
import com.supcon.mes.module_yhgl.model.api.YHSubmitAPI;
import com.supcon.mes.module_yhgl.model.bean.YHListEntity;
import com.supcon.mes.module_yhgl.model.contract.YHListContract;
import com.supcon.mes.module_yhgl.model.contract.YHSubmitContract;
import com.supcon.mes.module_yhgl.model.dto.AcceptanceCheckEntityDto;
import com.supcon.mes.module_yhgl.model.dto.LubricateOilsEntityDto;
import com.supcon.mes.module_yhgl.model.dto.MaintainDto;
import com.supcon.mes.module_yhgl.model.dto.RepairStaffDto;
import com.supcon.mes.module_yhgl.model.dto.SparePartEntityDto;
import com.supcon.mes.module_yhgl.model.event.ListEvent;
import com.supcon.mes.module_yhgl.model.event.LubricateOilsEvent;
import com.supcon.mes.module_yhgl.model.event.MaintenanceEvent;
import com.supcon.mes.module_yhgl.model.event.RepairStaffEvent;
import com.supcon.mes.module_yhgl.model.event.SparePartEvent;
import com.supcon.mes.module_yhgl.presenter.YHListPresenter;
import com.supcon.mes.module_yhgl.presenter.YHSubmitPresenter;
import com.supcon.mes.module_yhgl.util.FieldHepler;
import com.supcon.mes.module_yhgl.util.YHGLMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Predicate;

import static com.supcon.mes.middleware.constant.Constant.IntentKey.DEPLOYMENT_ID;

/**
 * Created by wangshizhan on 2018/8/21
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.YH_EDIT)
@Presenter(value = {YHSubmitPresenter.class, YHListPresenter.class})
@Controller(value = {SparePartController.class, LubricateOilsController.class, RepairStaffController.class, MaintenanceController.class, OnlineCameraController.class})
public class YHEditActivity extends BaseRefreshActivity implements YHSubmitContract.View, YHListContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("yhEditFindTime")
    CustomDateView yhEditFindTime;

    @BindByTag("yhEditFindStaff")
    CustomTextView yhEditFindStaff;

    @BindByTag("yhEditArea")
    CustomSpinner yhEditArea;

    @BindByTag("yhEditPriority")
    CustomSpinner yhEditPriority;

    @BindByTag("yhEditEamCode")
    CustomTextView yhEditEamCode;

    @BindByTag("yhEditEamName")
    CustomTextView yhEditEamName;

//    @BindByTag("yhEditEamModel")
//    CustomTextView yhEditEamModel;

    @BindByTag("yhEditType")
    CustomSpinner yhEditType;

    @BindByTag("yhEditWXType")
    CustomSpinner yhEditWXType;

    @BindByTag("yhEditWXChargeGroup")
    CustomVerticalTextView yhEditWXChargeGroup;

    @BindByTag("yhEditWXChargeStaff")
    CustomVerticalTextView yhEditWXChargeStaff;

    @BindByTag("yhEditDescription")
    CustomVerticalEditText yhEditDescription;

    @BindByTag("yhGalleryView")
    CustomGalleryView yhGalleryView;

    @BindByTag("yhEditMemo")
    CustomVerticalEditText yhEditMemo;

    @BindByTag("acceptChkStaff")
    CustomVerticalTextView acceptChkStaff;
    @BindByTag("acceptChkStaffCode")
    CustomVerticalTextView acceptChkStaffCode;
    @BindByTag("acceptChkTime")
    CustomVerticalDateView acceptChkTime;
    @BindByTag("acceptChkResult")
    CustomVerticalSpinner acceptChkResult;

    @BindByTag("yhEditCommentInput")
    CustomEditText yhEditCommentInput;

    @BindByTag("xqBtn")
    Button xqBtn;

    @BindByTag("gdBtn")
    Button gdBtn;

    @BindByTag("ysBtn")
    Button ysBtn;


    private YHEntity mYHEntity;

    private SinglePickController mSinglePickController;
    private DatePickController mDatePickController;

    private Map<String, SystemCodeEntity> wxTypes;
    private Map<String, SystemCodeEntity> yhTypes;
    private Map<String, SystemCodeEntity> yhPriorities;
    private Map<String, Area> mAreas;
    private Map<String, RepairGroupEntity> mRepairGroups;

    //    private AttachmentController mAttachmentController;
    private LinkController mLinkController;
    private List<GalleryBean> pics = new ArrayList<>();
    private boolean isCancel = false;
    private long deploymentId;
    private boolean hits;

    private SparePartController mSparePartController;
    private LubricateOilsController mLubricateOilsController;
    private RepairStaffController mRepairStaffController;
    private MaintenanceController maintenanceController;
    //dataGrid删除数据id
    private List<Long> dgDeletedIds_sparePart = new ArrayList<>();
    private List<Long> dgDeletedIds_lubricateOils = new ArrayList<>();
    private List<Long> dgDeletedIds_repairStaff = new ArrayList<>();
    private List<Long> dgDeletedIds_maintenance = new ArrayList<>();

    //表体修改前的列表数据
    private String lubricateOilsListStr;
    private String repairStaffListStr;
    private String sparePartListStr;
    private String maintenanceListStr;
    private YHEntity oldYHEntity;
    private String tableNo;
    private List<SystemCodeEntity> wxTypeEntities;
    private List<SystemCodeEntity> yhPriorityEntity;
    private List<SystemCodeEntity> yhTypeEntities;

    private static String FINDSTAFF = "FINDSTAFF";
    private static String CHARGESTAFF = "CHARGESTAFF";
    private static String CHECK_STAFF = "CHECK_STAFF";
    private String staffType = FINDSTAFF;

    private AcceptanceCheckEntity currentAcceptChkEntity = new AcceptanceCheckEntity();  //当前验收数据
    private String currentAcceptChkDateTime = DateUtil.dateTimeFormat(new Date().getTime()); // 当前验收时间
    private List<SystemCodeEntity> checkResultList = new ArrayList<>();
    private List<String> checkResultListStr = new ArrayList<>();

    @Subscribe
    public void onReceiveImageDeleteEvent(ImageDeleteEvent imageDeleteEvent) {
        getController(OnlineCameraController.class).deleteGalleryBean(yhGalleryView.getGalleryAdapter().getList().get(imageDeleteEvent.getPos()), imageDeleteEvent.getPos());
//        yhGalleryView.deletePic(imageDeleteEvent.getPos());
        EventBus.getDefault().post(new RefreshEvent());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_yh_edit;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(true);
        mYHEntity = (YHEntity) getIntent().getSerializableExtra(Constant.IntentKey.YHGL_ENTITY);
        tableNo = getIntent().getStringExtra(Constant.IntentKey.TABLENO);
        deploymentId = getIntent().getLongExtra(DEPLOYMENT_ID, 0L);

        mSparePartController = getController(SparePartController.class);
        mSparePartController.setEditable(true);
        mLubricateOilsController = getController(LubricateOilsController.class);
        mLubricateOilsController.setEditable(true);
        mRepairStaffController = getController(RepairStaffController.class);
        mRepairStaffController.setEditable(true);
        maintenanceController = getController(MaintenanceController.class);
        maintenanceController.setEditable(true);

        initSinglePickController();
        initDatePickController();
        initCheckResult();
    }

    private void initCheckResult() {
        checkResultList = EamApplication.dao().getSystemCodeEntityDao().queryBuilder().where(SystemCodeEntityDao.Properties.EntityCode.eq(Constant.SystemCode.CHECK_RESULT)).list();
        for (SystemCodeEntity entity : checkResultList) {
            checkResultListStr.add(entity.value);
        }
    }

    private void initDatePickController() {
        mDatePickController = new DatePickController(this);
        mDatePickController.textSize(18);
        mDatePickController.setCycleDisable(false);
        mDatePickController.setCanceledOnTouchOutside(true);
    }

    private void initSinglePickController() {
        mSinglePickController = new SinglePickController<String>(this);
        mSinglePickController.textSize(18);
        mSinglePickController.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
        mLinkController = new LinkController();
        registerController(Constant.Controller.LINK, mLinkController);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("隐患编辑");

        updateInitView();
    }

    public void updateInitView() {
        if (mYHEntity == null) return;
        mYHEntity.downStream = new SystemCodeEntity();
        oldYHEntity = GsonUtil.gsonToBean(mYHEntity.toString(), YHEntity.class);

        if (null != mYHEntity.eamID && !mYHEntity.eamID.checkNil()) {
            yhEditArea.setEditable(false);
        }
        yhEditFindStaff.setValue(mYHEntity.findStaffID != null ? mYHEntity.findStaffID.name : "");
        yhEditWXChargeStaff.setValue(mYHEntity.chargeStaff != null ? mYHEntity.chargeStaff.name : "");
        yhEditFindTime.setDate(mYHEntity.findTime != 0 ? DateUtil.dateTimeFormat(mYHEntity.findTime) : "");
        yhEditArea.setSpinner(mYHEntity.areaInstall != null ? mYHEntity.areaInstall.name : "");
        if (mYHEntity.eamID != null && !TextUtils.isEmpty(mYHEntity.eamID.name)) {
            yhEditEamCode.setValue(mYHEntity.eamID.code);
            yhEditEamName.setValue(mYHEntity.eamID.name);
//            yhEditEamModel.setValue(mYHEntity.eamID.model);
        }
        yhEditWXChargeGroup.setContent(mYHEntity.repiarGroup != null ? mYHEntity.repiarGroup.name : "");
        if (!TextUtils.isEmpty(mYHEntity.describe)) {
            yhEditDescription.setInput(mYHEntity.describe);
        }
        getController(OnlineCameraController.class).init(Constant.IMAGE_SAVE_YHPATH, Constant.PicType.YH_PIC);
        if (mYHEntity.attachmentEntities != null) {
            getController(OnlineCameraController.class).setPicData(mYHEntity.attachmentEntities);
        }
        if (!TextUtils.isEmpty(mYHEntity.remark)) {
            yhEditMemo.setInput(mYHEntity.remark);
        }

        initCheckView();
    }

    private void initCheckView() {
        acceptChkStaff.setContent(EamApplication.getAccountInfo().staffName);
        acceptChkStaffCode.setContent(EamApplication.getAccountInfo().staffCode);
        acceptChkTime.setContent(currentAcceptChkDateTime);
        currentAcceptChkEntity.checkStaff = new Staff();
        currentAcceptChkEntity.checkStaff.id = EamApplication.getAccountInfo().staffId;
        currentAcceptChkEntity.checkStaff.code = EamApplication.getAccountInfo().staffCode;
        currentAcceptChkEntity.checkStaff.name = EamApplication.getAccountInfo().staffName;
        currentAcceptChkEntity.checkTime = DateUtil.dateFormat(currentAcceptChkDateTime, "yyyy-MM-dd HH:mm:ss");
    }

    @Override
    protected void initData() {
        super.initData();

        wxTypeEntities = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_WX_TYPE);
        wxTypes = initEntities(wxTypeEntities);

        yhTypeEntities = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.QX_TYPE);
        yhTypes = initEntities(yhTypeEntities);

        yhPriorityEntity = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_PRIORITY);
        yhPriorities = initEntities(yhPriorityEntity);

        List<Area> areaEntities = EamApplication.dao().getAreaDao().queryBuilder().where(AreaDao.Properties.Ip.eq(MBapApp.getIp())).list();
        mAreas = initEntities(areaEntities);

        List<RepairGroupEntity> repairGroupEntities =
                EamApplication.dao().getRepairGroupEntityDao().queryBuilder().where(RepairGroupEntityDao.Properties.IsUse.eq(Boolean.valueOf(true)), RepairGroupEntityDao.Properties.Ip.eq(EamApplication.getIp())).list();
        mRepairGroups = initEntities(repairGroupEntities);

        updateInitData();
    }

    public void updateInitData() {
        if (mYHEntity != null) {
            iniTransition();

            if (mYHEntity.priority == null && yhPriorityEntity.size() > 0) {
                mYHEntity.priority = yhPriorityEntity.get(0);
            }
            yhEditPriority.setSpinner(mYHEntity.priority != null ? mYHEntity.priority.value : "");
            if (mYHEntity.faultInfoType == null && yhTypeEntities.size() > 0) {
                mYHEntity.faultInfoType = yhTypeEntities.get(0);
            }
            yhEditType.setSpinner(mYHEntity.faultInfoType != null ? mYHEntity.faultInfoType.value : "");
            if (mYHEntity.repairType == null && wxTypeEntities.size() > 0) {
                mYHEntity.repairType = wxTypeEntities.get(0);
            }
            if (mYHEntity.repairType != null) {
                yhEditWXType.setSpinner(mYHEntity.repairType.value);
                if (Constant.YHWXType.JX_SYSCODE.equals(mYHEntity.repairType.id) || Constant.YHWXType.DX_SYSCODE.equals(mYHEntity.repairType.id)) {
                    yhEditWXChargeGroup.setEditable(false);
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    @SuppressWarnings("unchecked")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        yhEditArea.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.areaInstall = null;
            } else {
                List<String> list = new ArrayList<>(mAreas.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "区域位置列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            Area area = mAreas.get(item);
                            mYHEntity.areaInstall = area;
                            yhEditArea.setSpinner(item);
                        })
                        .show(yhEditArea.getSpinnerValue());
            }
        });

        yhEditWXChargeGroup.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.repiarGroup = null;
            } else {
                List<String> list = new ArrayList<>(mRepairGroups.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "维修组列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            RepairGroupEntity repairGroupEntity = mRepairGroups.get(item);
                            mYHEntity.repiarGroup = repairGroupEntity;
                            yhEditWXChargeGroup.setContent(item);
                        })
                        .show(yhEditWXChargeGroup.getContent());
            }
        });

        yhEditWXChargeStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                staffType = CHARGESTAFF;
                if (action == -1) {
                    mYHEntity.chargeStaff = null;
                } else {
                    IntentRouter.go(context, Constant.Router.STAFF);
                }
            }
        });

        yhEditType.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.faultInfoType = null;
            } else {
                List<String> list = new ArrayList<>(yhTypes.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "隐患类型列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            SystemCodeEntity yhType = yhTypes.get(item);
                            mYHEntity.faultInfoType = yhType;
                            yhEditType.setSpinner(item);
                        })
                        .show(yhEditType.getSpinnerValue());
            }
        });

        yhEditWXType.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.repairType = null;
                yhEditWXChargeGroup.setEditable(true);
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
                            mYHEntity.repairType = wxType;
                            yhEditWXType.setSpinner(item);

                            //大修或检修时，维修组不可编辑，若有值清空
                            if (Constant.YHWXType.DX.equals(item) || Constant.YHWXType.JX.equals(item)) {
                                yhEditWXChargeGroup.setEditable(false);
                                yhEditWXChargeGroup.setContent(null);
                                mYHEntity.repiarGroup = null;
                            } else {
                                yhEditWXChargeGroup.setEditable(true);
                            }
                        })
                        .show(yhEditWXType.getSpinnerValue());
            }
        });

        yhEditPriority.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.priority = null;
            } else {
                List<String> list = new ArrayList<>(yhPriorities.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "优先级列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            SystemCodeEntity yhPriority = yhPriorities.get(item);
                            mYHEntity.priority = yhPriority;
                            yhEditPriority.setSpinner(item);
                        })
                        .show(yhEditPriority.getSpinnerValue());
            }
        });

        yhEditFindTime.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.findTime = 0;
            } else {
                mDatePickController
                        .listener((year, month, day, hour, minute, second) -> {
                            LogUtil.i(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
                            String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                            long select = DateUtil.dateFormat(dateStr, "yyyy-MM-dd HH:mm");

                            mYHEntity.findTime = select;
                            yhEditFindTime.setDate(DateUtil.dateTimeFormat(mYHEntity.findTime));

                        })
                        .show(mYHEntity.findTime);
            }
        });

        RxTextView.textChanges(yhEditMemo.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> mYHEntity.remark = charSequence.toString());

        RxTextView.textChanges(yhEditDescription.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> mYHEntity.describe = charSequence.toString());

        RxView.clicks(xqBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    mYHEntity.downStream.id = "BEAM2_2013/01";
                    List<LinkEntity> linkEntities = mLinkController.getLinkEntities();
//                    if (TextUtils.isEmpty(yhEditWXChargeGroup.getValue()) && TextUtils.isEmpty(yhEditWXChargeStaff.getValue())) {
//                        mYHEntity.chargeStaff = new Staff();
//                        mYHEntity.chargeStaff.id = EamApplication.getAccountInfo().staffId;
//                        yhEditWXChargeStaff.setContent(EamApplication.getAccountInfo().staffName);
//                    }
                    if (checkBeforeSubmit() && linkEntities.size() > 0) {
                        doSubmit(createWorkFlowVar(linkEntities.get(0)));
                    }
                });

        RxView.clicks(gdBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    mYHEntity.downStream.id = "BEAM2_2013/02";
                    List<LinkEntity> linkEntities = mLinkController.getLinkEntities();
                    if (checkBeforeSubmit() && linkEntities.size() > 0) {
                        doSubmit(createWorkFlowVar(linkEntities.get(0)));
                    }
                });

        RxView.clicks(ysBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(Object o) throws Exception {
                        return checkResult();
                    }
                })
                .subscribe(o -> {
                    mYHEntity.downStream.id = "BEAM2_2013/03";
                    List<LinkEntity> linkEntities = mLinkController.getLinkEntities();
                    if (checkBeforeSubmit() && linkEntities.size() > 0) {
                        doSubmit(createWorkFlowVar(linkEntities.get(0)));
                    }
                });

        yhEditEamCode.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.eamID = null;
                mYHEntity.areaInstall = null;
                yhEditArea.setEditable(true);
                yhEditArea.setSpinner(null);
                yhEditEamName.setValue(null);
//                yhEditEamModel.setValue(null);
            } else {
                Bundle bundle = new Bundle();
                if (mYHEntity.areaInstall != null) {
                    bundle.putString(Constant.IntentKey.AREA_NAME, mYHEntity.areaInstall.name);
                }
                IntentRouter.go(this, Constant.Router.EAM, bundle);
            }
        });


        yhEditFindStaff.setOnChildViewClickListener((childView, action, obj) -> {
            staffType = FINDSTAFF;
            if (action == -1) {
                mYHEntity.findStaffID = null;
            } else {
                IntentRouter.go(context, Constant.Router.STAFF);
            }
        });

/*        getController(BaseCameraController.class).setOnSuccessListener(new OnSuccessListener<File>() {
            @Override
            public void onSuccess(File result) {
                uploadLocalPic(result);
            }
        });*/
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map<String, Object> queryParam = new HashMap<>();

                if (!TextUtils.isEmpty(tableNo)) {
                    queryParam.put(Constant.BAPQuery.TABLE_NO, tableNo);
                }

                if (mYHEntity != null && !TextUtils.isEmpty(mYHEntity.tableNo)) {
                    queryParam.put(Constant.BAPQuery.TABLE_NO, mYHEntity.tableNo);
                }

                if (queryParam.containsKey(Constant.BAPQuery.TABLE_NO)) {
                    presenterRouter.create(YHListAPI.class).queryYHList(1, queryParam);
                } else {
                    refreshController.refreshComplete();
                }
            }
        });

        acceptChkTime.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    currentAcceptChkEntity.checkTime = null;
                    currentAcceptChkDateTime = "";
                } else {
                    mDatePickController.listener((year, month, day, hour, minute, second) -> {
                        currentAcceptChkDateTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        acceptChkTime.setDate(currentAcceptChkDateTime);
                        currentAcceptChkEntity.checkTime = DateUtil.dateFormat(currentAcceptChkDateTime, "yyyy-MM-dd HH:mm:ss");
                    }).show("".equals(currentAcceptChkDateTime) ? new Date().getTime() : DateUtil.dateFormat(currentAcceptChkDateTime, "yyyy-MM-dd HH:mm:ss"));
                }
            }
        });

        acceptChkResult.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    currentAcceptChkEntity.checkResult = null;
                } else {
                    if (checkResultListStr.size() <= 0) {
                        ToastUtils.show(context, "验收列表数据为空,请退出app重启加载");
                        return;
                    }
                    mSinglePickController.list(checkResultListStr)
                            .listener((index, item) -> {
                                acceptChkResult.setSpinner(String.valueOf(item));

                                currentAcceptChkEntity.checkResult = checkResultList.get(checkResultListStr.indexOf(String.valueOf(item)));
                            }).show(acceptChkResult.getSpinnerValue());
                }
            }
        });

        acceptChkStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    currentAcceptChkEntity.checkStaff = null;
                    acceptChkStaffCode.setValue(null);
                } else {
                    staffType = CHECK_STAFF;
                    IntentRouter.go(context, Constant.Router.STAFF);
                }
            }
        });

    }

    private boolean checkResult() {
        if (currentAcceptChkEntity.checkStaff == null) {
            ToastUtils.show(context, "请填写验收人员");
            return false;
        }
        if (currentAcceptChkEntity.checkTime == null) {
            ToastUtils.show(context, "请填写验收时间");
            return false;
        }
        if (currentAcceptChkEntity.checkResult == null) {
            ToastUtils.show(context, "请填写验收结论");
            return false;
        }
        return true;
    }

    private WorkFlowVar createWorkFlowVar(LinkEntity linkEntity) {
        WorkFlowVar workFlowVar = new WorkFlowVar();
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        workFlowEntity.dec = linkEntity.description;
        workFlowEntity.outcome = linkEntity.name;
        workFlowEntity.type = "normal";
        workFlowEntities.add(workFlowEntity);
        workFlowVar.operateType = "submit";
        workFlowVar.dec = linkEntity.description;
        workFlowVar.outCome = linkEntity.name;
        workFlowVar.outcomeMapJson = workFlowEntities;
        return workFlowVar;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addDeviceEvent(CommonSearchEvent commonSearchEvent) {
        if (!(commonSearchEvent.commonSearchEntity instanceof EamType)) {
            return;
        }
        EamType eamType = (EamType) commonSearchEvent.commonSearchEntity;
        if (eamType != null) {
            yhEditEamName.setValue(eamType.name);
            yhEditEamCode.setValue(eamType.code);
//            yhEditEamModel.setValue(eamType.model);
            WXGDEam wxgdEam = new WXGDEam();
            wxgdEam.name = eamType.name;
            wxgdEam.code = eamType.code;
            wxgdEam.model = eamType.model;
            wxgdEam.id = eamType.id;
            mYHEntity.eamID = wxgdEam;
            mSparePartController.upEam(wxgdEam);
            mLubricateOilsController.upEam(wxgdEam);
            maintenanceController.upEam(wxgdEam);

            //处理区域位置
            yhEditArea.setEditable(false);
            yhEditArea.setSpinner(eamType.getInstallPlace().name);
            mYHEntity.areaInstall = eamType.getInstallPlace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMaintenanceStaff(CommonSearchEvent commonSearchEvent) {
        if (!(commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff)) {
            return;
        }
        CommonSearchStaff searchStaff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;
        if (staffType.equals(FINDSTAFF)) {
            yhEditFindStaff.setValue(searchStaff.name);
            mYHEntity.findStaffID = new Staff();
            mYHEntity.findStaffID.id = searchStaff.id;
            mYHEntity.findStaffID.code = searchStaff.code;
            mYHEntity.findStaffID.name = searchStaff.name;
        } else if (staffType.equals(CHARGESTAFF)) {
            yhEditWXChargeStaff.setValue(searchStaff.name);
            mYHEntity.chargeStaff = new Staff();
            mYHEntity.chargeStaff.id = searchStaff.id;
            mYHEntity.chargeStaff.code = searchStaff.code;
            mYHEntity.chargeStaff.name = searchStaff.name;
        } else if (CHECK_STAFF.equals(staffType)) {
            acceptChkStaff.setValue(searchStaff.name);
            acceptChkStaffCode.setValue(searchStaff.code);
            currentAcceptChkEntity.checkStaff = new Staff();
            currentAcceptChkEntity.checkStaff.id = searchStaff.id;
            currentAcceptChkEntity.checkStaff.code = searchStaff.code;
            currentAcceptChkEntity.checkStaff.name = searchStaff.name;
        }
    }

    /**
     * @param
     * @return
     * @description 接收原始数据
     * @author zhangwenshuai1 2018/10/11
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveList(ListEvent listEvent) {
        if ("lubricateOils".equals(listEvent.getFlag())) {
            lubricateOilsListStr = listEvent.getList().toString();
        } else if ("repairStaff".equals(listEvent.getFlag())) {
            repairStaffListStr = listEvent.getList().toString();
        } else if ("sparePart".equals(listEvent.getFlag())) {
            sparePartListStr = listEvent.getList().toString();
        } else if ("maintenance".equals(listEvent.getFlag())) {
            maintenanceListStr = listEvent.getList().toString();
        }
        if (listEvent.getList().size() > 0) {
            if (!"repairStaff".equals(listEvent.getFlag())) {
                yhEditEamCode.setEditable(false);
            }
        }
    }

    //更新备件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshSparePart(SparePartEvent sparePartEvent) {
        mSparePartController.updateSparePartEntities(sparePartEvent.getList());
        if (sparePartEvent.getList().size() <= 0) {
            mSparePartController.clear();
        }
        dgDeletedIds_sparePart = sparePartEvent.getDgDeletedIds();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshLubricateOils(LubricateOilsEvent event) {
        mLubricateOilsController.updateLubricateOilsEntities(event.getList());
        if (event.getList().size() <= 0) {
            mLubricateOilsController.clear();
        }
        dgDeletedIds_lubricateOils = event.getDgDeletedIds();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshRepairStaff(RepairStaffEvent event) {
        mRepairStaffController.updateRepairStaffEntiies(event.getList());
        if (event.getList().size() <= 0) {
//            repairStaffListWidget.clear();
            mRepairStaffController.clear();
        }

        dgDeletedIds_repairStaff = event.getDgDeletedIds();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshRepairStaff(MaintenanceEvent event) {
        maintenanceController.updateMaintenanceEntities(event.getList());
        if (event.getList().size() <= 0) {
            maintenanceController.clear();
        }
        dgDeletedIds_maintenance = event.getDgDeletedIds();
    }


    private void iniTransition() {

        if (mYHEntity.pending == null) {
            mLinkController.initStartTransition(null, "faultInfoFW");
        } else {
            mLinkController.initPendingTransition(null, mYHEntity.pending.id);
        }
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
    protected void onResume() {
        super.onResume();
        getWindow().setWindowAnimations(R.style.activityAnimation);
    }

    @Override
    public void onBackPressed() {
        //退出前校验表单是否修改过
        //如果修改过, 提示是否保存
        if (mYHEntity != null && (isModified() || doCheckChange())) {
            new CustomDialog(context)
                    .twoButtonAlertDialog("页面已经被修改，是否要保存?")
                    .bindView(R.id.grayBtn, "保存")
                    .bindView(R.id.redBtn, "离开")
                    .bindClickListener(R.id.grayBtn, v -> doSave(), true)
                    .bindClickListener(R.id.redBtn, v3 -> {
                        EventBus.getDefault().post(new RefreshEvent());
                        //关闭页面
                        YHEditActivity.this.finish();
                    }, true)
                    .show();
        } else {
            EventBus.getDefault().post(new RefreshEvent());
            super.onBackPressed();
        }

    }

    /**
     * 提交之前校验必填字段
     *
     * @return
     */
    private boolean checkBeforeSubmit() {

        if (isCancel) {
            return true;
        }
//        if (checkTableBlank()) {
//            return false;
//        }
        if (mYHEntity.findStaffID == null || null == mYHEntity.findStaffID.id) {
            SnackbarHelper.showError(rootView, "请填写发现人！");
            return false;
        }
        if (mYHEntity.findTime == 0) {
            SnackbarHelper.showError(rootView, "请填写发现时间！");
            return false;
        }

        if (mYHEntity.priority == null) {
            SnackbarHelper.showError(rootView, "请选择优先级！");
            return false;
        }

        //判断必填字段是否为空
        if (mYHEntity.eamID == null || mYHEntity.eamID.code == null) {
            SnackbarHelper.showError(rootView, "请选择设备");
            return false;
        }

        if (mYHEntity.faultInfoType == null) {
            SnackbarHelper.showError(rootView, "请选择隐患类型！");
            return false;
        }

        if (TextUtils.isEmpty(yhEditDescription.getInput())) {
            SnackbarHelper.showError(rootView, "请填写隐患现象！");
            return false;
        }

        if (mYHEntity.repairType == null) {
            SnackbarHelper.showError(rootView, "请选择维修类型！");
            return false;
        }
        return true;
    }

    private void doSave() {
        hits = true;
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
        map.put("faultInfo.createStaffId", (mYHEntity.findStaffID == null || mYHEntity.findStaffID.checkNil()) ? "" : Util.strFormat2(mYHEntity.findStaffID.id));
        map.put("faultInfo.createTime", DateUtil.dateTimeFormat(mYHEntity.findTime));
        map.put("faultInfo.findStaffID.id", (mYHEntity.findStaffID == null || mYHEntity.findStaffID.checkNil()) ? "" : Util.strFormat2(mYHEntity.findStaffID.id));
        map.put("faultInfo.findTime", DateUtil.dateTimeFormat(mYHEntity.findTime));
        map.put("faultInfo.createPositionId", EamApplication.getAccountInfo().positionId);
        map.put("viewselect", "faultInfoEdit");
        map.put("id", mYHEntity.id != -1 ? mYHEntity.id : "");
        map.put("faultInfo.id", mYHEntity.id != -1 ? mYHEntity.id : "");

        map.put("faultInfo.sourceType.id", mYHEntity.sourceType != null ? mYHEntity.sourceType.id : "BEAM2006/02");
        map.put("faultInfo.sourceType.value", mYHEntity.sourceType != null ? mYHEntity.sourceType.value : "其他");

        map.put("faultInfo.chargeStaff.id", mYHEntity.chargeStaff == null ? "" : Util.strFormat2(mYHEntity.chargeStaff.id));

        if (mYHEntity.pending != null && mYHEntity.pending.id != null) {
            map.put("pendingId", Util.strFormat2(mYHEntity.pending.id));
            map.put("deploymentId", Util.strFormat2(mYHEntity.pending.deploymentId));
        } else {
            map.put("deploymentId", deploymentId);
            map.put("faultInfo.version", 1);
        }


        if (mYHEntity.eamID != null && mYHEntity.eamID.id != null) {
            map.put("faultInfo.eamID.id", Util.strFormat2(mYHEntity.eamID.id));
        } else {
            map.put("faultInfo.eamID.id", "");
        }
        if (mYHEntity.downStream != null && mYHEntity.downStream.id != null) {
            map.put("faultInfo.downStream.id", mYHEntity.downStream.id);
        } else {
            map.put("faultInfo.downStream.id", "BEAM2_2013/03");
        }

        if (mYHEntity.areaInstall != null && mYHEntity.areaInstall.id != 0) {
            map.put("faultInfo.areaInstall.id", Util.strFormat2(mYHEntity.areaInstall.id));
        } else {
            map.put("faultInfo.areaInstall.id", "");
        }
        if (mYHEntity.repiarGroup != null && mYHEntity.repiarGroup.id != null) {
            map.put("faultInfo.repiarGroup.id", Util.strFormat2(mYHEntity.repiarGroup.id));
        } else {
            map.put("faultInfo.repiarGroup.id", "");
        }

        if (mYHEntity.faultInfoType != null) {
            map.put("faultInfo.faultInfoType.id", Util.strFormat2(mYHEntity.faultInfoType.id));
            map.put("faultInfo.faultInfoType.value", Util.strFormat2(mYHEntity.faultInfoType.value));
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

            if (Constant.Transition.CANCEL_CN.equals(workFlowEntity.dec)) {
                map.put("workFlowVarStatus", Constant.Transition.CANCEL);
            }
        } else {
//            map.put("workFlowVar.dec", "");
//            map.put("workFlowVar.outcome", "");
            map.put("operateType", "save");
        }
        map.put("taskDescription", "BEAM2_1.0.0.faultInfoFW.task342");
        map.put("activityName", "task342");
        map.put("workFlowVar.comment", yhEditCommentInput.getInput());

        map.put("viewCode", "BEAM2_1.0.0_faultInfo_faultInfoEdit");

        map.put("modelName", "FaultInfo");
        map.put("datagridKey", "BEAM2_faultInfo_faultInfo_faultInfoEdit_datagrids");
        map.put("__file_upload", true);

        //标题数据
        List<SparePartEntityDto> sparePartEntityDtos = YHGLMapManager.translateSparePartDto(mSparePartController.getSparePartEntities());
        List<RepairStaffDto> repairStaffDtos = YHGLMapManager.translateStaffDto(mRepairStaffController.getRepairStaffEntities());
        List<LubricateOilsEntityDto> lubricateOilsEntityDtos = YHGLMapManager.translateLubricateOilsDto(mLubricateOilsController.getLubricateOilsEntities());
        List<MaintainDto> maintainDtos = YHGLMapManager.translateMaintainDto(maintenanceController.getMaintenanceEntities());

        map = YHGLMapManager.dgDeleted(map, dgDeletedIds_sparePart, "dg1557402465409");
        map.put("dg1557402465409ModelCode", "BEAM2_1.0.0_faultInfo_FaultSparePart");
        map.put("dg1557402465409ListJson", sparePartEntityDtos.toString());
        map.put("dgLists['dg1557402465409']", sparePartEntityDtos.toString());

        map = YHGLMapManager.dgDeleted(map, dgDeletedIds_repairStaff, "dg1557402325583");
        map.put("dg1557402325583ModelCode", "BEAM2_1.0.0_faultInfo_FaultRepairStaff");
        map.put("dg1557402325583ListJson", repairStaffDtos);
        map.put("dgLists['dg1557402325583']", repairStaffDtos);

        map = YHGLMapManager.dgDeleted(map, dgDeletedIds_lubricateOils, "dg1557455440578");
        map.put("dg1557455440578ModelCode", "BEAM2_1.0.0_faultInfo_FaultLubricateOils");
        map.put("dg1557455440578ListJson", lubricateOilsEntityDtos);
        map.put("dgLists['dg1557455440578']", lubricateOilsEntityDtos);

        map = YHGLMapManager.dgDeleted(map, dgDeletedIds_maintenance, "dg1557457043896");
        map.put("dg1557457043896ModelCode", "BEAM2_1.0.0_faultInfo_MaintainJwx");
        map.put("dg1557457043896ListJson", maintainDtos);
        map.put("dgLists['dg1557457043896']", maintainDtos);

        if (!TextUtils.isEmpty(currentAcceptChkEntity.getCheckResult().id)) {
            List<AcceptanceCheckEntity> list = new ArrayList();
            list.add(currentAcceptChkEntity);
            LinkedList<AcceptanceCheckEntityDto> acceptanceCheckEntityDtos = YHGLMapManager.translateCheckResultDto(list);
//            map.put("dg1568882818859ModelCode", "BEAM2_1.0.0_faultInfo_FaultCheck");
//            map.put("dg1568882818859ListJson", acceptanceCheckEntityDtos);
//            map.put("dgLists['dg1568882818859']", acceptanceCheckEntityDtos);
            map.put("dg1568817060483ModelCode", "BEAM2_1.0.0_faultInfo_FaultCheck");
            map.put("dg1568817060483ListJson", acceptanceCheckEntityDtos);
            map.put("dgLists['dg1568817060483']", acceptanceCheckEntityDtos);
        }


        Map<String, Object> attachmentMap = new HashMap<>();

//        List<Map<String, Object>> files = new ArrayList<>();
//        for (GalleryBean galleryBean : pics) {
//            Map<String, Object> file = new HashMap<>();
//            file.put("file.filePath", galleryBean.url);
//            file.put("file.name", galleryBean.url.substring(galleryBean.url.lastIndexOf("\\") + 1));
//            file.put("file.memos", "pda隐患拍照上传");
//            file.put("file.fileType", "attachment");
//            files.add(file);
//        }
//
//        if (pics.size() != 0) {
//            attachmentMap.put("file.staffId", String.valueOf(EamApplication.getAccountInfo().staffId));
//            attachmentMap.put("file.type", "Table");
//            attachmentMap.put("files", files);
//            attachmentMap.put("linkId", String.valueOf(mYHEntity.tableInfoId));
//        }
        getController(OnlineCameraController.class).doSave(attachmentMap);
        if (attachmentMap.size() != 0) {
            attachmentMap.put("linkId", String.valueOf(mYHEntity.tableInfoId));
        }

        LogUtil.d(GsonUtil.gsonString(map));
        onLoading("单据处理中...");
        presenterRouter.create(YHSubmitAPI.class).doSubmit(map, attachmentMap, true);

    }

    @Override
    public boolean isModified() {
        return checkIsModified() || super.isModified();
    }

    /**
     * 判断是否填写过表单
     */
    private boolean checkIsModified() {
        if (oldYHEntity != null && mYHEntity != null && !GsonUtil.gsonString(mYHEntity).equals(oldYHEntity.toString())) {
            return true;
        }

        return !TextUtils.isEmpty(yhEditCommentInput.getInput());
    }

    @Override
    public void doSubmitSuccess(BapResultEntity entity) {
        LogUtil.d("entity:" + entity);

        RefreshEvent refreshEvent = new RefreshEvent();
        if (isCancel) {
            refreshEvent.action = Constant.Transition.CANCEL;
            isCancel = false;
        }
        EventBus.getDefault().post(refreshEvent);

        if (hits) {
            hits = false;
            onLoadSuccessAndExit("保存成功！", this::finish);
        } else {
            onLoadSuccessAndExit("处理成功！", this::finish);
        }
    }

    @Override
    public void doSubmitFailed(String errorMsg) {
        LogUtil.e("errorMsg:" + errorMsg);
        hits = false;
        onLoadFailed(errorMsg);
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
    }

    //检查是否变化
    public boolean doCheckChange() {
        if (!oldYHEntity.toString().equals(mYHEntity.toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(repairStaffListStr) && !repairStaffListStr.equals(mRepairStaffController.getRepairStaffEntities().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(lubricateOilsListStr) && !lubricateOilsListStr.equals(mLubricateOilsController.getLubricateOilsEntities().toString())) {
            return true;
        }
        if ((!TextUtils.isEmpty(sparePartListStr) && !sparePartListStr.equals(mSparePartController.getSparePartEntities().toString()))) {
            return true;
        }
        if ((!TextUtils.isEmpty(maintenanceListStr) && !maintenanceListStr.equals(maintenanceController.getMaintenanceEntities().toString()))) {
            return true;
        }
        return false;
    }

    @Override
    public void queryYHListSuccess(YHListEntity entity) {
        List<YHEntity> result = entity.result;
        if (result != null && result.size() > 0) {
            mYHEntity = result.get(0);
            updateInitView();
            updateInitData();
            mSparePartController.setYHEntity(mYHEntity);
            mLubricateOilsController.setYHEntity(mYHEntity);
            mRepairStaffController.setYHEntity(mYHEntity);
            maintenanceController.setYHEntity(mYHEntity);
            refreshController.refreshComplete();
        } else {
            ToastUtils.show(this, "未查到当前待办");
        }
    }

    @Override
    public void queryYHListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshController.refreshComplete();
    }

    /**
     * @param
     * @return
     * @description 判断维修组和负责人必须填其中之一
     * @author zhangwenshuai1 2018/10/23
     */
    private boolean checkTableBlank() {
        if (TextUtils.isEmpty(yhEditWXChargeGroup.getValue()) && TextUtils.isEmpty(yhEditWXChargeStaff.getValue())) {
            SnackbarHelper.showError(rootView, "维修组和负责人不允许同时为空！");
            return true;
        }
        return false;
    }
}
