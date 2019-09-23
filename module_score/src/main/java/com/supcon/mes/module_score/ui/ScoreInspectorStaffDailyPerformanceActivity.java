package com.supcon.mes.module_score.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.IntentRouter;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.api.ScoreInspectorStaffDailyPerformanceAPI;
import com.supcon.mes.module_score.model.api.ScoreStaffSubmitAPI;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;
import com.supcon.mes.module_score.model.contract.ScoreInspectorStaffDailyPerformanceContract;
import com.supcon.mes.module_score.model.contract.ScoreStaffSubmitContract;
import com.supcon.mes.module_score.presenter.ScoreInspectorStaffDailyPerformancePresenter;
import com.supcon.mes.module_score.presenter.ScoreInspectorStaffSubmitPresenter;
import com.supcon.mes.module_score.ui.adapter.ScoreStaffDailyPerformanceAdapter;
import com.supcon.mes.module_score.ui.util.ScoreMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 巡检工每日评分
 */
@Router(value = Constant.Router.SCORE_INSPECTOR_STAFF_DAILY_PERFORMANCE)
@Presenter(value = {ScoreInspectorStaffDailyPerformancePresenter.class, ScoreInspectorStaffSubmitPresenter.class})
public class ScoreInspectorStaffDailyPerformanceActivity extends BaseRefreshRecyclerActivity implements ScoreInspectorStaffDailyPerformanceContract.View, ScoreStaffSubmitContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("scoreStaff")
    CustomVerticalTextView scoreStaff;
    @BindByTag("staffScore")
    CustomVerticalTextView staffScore;
    @BindByTag("scoreTime")
    CustomTextView scoreTime;

    private ScoreStaffDailyPerformanceAdapter scoreStaffPerformanceAdapter;
    private ScoreStaffEntity scoreStaffEntity;
    private boolean isEdit;
    private float avgScore = 30;//设备平均分

    @Override
    protected int getLayoutID() {
        return R.layout.ac_score_staff_daily_performance;
    }
    @Override
    protected IListAdapter createAdapter() {
        scoreStaffPerformanceAdapter = new ScoreStaffDailyPerformanceAdapter(this);
        return scoreStaffPerformanceAdapter;
    }
    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        scoreStaffEntity = (ScoreStaffEntity) getIntent().getSerializableExtra(Constant.IntentKey.SCORE_ENTITY);
        isEdit = getIntent().getBooleanExtra(Constant.IntentKey.isEdit, false);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));

        scoreStaffPerformanceAdapter.setEditable(isEdit);

        rightBtn.setImageResource(R.drawable.sl_top_submit);
        scoreStaff.setEditable(isEdit);
        if (isEdit) {
            rightBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        Spanned title = HtmlParser.buildSpannedText(String.format(getString(R.string.device_style7), "巡检工评分表", ""), new HtmlTagHandler());
        titleText.setText(title);
        if (scoreStaffEntity == null) {
            scoreStaffEntity = new ScoreStaffEntity();
            scoreStaffEntity.patrolWorker = new Staff();
            scoreStaffEntity.patrolWorker.name = EamApplication.getAccountInfo().staffName;
            scoreStaffEntity.patrolWorker.code = EamApplication.getAccountInfo().staffCode;
            scoreStaffEntity.patrolWorker.id = EamApplication.getAccountInfo().staffId;
        } else {
            scoreStaff.setEnabled(false);
        }
        scoreStaff.setKey("巡检工");
        scoreStaff.setContent(scoreStaffEntity.getPatrolWorker().name);
        staffScore.setContent(Util.big0(scoreStaffEntity.score));
        scoreStaffPerformanceAdapter.updateTotal(scoreStaffEntity.score - avgScore);
        scoreStaffEntity.scoreData = (scoreStaffEntity.scoreData != null) ? scoreStaffEntity.scoreData : System.currentTimeMillis();
        scoreTime.setContent(DateUtil.dateFormat(scoreStaffEntity.scoreData));

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(ScoreInspectorStaffDailyPerformanceAPI.class).getInspectorStaffDailyScore(scoreStaffEntity.id);
            }
        });
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> new CustomDialog(context)
                        .twoButtonAlertDialog("确定提交巡检工评分吗?")
                        .bindView(R.id.redBtn, "确定")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, v12 -> {
                            if (scoreStaffEntity.patrolWorker == null) {
                                ToastUtils.show(this, "请选择巡检工!");
                                return;
                            }
                            onLoading("正在处理中...");
                            doSubmit();
                        }, true)
                        .bindClickListener(R.id.grayBtn, null, true)
                        .show());

        scoreStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    scoreStaffEntity.patrolWorker = null;
                    scoreStaffEntity.id = -1;
                }
                IntentRouter.go(ScoreInspectorStaffDailyPerformanceActivity.this, Constant.Router.STAFF);
            }
        });

        scoreStaffPerformanceAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                ScoreStaffPerformanceEntity scoreStaffPerformanceEntity = (ScoreStaffPerformanceEntity) obj;
                scoreStaffEntity.score = scoreStaffPerformanceEntity.scoreNum + avgScore;
                staffScore.setContent(Util.big0(scoreStaffEntity.score));
                scoreStaffPerformanceAdapter.updateTotal(scoreStaffEntity.score - avgScore);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {

        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity != null) {
            if (commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff) {
                CommonSearchStaff searchStaff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;
                scoreStaff.setContent(Util.strFormat(searchStaff.name));
                Staff staff = new Staff();
                staff.id = searchStaff.id;
                staff.name = searchStaff.name;
                staff.code = searchStaff.code;
                scoreStaffEntity.patrolWorker = staff;
                refreshListController.refreshBegin();
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void getInspectorStaffDailyScoreSuccess(List entity) {
        refreshListController.refreshComplete(entity);
    }

    @Override
    public void getInspectorStaffDailyScoreFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        SnackbarHelper.showError(rootView, errorMsg);
    }

    private void doSubmit() {

        Map map = ScoreMapManager.createMap(scoreStaffEntity);

        map.put("viewselect", "patrolScoreEdit");
        map.put("datagridKey", "BEAM_patrolWorkerScore_workerScoreHead_patrolScoreEdit_datagrids");
        map.put("viewCode", "BEAM_1.0.0_patrolWorkerScore_patrolScoreEdit");

        map.put("workerScoreHead.scoreType.id", "BEAM_065/02");
        map.put("workerScoreHead.scoreType.value", "巡检工个人评分");

//        List list1 = ScoreMapManager.dataStaffChange(scoreStaffPerformanceAdapter.getList(), "设备运行");
//        map.put("dg1560145365044ModelCode", "BEAM_1.0.0_patrolWorkerScore_Running");
//        map.put("dg1560145365044ListJson", list1.toString());
//        map.put("dgLists['dg1560145365044']", list1.toString());
//
//        List list2 = ScoreMapManager.dataStaffChange(scoreStaffPerformanceAdapter.getList(), "规范化管理");
//        map.put("dg1560222990407ModelCode", "BEAM_1.0.0_patrolWorkerScore_Standardized");
//        map.put("dg1560222990407ListJson", list2.toString());
//        map.put("dgLists['dg1560222990407']", list2.toString());
//
//        List list3 = ScoreMapManager.dataStaffChange(scoreStaffPerformanceAdapter.getList(), "安全生产");
//        map.put("dg1560223948889ModelCode", "BEAM_1.0.0_patrolWorkerScore_SafeProduction");
//        map.put("dg1560223948889ListJson", list3.toString());
//        map.put("dgLists['dg1560223948889']", list3.toString());
//
//        List list4 = ScoreMapManager.dataStaffChange(scoreStaffPerformanceAdapter.getList(), "工作表现");
//        map.put("dg1560224145331ModelCode", "BBEAM_1.0.0_patrolWorkerScore_WorkePerformance");
//        map.put("dg1560224145331ListJson", list4.toString());
//        map.put("dgLists['dg1560224145331']", list4.toString());
//

        map.put("operateType", Constant.Transition.SUBMIT);
        String url = "/BEAM/patrolWorkerScore/workerScoreHead/patrolScoreEdit/submit.action?__pc__" +
                "=cGF0cm9sU2NvcmVfYWRkX2FkZF9CRUFNXzEuMC4wX3BhdHJvbFdvcmtlclNjb3JlX3BhdHJvbFNjb3JlfA__&_bapFieldPermissonModelCode_" +
                "=BEAM_1.0.0_patrolWorkerScore_WorkerScoreHead&_bapFieldPermissonModelName_=WorkerScoreHead";
        presenterRouter.create(ScoreStaffSubmitAPI.class).doStaffSubmit(url, map);
    }


    public long getYesterday() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DAY_OF_MONTH, -1);
        return ca.getTimeInMillis();
    }

    @Override
    public void doStaffSubmitSuccess(BapResultEntity entity) {
        onLoadSuccessAndExit("评分成功", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                EventBus.getDefault().post(new RefreshEvent());
                finish();
            }
        });
    }

    @Override
    public void doStaffSubmitFailed(String errorMsg) {
        if ("本数据已经被其他人修改或删除，请刷页面后重试".equals(errorMsg)) {
            loaderController.showMsgAndclose(ErrorMsgHelper.msgParse(errorMsg), false, 5000);
        } else if (errorMsg.contains("com.supcon.orchid.BEAM2.entities.BEAM2SparePart")) {
            //error : Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [com.supcon.orchid.BEAM2.entities.BEAM2SparePart#1211]
            loaderController.showMsgAndclose("请刷新备件表体数据！", false, 4000);
        } else {
            onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void back() {
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }


}
