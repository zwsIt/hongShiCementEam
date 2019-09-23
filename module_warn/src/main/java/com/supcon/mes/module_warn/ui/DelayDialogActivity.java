package com.supcon.mes.module_warn.ui;


import android.annotation.SuppressLint;
import android.graphics.Point;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.api.DelayAPI;
import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.contract.DelayContract;
import com.supcon.mes.module_warn.presenter.DelayPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

@Router(Constant.Router.DELAYDIALOG)
@Presenter(value = DelayPresenter.class)
public class DelayDialogActivity extends BasePresenterActivity implements DelayContract.View {

    @BindByTag("delayDate")
    CustomVerticalDateView delayDateTv;
    @BindByTag("delayDuration")
    CustomVerticalEditText delayDuration;
    @BindByTag("delayReason")
    CustomVerticalEditText delayReasonTv;
    @BindByTag("blueBtn")
    Button blueBtn;
    @BindByTag("grayBtn")
    Button grayBtn;

    private DatePickController mDatePickController;
    private String delayDate;//延期日期
    private String delayReason="";//延期原因
    private String ids, sourceType, peroidType;
    private long nextTime;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_delay_dialog;
    }

    @Override
    protected void onInit() {
        super.onInit();
        ids = getIntent().getStringExtra(Constant.IntentKey.WARN_SOURCE_IDS);
        sourceType = getIntent().getStringExtra(Constant.IntentKey.WARN_SOURCE_TYPE);
        peroidType = getIntent().getStringExtra(Constant.IntentKey.WARN_PEROID_TYPE);
        nextTime = getIntent().getLongExtra(Constant.IntentKey.WARN_NEXT_TIME, 0);
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
        delayDuration.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        if (!TextUtils.isEmpty(peroidType) && peroidType.equals(Constant.PeriodType.TIME_FREQUENCY)) {
            delayDuration.setVisibility(View.GONE);
            delayDateTv.setDate(DateUtil.dateFormat(nextTime));
            delayDate = DateUtil.dateFormat(nextTime);
        } else {
            delayDateTv.setVisibility(View.GONE);
        }

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        delayDateTv.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                delayDate = "";
            } else {
                mDatePickController.listener((year, month, day, hour, minute, second) -> {
                    delayDate = year + "-" + month + "-" + day;
                    delayDateTv.setDate(delayDate);
                }).show(nextTime);
            }
        });

        RxTextView.textChanges(delayReasonTv.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    delayReason = charSequence.toString();
                });

        RxView.clicks(blueBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (doCheck()) {
                        Map param = new HashMap<>();
                        param.put(Constant.BAPQuery.sourceIds, ids);
                        param.put(Constant.BAPQuery.sourceType, sourceType);
                        param.put(Constant.BAPQuery.delayDate, delayDate);
                        param.put(Constant.BAPQuery.delayReason, delayReason);
                        param.put(Constant.BAPQuery.peroidType, peroidType);
                        onLoading("延期中...");
                        presenterRouter.create(DelayAPI.class).delayDate(param);
                    }
                });
        RxView.clicks(grayBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
    }

    public boolean doCheck() {
        if (TextUtils.isEmpty(delayDate)) {
            ToastUtils.show(this, "延期日期不能为空");
            return false;
        }
        long select = DateUtil.dateFormat(delayDate, "yyyy-MM-dd");
        if (select <= nextTime) {
            ToastUtils.show(this, "延期日期必须大于" + DateUtil.dateFormat(nextTime));
            return false;
        }
        if (TextUtils.isEmpty(delayReason.trim())) {
            ToastUtils.show(this, "延期原因不允许为空!");
            return false;
        }
        return true;
    }

    @SuppressLint("CheckResult")
    @Override
    public void delayDateSuccess(DelayEntity entity) {
        onLoadSuccessAndExit("延期成功!", () -> {
            Flowable.timer(300, TimeUnit.MILLISECONDS)
                    .subscribe(aLong -> {
                        EventBus.getDefault().post(new RefreshEvent());
                        back();
                    });
        });
    }

    @Override
    public void delayDateFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
