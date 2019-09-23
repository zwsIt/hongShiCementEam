package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DatePickController;
import com.supcon.mes.middleware.model.api.WorkCountAPI;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.WorkCountEntity;
import com.supcon.mes.middleware.model.contract.WorkCountContract;
import com.supcon.mes.middleware.presenter.WorkCountPresenter;
import com.supcon.mes.middleware.util.AnimatorUtil;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.model.api.WXGDStatisticsAPI;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;
import com.supcon.mes.module_wxgd.model.contract.WXGDStatisticsContract;
import com.supcon.mes.module_wxgd.presenter.WXGDStatisticsPresenter;
import com.supcon.mes.module_wxgd.ui.adapter.WXGDStatisticsAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/8/13
 * ------------- Description -------------
 */
@Router(Constant.Router.WXGD_STATISTICS)
@Presenter(value = {WorkCountPresenter.class, WXGDStatisticsPresenter.class})
public class WXGDStatisticsActivity extends BaseRefreshRecyclerActivity<WXGDEntity> implements WXGDStatisticsContract.View, WorkCountContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("statisticsWaitNum")
    TextView statisticsWaitNum;
    @BindByTag("statisticsProcessingNum")
    TextView statisticsProcessingNum;
    @BindByTag("statisticsCompletedNum")
    TextView statisticsCompletedNum;


    private WXGDStatisticsAdapter yhglStatisticsAdapter;

    Map<String, Object> queryParam = new HashMap<>();
    Map<String, Object> workCountQueryParam = new HashMap<>();
    private View timeStart, timeEnd;
    private DatePickController datePickController;
    private ImageView startExpend, endExpend;
    private TextView startDate, endDate;

    @Override
    protected IListAdapter<WXGDEntity> createAdapter() {
        yhglStatisticsAdapter = new WXGDStatisticsAdapter(this);
        return yhglStatisticsAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_statistics;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));

        timeStart = findViewById(R.id.statisticsStart);
        timeEnd = findViewById(R.id.statisticsEnd);
        startExpend = timeStart.findViewById(R.id.expend);
        startDate = timeStart.findViewById(R.id.dateTv);
        endExpend = timeEnd.findViewById(R.id.expend);
        endDate = timeEnd.findViewById(R.id.dateTv);
        datePickController = new DatePickController(this);

        datePickController.setCycleDisable(true);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(false);
        datePickController.textSize(18);
    }

    @Override
    protected void initData() {
        super.initData();
        titleText.setText("工单统计");

        queryParam.put(Constant.BAPQuery.CREATE_DATE_START, DateUtil.dateFormat(getTimeOfMonthStart(), "yyyy-MM-dd 00:00:00"));
        queryParam.put(Constant.BAPQuery.CREATE_DATE_END, DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd 23:59:59"));
        startDate.setText(DateUtil.dateFormat(getTimeOfMonthStart(), "yyyy-MM-dd"));
        endDate.setText(DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd"));

        workCountQueryParam.put("startTime", DateUtil.dateFormat(getTimeOfMonthStart(), "yyyy-MM-dd"));
        workCountQueryParam.put("endTime", DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd"));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        refreshListController.setOnRefreshPageListener(
                pageIndex -> {
                    presenterRouter.create(WXGDStatisticsAPI.class).listWxgds(pageIndex, queryParam);

                    if (pageIndex == 1) {
                        presenterRouter.create(WorkCountAPI.class).getWorkCount("/BEAM2/patrolWorkerScore/workerScoreHead/getWorkRecordCountByState.action", workCountQueryParam);
                    }
                });

        timeStart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                AnimatorUtil.rotationExpandIcon(startExpend, 0, 180);
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    if (compareTime(year + "-" + month + "-" + day, endDate.getText().toString())) {
                        startDate.setText(year + "-" + month + "-" + day);
                        queryParam.put(Constant.BAPQuery.CREATE_DATE_START, year + "-" + month + "-" + day + " 00:00:00");
                        workCountQueryParam.put("startTime", year + "-" + month + "-" + day);
                        refreshListController.refreshBegin();
                    }
                }).show(DateUtil.dateFormat(startDate.getText().toString()), startExpend);
            }
        });
        timeEnd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                AnimatorUtil.rotationExpandIcon(endExpend, 0, 180);
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    if (compareTime(startDate.getText().toString(), year + "-" + month + "-" + day)) {
                        endDate.setText(year + "-" + month + "-" + day);
                        queryParam.put(Constant.BAPQuery.CREATE_DATE_END, year + "-" + month + "-" + day + " 23:59:59");
                        workCountQueryParam.put("endTime", year + "-" + month + "-" + day);
                        refreshListController.refreshBegin();
                    }
                }).show(DateUtil.dateFormat(endDate.getText().toString()), endExpend);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {

        refreshListController.refreshBegin();
    }

    @Override
    public void listWxgdsSuccess(WXGDListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listWxgdsFailed(String errorMsg) {
        refreshListController.refreshComplete();
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
    }

    @SuppressLint("CheckResult")
    @Override
    public void getWorkCountSuccess(CommonListEntity entity) {
        if (entity.result.size() > 0) {
            Flowable.fromIterable((List<WorkCountEntity>) entity.result)
                    .subscribe(new Consumer<WorkCountEntity>() {
                        @Override
                        public void accept(WorkCountEntity workCountEntity) throws Exception {
                            if (workCountEntity.tagName.equals(Constant.WxgdStatus.DISPATCH)) {
                                statisticsWaitNum.setText(String.valueOf(workCountEntity.num));
                            } else if (workCountEntity.tagName.equals(Constant.WxgdStatus.CONFIRM)) {
                                statisticsProcessingNum.setText(String.valueOf(workCountEntity.num));
                            } else if (workCountEntity.tagName.equals(Constant.WxgdStatus.COMPLETE)) {
                                statisticsCompletedNum.setText(String.valueOf(workCountEntity.num));
                            }
                        }
                    });
        }
    }

    @Override
    public void getWorkCountFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
    }

    public static long getTimeOfMonthStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        return ca.getTimeInMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private boolean compareTime(String start, String stop) {
        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(stop)) {
            return false;
        }
        long startTime = DateUtil.dateFormat(start, "yyyy-MM-dd");
        long stopTime = DateUtil.dateFormat(stop, "yyyy-MM-dd");
        if (stopTime >= startTime) {
            return true;
        }
        ToastUtils.show(this, "开始时间不能大于结束时间!");
        return false;
    }

}
