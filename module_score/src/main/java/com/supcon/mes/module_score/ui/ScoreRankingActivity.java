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
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DatePickController;
import com.supcon.mes.middleware.controller.UserPowerCheckController;
import com.supcon.mes.middleware.model.api.CommonListAPI;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.AnimatorUtil;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_score.IntentRouter;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.constant.ScoreConstant;
import com.supcon.mes.module_score.model.api.ScoreStaffListAPI;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.model.contract.ScoreStaffListContract;
import com.supcon.mes.module_score.presenter.ScoreStaffListPresenter;
import com.supcon.mes.module_score.presenter.ScoreStaffRankListPresenter;
import com.supcon.mes.module_score.ui.adapter.RankingAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ScoreRankingActivity
 * created by zhangwenshuai1 2020/8/3
 * 个人绩效列表（排名）
 */
@Router(value = Constant.Router.SCORE_RANKING)
@Presenter(value = ScoreStaffRankListPresenter.class)
@Controller(value = {UserPowerCheckController.class})
public class ScoreRankingActivity extends BaseRefreshRecyclerActivity implements CommonListContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("dateLayout")
    LinearLayout dateLayout;
    @BindByTag("dateTv")
    TextView dateTv;
    @BindByTag("expend")
    ImageView expend;

    private DatePickController datePickController;
    private RankingAdapter rankingAdapter;

    private final Map<String, Object> queryParam = new HashMap<>();
//    private int ranking;
//    private String type;

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
//        ranking = getIntent().getIntExtra(Constant.IntentKey.RANKING, -1);
        EventBus.getDefault().register(this);
//        type = getIntent().getStringExtra(Constant.IntentKey.TYPE);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(15));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        titleText.setText("个人绩效排名");
        datePickController = new DatePickController(this);

        datePickController.setCycleDisable(true);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(false);
        datePickController.textSize(18);

        dateTv.setText(DateUtil.dateFormat(getYesterday(), "yyyy-MM-dd"));
        queryParam.put(Constant.BAPQuery.SCORE_DATA_START, DateUtil.dateFormat(getYesterday(), "yyyy-MM-dd 00:00:00"));
        queryParam.put(Constant.BAPQuery.SCORE_DATA_STOP, DateUtil.dateFormat(getYesterday(), "yyyy-MM-dd 23:59:59"));

    }

    private void checkUserAddPermission() {
        String operateCode;
//        if (ScoreConstant.ScoreType.INSPECTION_STAFF.equals(type)){
            operateCode = ScoreConstant.Permission.add_inspection_staff;
//        }else if (ScoreConstant.ScoreType.EAM_MACHINE_STAFF.equals(type)){
//            operateCode = ScoreConstant.Permission.add_machine_staff;
//        }
        String finalOperateCode = operateCode;
        getController(UserPowerCheckController.class).checkModulePermission(EamApplication.getCid(), operateCode, result -> {
            if (result.get(finalOperateCode) != null && result.get(finalOperateCode)){
                rightBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
//                .throttleFirst(0, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        RxView.clicks(rightBtn).throttleFirst(500,TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.isEdit, true);
                    bundle.putBoolean(Constant.IntentKey.UPDATE, false);
                    IntentRouter.go(context, Constant.Router.SCORE_STAFF_PERFORMANCE, bundle);
//                        if (ScoreConstant.ScoreType.INSPECTION_STAFF.equals(type)){
//                            IntentRouter.go(context, Constant.Router.SCORE_INSPECTOR_STAFF_PERFORMANCE, bundle);
//                        }else if (ScoreConstant.ScoreType.EAM_MACHINE_STAFF.equals(type)){
//                            IntentRouter.go(context, Constant.Router.SCORE_MECHANIC_STAFF_PERFORMANCE, bundle);
//                        }
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
//            String url/* = "/BEAM/patrolWorkerScore/workerScoreHead/patrolMonhtList-query.action"*/;  // 每日个人绩效list
            checkUserAddPermission();
//            if (type.equals(ScoreConstant.ScoreType.EAM_MACHINE_STAFF)) {
//                url = "/BEAM/patrolWorkerScore/workerScoreHead/repairerScoreList-query.action";
//            } else {
//                url = "/BEAM/patrolWorkerScore/workerScoreHead/patrolScore-query.action";
//            }
            presenterRouter.create(CommonListAPI.class).listCommonObj(pageIndex,queryParam,false);
        });
        rankingAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                ScoreStaffEntity scoreStaffEntity = (ScoreStaffEntity) obj;
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.IntentKey.SCORE_ENTITY, scoreStaffEntity);
                bundle.putBoolean(Constant.IntentKey.isEdit, scoreStaffEntity.scoreData >= getToday());
                bundle.putBoolean(Constant.IntentKey.UPDATE, scoreStaffEntity.scoreData >= getToday());
                IntentRouter.go(context, Constant.Router.SCORE_STAFF_PERFORMANCE, bundle);
//                if (type.equals(ScoreConstant.ScoreType.EAM_MACHINE_STAFF)) {
//                    // 机修工每日评分
//                    IntentRouter.go(context, Constant.Router.SCORE_MECHANIC_STAFF_PERFORMANCE, bundle);
//                } else {
//                    // 巡检工每日评分
//                    IntentRouter.go(context, Constant.Router.SCORE_INSPECTOR_STAFF_PERFORMANCE, bundle);
//                }
            }
        });
    }

    public long getYesterday() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DAY_OF_MONTH, -1);
        return ca.getTimeInMillis();
    }
    public long getToday() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY,0);
        ca.set(Calendar.MINUTE,0);
        ca.set(Calendar.SECOND,0);
        ca.set(Calendar.MILLISECOND,0);
//        DateUtil.dateFormat(ca.getTimeInMillis(),Constant.TimeString.YEAR_MONTH_DAY + Constant.TimeString.START_TIME);
//        DateUtil.dateFormat(,Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC);
        return ca.getTimeInMillis();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent){
        refreshListController.refreshBegin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void listCommonObjSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listCommonObjFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }
}
