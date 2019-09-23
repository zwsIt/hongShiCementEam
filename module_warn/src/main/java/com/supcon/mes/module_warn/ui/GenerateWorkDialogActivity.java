package com.supcon.mes.module_warn.ui;


import android.annotation.SuppressLint;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.RepairGroupEntity;
import com.supcon.mes.middleware.model.bean.RepairGroupEntityDao;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.api.DispatchAPI;
import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.contract.DispatchContract;
import com.supcon.mes.module_warn.presenter.DispatchPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

@Router(Constant.Router.GENERATE_WORK_DIALOG)
@Presenter(value = DispatchPresenter.class)
public class GenerateWorkDialogActivity extends BasePresenterActivity implements DispatchContract.View {

    @BindByTag("dispatchPlanStartDate")
    CustomVerticalDateView dispatchPlanStartDate;
    @BindByTag("dispatchPlanEndDate")
    CustomVerticalDateView dispatchPlanEndDate;
    @BindByTag("dispatchRepairGroup")
    CustomVerticalSpinner dispatchRepairGroup;
    @BindByTag("blueBtn")
    Button blueBtn;
    @BindByTag("grayBtn")
    Button grayBtn;

    private DatePickController mDatePickController;
    private SinglePickController mSinglePickController;
    private Long repairGroupId;//维修组id
    private String ids, sourceType, startDate, endDate;

    private Map<String, RepairGroupEntity> mRepairGroups;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_generate_work_dialog;
    }

    @Override
    protected void onInit() {
        super.onInit();
        ids = getIntent().getStringExtra(Constant.IntentKey.WARN_SOURCE_IDS);
        sourceType = getIntent().getStringExtra(Constant.IntentKey.WARN_SOURCE_TYPE);
    }

    @Override
    protected void initView() {
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (point.x * 0.8);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        setFinishOnTouchOutside(true);

        mDatePickController = new DatePickController(this);
        mDatePickController.setSecondVisible(false);
        mDatePickController.setCanceledOnTouchOutside(true);
        mDatePickController.setDividerVisible(true);

        mSinglePickController = new SinglePickController<String>(this);
        mSinglePickController.textSize(18);
        mSinglePickController.setCanceledOnTouchOutside(true);

        initRepairGroup();

    }

    private void initRepairGroup() {
        mRepairGroups = new HashMap<>();
        List<RepairGroupEntity> repairGroupEntities =
                EamApplication.dao().getRepairGroupEntityDao().queryBuilder().where(RepairGroupEntityDao.Properties.IsUse.eq(Boolean.valueOf(true)), RepairGroupEntityDao.Properties.Ip.eq(EamApplication.getIp())).list();

        for(RepairGroupEntity repairGroupEntity: repairGroupEntities){
            mRepairGroups.put(repairGroupEntity.name, repairGroupEntity);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        dispatchPlanStartDate.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                startDate = "";
            } else {
                mDatePickController.listener((year, month, day, hour, minute, second) -> {
                    startDate = year + "-" + month + "-" + day;
                    dispatchPlanStartDate.setDate(startDate);
                }).show(TextUtils.isEmpty(startDate)?System.currentTimeMillis():DateUtil.dateFormat(startDate));
            }
        });

        dispatchPlanEndDate.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                endDate = "";
            } else {
                mDatePickController.listener((year, month, day, hour, minute, second) -> {
                    endDate = year + "-" + month + "-" + day;
                    dispatchPlanEndDate.setDate(endDate);
                }).show(TextUtils.isEmpty(endDate)?System.currentTimeMillis():DateUtil.dateFormat(endDate));
            }
        });

        dispatchRepairGroup.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                repairGroupId = null;
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
                            repairGroupId = repairGroupEntity.id;
                            dispatchRepairGroup.setSpinner(item);
                        })
                        .show(dispatchRepairGroup.getSpinnerValue());
            }
        });



        RxView.clicks(blueBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (doCheck()) {
                        Map param = new HashMap<>();
                        param.put(Constant.BAPQuery.sourceIds, ids);
                        param.put(Constant.BAPQuery.sourceType, sourceType);
                        param.put(Constant.BAPQuery.startDate, startDate);
                        param.put(Constant.BAPQuery.endDate, endDate);
                        param.put(Constant.BAPQuery.repairGroupId, repairGroupId);
                        onLoading("派单中...");
                        presenterRouter.create(DispatchAPI.class).generateWork(param);
                    }
                });
        RxView.clicks(grayBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
    }

    public boolean doCheck() {

        if(TextUtils.isEmpty(dispatchPlanStartDate.getContent())){
            ToastUtils.show(context, "计划开始日期不允许为空");
            return false;
        }

        if(TextUtils.isEmpty(dispatchPlanEndDate.getContent())){
            ToastUtils.show(context, "计划结束日期不允许为空");
            return false;
        }

        if(TextUtils.isEmpty(dispatchRepairGroup.getContent())){
            ToastUtils.show(context, "维修组不允许为空");
            return false;
        }

        long start = DateUtil.dateFormat(dispatchPlanStartDate.getContent(), "yyyy-MM-dd");
        long end = DateUtil.dateFormat(dispatchPlanEndDate.getContent(), "yyyy-MM-dd");
        if (start >= end) {
            ToastUtils.show(this, "计划开始日期必须小于计划结束日期");
            return false;
        }

        startDate = dispatchPlanStartDate.getContent();
        endDate = dispatchPlanEndDate.getContent();

        return true;
    }


    @Override
    public void onBackPressed() {
        back();
    }

    @SuppressLint("CheckResult")
    @Override
    public void generateWorkSuccess(DelayEntity entity) {
        onLoadSuccessAndExit("派单成功!", () -> {
            Flowable.timer(300, TimeUnit.MILLISECONDS)
                    .subscribe(aLong -> {
                        EventBus.getDefault().post(new RefreshEvent());
                        back();
                    });
        });
    }

    @Override
    public void generateWorkFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }
}
