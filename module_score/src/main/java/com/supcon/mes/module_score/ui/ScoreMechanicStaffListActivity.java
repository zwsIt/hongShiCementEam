package com.supcon.mes.module_score.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_score.IntentRouter;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.api.ScoreStaffListAPI;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.model.contract.ScoreStaffListContract;
import com.supcon.mes.module_score.presenter.ScoreStaffListPresenter;
import com.supcon.mes.module_score.ui.adapter.ScoreStaffListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 机修工 评分
 */
@Router(value = Constant.Router.SCORE_MECHANIC_STAFF_LIST)
@Presenter(value = ScoreStaffListPresenter.class)
public class ScoreMechanicStaffListActivity extends BaseRefreshRecyclerActivity implements ScoreStaffListContract.View {

    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;

    @BindByTag("rightBtn")
    AppCompatImageButton rightBtn;

    @BindByTag("customSearchView")
    CustomSearchView customSearchView;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;


    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("startTime")
    CustomVerticalDateView startTime;
    @BindByTag("stopTime")
    CustomVerticalDateView stopTime;

    private DatePickController datePickController;

    private final Map<String, Object> queryParam = new HashMap<>();
    private String selecStr;
    private ScoreStaffListAdapter scoreStaffListAdapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected IListAdapter createAdapter() {
        scoreStaffListAdapter = new ScoreStaffListAdapter("机修工", this);
        return scoreStaffListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_score_list;
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
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(15));

        datePickController = new DatePickController(this);
        datePickController.setCycleDisable(true);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(true);
        datePickController.textSize(18);

        customSearchView.setHint("请输入机修工名字");
        searchTitleBar.enableRightBtn();
        searchTitleBar.setTitleText("机修工评分");
        startTime.setDate(getThreeDay());
        stopTime.setDate(dateFormat.format(System.currentTimeMillis()));

        queryParam.put(Constant.BAPQuery.SCORE_DATA_START, getThreeDay());
        queryParam.put(Constant.BAPQuery.SCORE_DATA_STOP, dateFormat.format(System.currentTimeMillis()));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.isEdit, true);
                    IntentRouter.go(ScoreMechanicStaffListActivity.this, Constant.Router.SCORE_MECHANIC_STAFF_PERFORMANCE, bundle);
                });
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            if (queryParam.containsKey(Constant.BAPQuery.NAME)) {
                queryParam.remove(Constant.BAPQuery.NAME);
            }
            if (!TextUtils.isEmpty(selecStr)) {
                queryParam.put(Constant.BAPQuery.NAME, selecStr);
            }
            String url = "/BEAM/patrolWorkerScore/workerScoreHead/repairerScoreList-query.action";
            presenterRouter.create(ScoreStaffListAPI.class).patrolScore(url, queryParam, pageIndex);
        });

        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        doSearchTableNo(charSequence.toString());
                    }
                });
        KeyExpandHelper.doActionSearch(customSearchView.editText(), true, () ->
                doSearchTableNo(customSearchView.getInput()));

        scoreStaffListAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                ScoreStaffEntity item = scoreStaffListAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.IntentKey.SCORE_ENTITY, item);
                bundle.putBoolean(Constant.IntentKey.isEdit, compareTimeIsEdit(item.scoreData != null ? item.scoreData : 0));
                IntentRouter.go(ScoreMechanicStaffListActivity.this, Constant.Router.SCORE_MECHANIC_STAFF_PERFORMANCE, bundle);
            }
        });

        startTime.setOnChildViewClickListener((childView, action, obj) -> {
            datePickController.listener((year, month, day, hour, minute, second) -> {
                String date = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                startTime.setDate(date);
                if (compareTime(startTime.getContent(), stopTime.getContent())) {
                    if (!queryParam.containsKey(Constant.BAPQuery.SCORE_DATA_START)) {
                        queryParam.remove(Constant.BAPQuery.SCORE_DATA_START);
                    }
                    queryParam.put(Constant.BAPQuery.SCORE_DATA_START, date);
                    refreshListController.refreshBegin();
                }

            }).show(DateUtil.dateFormat(getThreeDay()));
        });

        stopTime.setOnChildViewClickListener((childView, action, obj) ->
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    String date = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    stopTime.setDate(date);
                    if (compareTime(startTime.getContent(), stopTime.getContent())) {
                        if (!queryParam.containsKey(Constant.BAPQuery.SCORE_DATA_STOP)) {
                            queryParam.remove(Constant.BAPQuery.SCORE_DATA_STOP);
                        }
                        queryParam.put(Constant.BAPQuery.SCORE_DATA_STOP, date);
                        refreshListController.refreshBegin();
                    }
                }).show(System.currentTimeMillis()));
    }

    public void doSearchTableNo(String search) {
        selecStr = search;
        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {

        refreshListController.refreshBegin();
    }

    @Override
    public void patrolScoreSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void patrolScoreFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public String getThreeDay() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DAY_OF_MONTH, -3);
        String s = DateUtil.dateFormat(ca.getTimeInMillis(), "yyyy-MM-dd 00:00:00");
        return s;
    }

    public String getYesterday() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DAY_OF_MONTH, -1);
        String s = DateUtil.dateFormat(ca.getTimeInMillis(), "yyyy-MM-dd 00:00:00");
        return s;
    }

    private boolean compareTime(String start, String stop) {
        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(stop)) {
            return false;
        }
        try {
            long startTime = dateFormat.parse(start).getTime();
            long stopTime = dateFormat.parse(stop).getTime();
            if (stopTime > startTime) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ToastUtils.show(this, "开始时间不能大于结束时间!");
        return false;
    }

    private boolean compareTimeIsEdit(long createTime) {
        long nowTime = DateUtil.dateFormat(getYesterday());
        if (createTime == 0 || nowTime == 0) {
            return false;
        }
        if (nowTime > createTime) {
            return false;
        }
        return true;
    }


}
