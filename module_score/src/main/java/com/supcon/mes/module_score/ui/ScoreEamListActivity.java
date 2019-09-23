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
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.IntentRouter;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.api.ScoreEamListAPI;
import com.supcon.mes.module_score.model.bean.ScoreEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamListEntity;
import com.supcon.mes.module_score.model.contract.ScoreEamListContract;
import com.supcon.mes.module_score.presenter.ScoreEamListPresenter;
import com.supcon.mes.module_score.ui.adapter.ScoreEamListAdapter;

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

import io.reactivex.functions.Consumer;

@Router(value = Constant.Router.SCORE_EAM_LIST)
@Presenter(value = ScoreEamListPresenter.class)
public class ScoreEamListActivity extends BaseRefreshRecyclerActivity implements ScoreEamListContract.View {

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
    private ScoreEamListAdapter scoreEamListAdapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private String scoreTableNo;

    @Override
    protected IListAdapter createAdapter() {
        scoreEamListAdapter = new ScoreEamListAdapter(this);
        return scoreEamListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_score_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        scoreTableNo = getIntent().getStringExtra(Constant.IntentKey.SCORETABLENO);
        selecStr = getIntent().getStringExtra(Constant.IntentKey.EAM_CODE);
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

        customSearchView.setHint("请输入设备");
        searchTitleBar.enableRightBtn();
        searchTitleBar.setTitleText("设备评分绩效");
        customSearchView.setInput(selecStr);
        if (TextUtils.isEmpty(scoreTableNo) && TextUtils.isEmpty(selecStr)) {
            startTime.setDate(getThreeDay());
            stopTime.setDate(dateFormat.format(System.currentTimeMillis()));
            queryParam.put(Constant.BAPQuery.SCORE_TIME_START, getThreeDay());
            queryParam.put(Constant.BAPQuery.SCORE_TIME_STOP, dateFormat.format(System.currentTimeMillis()));
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onBackPressed();
                    }
                });
        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.isEdit, true);
                    IntentRouter.go(ScoreEamListActivity.this, Constant.Router.SCORE_EAM_PERFORMANCE, bundle);
                });
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            queryParam.remove(Constant.BAPQuery.EAM_NAME);
            queryParam.remove(Constant.BAPQuery.EAM_CODE);
            if (!TextUtils.isEmpty(selecStr)) {
                if (Util.isContainChinese(selecStr)) {
                    queryParam.put(Constant.BAPQuery.EAM_NAME, selecStr);
                } else {
                    queryParam.put(Constant.BAPQuery.EAM_CODE, selecStr);
                }
            }
            queryParam.remove(Constant.BAPQuery.SCORE_TABLE_NO);
            if (!TextUtils.isEmpty(scoreTableNo)) {
                queryParam.put(Constant.BAPQuery.SCORE_TABLE_NO, scoreTableNo);
            }
            presenterRouter.create(ScoreEamListAPI.class).getScoreList(queryParam, pageIndex);
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

        scoreEamListAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                ScoreEamEntity item = scoreEamListAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.IntentKey.SCORE_ENTITY, item);
                bundle.putBoolean(Constant.IntentKey.isEdit, compareTimeIsEdit(item.scoreTime != null ? item.scoreTime : 0));
                IntentRouter.go(ScoreEamListActivity.this, Constant.Router.SCORE_EAM_PERFORMANCE, bundle);
            }
        });

        startTime.setOnChildViewClickListener((childView, action, obj) -> {
            datePickController.listener((year, month, day, hour, minute, second) -> {
                String date = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                startTime.setDate(date);
                if (compareTime(startTime.getContent(), stopTime.getContent())) {
                    if (!queryParam.containsKey(Constant.BAPQuery.SCORE_TIME_START)) {
                        queryParam.remove(Constant.BAPQuery.SCORE_TIME_START);
                    }
                    queryParam.put(Constant.BAPQuery.SCORE_TIME_START, date);
                    refreshListController.refreshBegin();
                }

            }).show(DateUtil.dateFormat(queryParam.containsKey(Constant.BAPQuery.SCORE_TIME_START) ? (String) queryParam.get(Constant.BAPQuery.SCORE_TIME_START) : getThreeDay()));
        });

        stopTime.setOnChildViewClickListener((childView, action, obj) ->
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    String date = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    stopTime.setDate(date);
                    if (compareTime(startTime.getContent(), stopTime.getContent())) {
                        if (!queryParam.containsKey(Constant.BAPQuery.SCORE_TIME_STOP)) {
                            queryParam.remove(Constant.BAPQuery.SCORE_TIME_STOP);
                        }
                        queryParam.put(Constant.BAPQuery.SCORE_TIME_STOP, date);
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
    public void getScoreListSuccess(ScoreEamListEntity entity) {
        if (entity.result != null) {
            refreshListController.refreshComplete(entity.result);
            if (!TextUtils.isEmpty(scoreTableNo) && entity.result.size() == 1) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.IntentKey.SCORE_ENTITY, entity.result.get(0));
                bundle.putBoolean(Constant.IntentKey.isEdit, false);
                IntentRouter.go(ScoreEamListActivity.this, Constant.Router.SCORE_EAM_PERFORMANCE, bundle);
                finish();
            }
        } else {
            refreshListController.refreshComplete(null);
        }
    }

    @Override
    public void getScoreListFailed(String errorMsg) {
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
