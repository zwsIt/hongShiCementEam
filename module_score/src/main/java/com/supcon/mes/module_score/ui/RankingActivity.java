package com.supcon.mes.module_score.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DatePickController;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.util.AnimatorUtil;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_score.IntentRouter;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.api.ScoreStaffListAPI;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.model.contract.ScoreStaffListContract;
import com.supcon.mes.module_score.presenter.ScoreStaffListPresenter;
import com.supcon.mes.module_score.ui.adapter.RankingAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/31
 * ------------- Description -------------
 */
@Router(value = Constant.Router.RANKING)
@Presenter(value = ScoreStaffListPresenter.class)
public class RankingActivity extends BaseRefreshRecyclerActivity implements ScoreStaffListContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;


    @BindByTag("dateLayout")
    LinearLayout dateLayout;
    @BindByTag("dateTv")
    TextView dateTv;
    @BindByTag("expend")
    ImageView expend;


    private DatePickController datePickController;
    private RankingAdapter rankingAdapter;

    private final Map<String, Object> queryParam = new HashMap<>();
    private int ranking;
    private String type;

    @Override
    protected IListAdapter createAdapter() {
        rankingAdapter = new RankingAdapter(this);
        return rankingAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_rangking;
    }

    @Override
    protected void onInit() {
        super.onInit();
        ranking = getIntent().getIntExtra(Constant.IntentKey.RANKING, -1);
        type = getIntent().getStringExtra(Constant.IntentKey.TYPE);
        rankingAdapter.setRank(ranking);
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
        if (type.equals("BEAM_065/03")) {
            titleText.setText("机修工排名");
        } else {
            titleText.setText("巡检工排名");
        }
        datePickController = new DatePickController(this);

        datePickController.setCycleDisable(true);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(false);
        datePickController.textSize(18);

        dateTv.setText(DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd"));
        queryParam.put(Constant.BAPQuery.SCORE_DATA_START, DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd 00:00:00"));
        queryParam.put(Constant.BAPQuery.SCORE_DATA_STOP, DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd 23:59:59"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnimatorUtil.rotationExpandIcon(expend, 180, 0);
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

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                AnimatorUtil.rotationExpandIcon(expend, 0, 180);
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    dateTv.setText(year + "-" + month + "-" + day);
                    queryParam.put(Constant.BAPQuery.SCORE_DATA_START, year + "-" + month + "-" + day + " 00:00:00");
                    queryParam.put(Constant.BAPQuery.SCORE_DATA_STOP, year + "-" + month + "-" + day + " 23:59:59");
                    refreshListController.refreshBegin();
                }).show(DateUtil.dateFormat(dateTv.getText().toString()), expend);
            }
        });

        refreshListController.setOnRefreshPageListener(pageIndex -> {
            String url="/BEAM/patrolWorkerScore/workerScoreHead/patrolMonhtList-query.action";
//            if (type.equals("BEAM_065/03")) {
////                url = "/BEAM/patrolWorkerScore/workerScoreHead/repairerScoreList-query.action";
////            } else {
////                url = "/BEAM/patrolWorkerScore/workerScoreHead/patrolScore-query.action";
////            }
            presenterRouter.create(ScoreStaffListAPI.class).patrolScore(url, queryParam, pageIndex);
        });
        rankingAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.IntentKey.SCORE_ENTITY, ((ScoreStaffEntity) obj));
                bundle.putBoolean(Constant.IntentKey.isEdit, false);
                if (type.equals("BEAM_065/03")) {
                    IntentRouter.go(context, Constant.Router.SCORE_MECHANIC_STAFF_PERFORMANCE, bundle);
                } else {
                    IntentRouter.go(context, Constant.Router.SCORE_INSPECTOR_STAFF_DAILY_PERFORMANCE, bundle);
                }
            }
        });
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
}
