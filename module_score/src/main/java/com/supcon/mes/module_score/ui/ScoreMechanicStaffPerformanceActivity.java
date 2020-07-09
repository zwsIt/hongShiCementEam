package com.supcon.mes.module_score.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
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
import com.supcon.mes.module_score.constant.ScoreConstant;
import com.supcon.mes.module_score.controller.ScoreCameraController;
import com.supcon.mes.module_score.model.api.ScoreMechanicStaffPerformanceAPI;
import com.supcon.mes.module_score.model.api.ScoreStaffSubmitAPI;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;
import com.supcon.mes.module_score.model.contract.ScoreMechanicStaffPerformanceContract;
import com.supcon.mes.module_score.model.contract.ScoreStaffSubmitContract;
import com.supcon.mes.module_score.presenter.ScoreInspectorStaffSubmitPresenter;
import com.supcon.mes.module_score.presenter.ScoreMechanicStaffPerformancePresenter;
import com.supcon.mes.module_score.ui.adapter.ScoreStaffEamAdapter;
import com.supcon.mes.module_score.ui.adapter.ScoreStaffPerformanceAdapter;
import com.supcon.mes.module_score.ui.util.ScoreMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 机修工评分
 */
@Router(value = Constant.Router.SCORE_MECHANIC_STAFF_PERFORMANCE)
@Presenter(value = {ScoreMechanicStaffPerformancePresenter.class, ScoreInspectorStaffSubmitPresenter.class})
@Controller(value = {ScoreCameraController.class})
public class ScoreMechanicStaffPerformanceActivity extends BaseRefreshRecyclerActivity<ScoreStaffPerformanceEntity> implements ScoreMechanicStaffPerformanceContract.View, ScoreStaffSubmitContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;

    @BindByTag("contentView")
    RecyclerView contentView;

//    @BindByTag("recyclerViewEam")
//    RecyclerView recyclerViewEam;

    @BindByTag("scoreStaff")
    CustomVerticalTextView scoreStaff;
    @BindByTag("staffScore")
    CustomVerticalTextView staffScore;
    @BindByTag("scoreTime")
    CustomTextView scoreTime;

    private ScoreStaffPerformanceAdapter scoreStaffPerformanceAdapter;
    private ScoreStaffEntity scoreStaffEntity,oriScoreStaffEntity;
    private boolean isEdit,update;
//    private ScoreStaffEamAdapter scoreStaffEamAdapter;
//    private float avgScore = 0;//平均分

    @Override
    protected int getLayoutID() {
        return R.layout.ac_score_staff_performance;
    }

    @Override
    protected IListAdapter<ScoreStaffPerformanceEntity> createAdapter() {
        scoreStaffPerformanceAdapter = new ScoreStaffPerformanceAdapter(this);
        return scoreStaffPerformanceAdapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        scoreStaffEntity = (ScoreStaffEntity) getIntent().getSerializableExtra(Constant.IntentKey.SCORE_ENTITY);
        isEdit = getIntent().getBooleanExtra(Constant.IntentKey.isEdit, false);
        update = getIntent().getBooleanExtra(Constant.IntentKey.UPDATE, false);

        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.setAdapter(scoreStaffPerformanceAdapter);
        scoreStaffPerformanceAdapter.setEditable(isEdit);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
//        recyclerViewEam.setLayoutManager(new LinearLayoutManager(context));
//        scoreStaffEamAdapter = new ScoreStaffEamAdapter(this);
//        recyclerViewEam.setAdapter(scoreStaffEamAdapter);

        rightBtn.setImageResource(R.drawable.sl_top_submit);
        scoreStaff.setEditable(isEdit);
        if (isEdit) {
            rightBtn.setVisibility(View.VISIBLE);
        }
        Spanned title = HtmlParser.buildSpannedText(String.format(getString(R.string.device_style7), "员工评分表", ""), new HtmlTagHandler());
        titleText.setText(title);
        if (scoreStaffEntity == null) {
            scoreStaffEntity = new ScoreStaffEntity();
            scoreStaffEntity.patrolWorker = new Staff();
//            scoreStaffEntity.patrolWorker.name = EamApplication.getAccountInfo().staffName;
//            scoreStaffEntity.patrolWorker.code = EamApplication.getAccountInfo().staffCode;
//            scoreStaffEntity.patrolWorker.id = EamApplication.getAccountInfo().staffId;
        } else {
            scoreStaff.setEditable(false);
        }
        scoreStaff.setKey("员工");
        scoreStaff.setContent(scoreStaffEntity.getPatrolWorker().name);
        staffScore.setContent(Util.big0(scoreStaffEntity.score));
        scoreStaffPerformanceAdapter.updateTotal(scoreStaffEntity.score);
        scoreStaffEntity.scoreData = (scoreStaffEntity.scoreData != null) ? scoreStaffEntity.scoreData : System.currentTimeMillis();
        scoreTime.setContent(DateUtil.dateFormat(scoreStaffEntity.scoreData));
        oriScoreStaffEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(scoreStaffEntity),ScoreStaffEntity.class);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(ScoreMechanicStaffPerformanceAPI.class).getMechanicStaffScore(scoreStaffEntity.id);
//                long staffId = scoreStaffEntity.getPatrolWorker().id != null ? scoreStaffEntity.getPatrolWorker().id : -1;
//                presenterRouter.create(ScoreMechanicStaffPerformanceAPI.class).getDutyEam(staffId, "BEAM_065/03");
            }
        });
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> new CustomDialog(context)
                        .twoButtonAlertDialog("确定提交员工评分吗?")
                        .bindView(R.id.redBtn, "确定")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, v12 -> {
                            if (scoreStaffEntity.patrolWorker == null || scoreStaffEntity.patrolWorker.id == null) {
                                ToastUtils.show(this, "请选择员工!");
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
                    scoreStaffEntity.id = -1L;
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                IntentRouter.go(ScoreMechanicStaffPerformanceActivity.this, Constant.Router.CONTACT_SELECT, bundle);
            }
        });

        scoreStaffPerformanceAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                updateTotalScore((ScoreStaffPerformanceEntity) obj);
            }
        });
    }
    public void updateTotalScore(ScoreStaffPerformanceEntity scoreStaffPerformanceEntity) {
        if (scoreStaffPerformanceEntity != null){
            scoreStaffEntity.score = scoreStaffPerformanceEntity.scoreNum;
        }
        staffScore.setContent(Util.big0(scoreStaffEntity.score));
        scoreStaffPerformanceAdapter.updateTotal(scoreStaffEntity.score);
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
                scoreStaffEntity.score = 100f;
                updateTotalScore(null);
                refreshListController.refreshBegin();
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMechanicStaffScoreSuccess(List entity) {
        refreshListController.refreshComplete(entity);
    }

    @Override
    public void getMechanicStaffScoreFailed(String errorMsg) {
        refreshListController.refreshComplete();
        SnackbarHelper.showError(rootView, errorMsg);
//        contentView.setAdapter((BaseListDataRecyclerViewAdapter) EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
    }

    @SuppressLint("CheckResult")
    @Override
    public void getDutyEamSuccess(CommonListEntity entity) {
//        if (entity.result != null && entity.result.size() > 0) {
//            ScoreDutyEamEntity scoreDutyEamEntity = (ScoreDutyEamEntity) entity.result.get(0);
//            ScoreDutyEamEntity scoreDutyEamEntityTitle = new ScoreDutyEamEntity();
//            scoreDutyEamEntityTitle.name = "现场管理";
//            scoreDutyEamEntityTitle.avgScore = scoreDutyEamEntity.avgScore;
//            scoreDutyEamEntityTitle.viewType = ListType.TITLE.value();
//            entity.result.add(0, scoreDutyEamEntityTitle);
//
//            avgScore = scoreDutyEamEntityTitle.avgScore;
//            if (scoreStaffEntity.score == 100) {
//                staffScore.setContent(Util.big0(70 + scoreDutyEamEntityTitle.avgScore));
//                scoreStaffPerformanceAdapter.updateTotal(70);
//            } else {
//                scoreStaffPerformanceAdapter.updateTotal(scoreStaffEntity.score - avgScore);
//            }
//        }
//        scoreStaffEamAdapter.setList(entity.result);
//        scoreStaffEamAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDutyEamFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
//        scoreStaffEamAdapter.setList(new LinkedList());
//        scoreStaffEamAdapter.notifyDataSetChanged();
    }

    private void doSubmit() {

        Map map = ScoreMapManager.createMap(scoreStaffEntity);
//        if (scoreStaffEamAdapter.getList() != null) {
//            ScoreMapManager.addAvg(scoreStaffEamAdapter.getList(), map);
//        }
        map.put("viewselect", "repairerScoreEdit");
        map.put("datagridKey", "BEAM_patrolWorkerScore_workerScoreHead_repairerScoreEdit_datagrids");
        map.put("viewCode", "BEAM_1.0.0_patrolWorkerScore_repairerScoreEdit");

        map.put("workerScoreHead.scoreType.id", ScoreConstant.ScoreType.EAM_MACHINE_STAFF);
        map.put("workerScoreHead.scoreType.value", "设备科个人评分");

//        List list1 = ScoreMapManager.dataStaffChange(scoreStaffPerformanceAdapter.getList(), "设备运行");
//        map.put("dg1560475480845ModelCode", "BEAM_1.0.0_patrolWorkerScore_Running");
//        map.put("dg1560475480845ListJson", list1.toString());
//        map.put("dgLists['dg1560475480845']", list1.toString());

        List list1 = ScoreMapManager.dataStaffChange(scoreStaffPerformanceAdapter.getList(), "责任到人");
        map.put("dg1592559476763ModelCode", "BEAM_1.0.0_patrolWorkerScore_EamResStaff");
        map.put("dg1592559476763ListJson", list1.toString());
        map.put("dgLists['dg1592559476763']", list1.toString());

        List list2 = ScoreMapManager.dataStaffChange(scoreStaffPerformanceAdapter.getList(), "规范化管理");
        map.put("dg1560475480876ModelCode", "BEAM_1.0.0_patrolWorkerScore_Standardized");
        map.put("dg1560475480876ListJson", list2.toString());
        map.put("dgLists['dg1560475480876']", list2.toString());

        List list3 = ScoreMapManager.dataStaffChange(scoreStaffPerformanceAdapter.getList(), "安全生产");
        map.put("dg1560475480892ModelCode", "BEAM_1.0.0_patrolWorkerScore_SafeProduction");
        map.put("dg1560475480892ListJson", list3.toString());
        map.put("dgLists['dg1560475480892']", list3.toString());

        List list4 = ScoreMapManager.dataStaffChange(scoreStaffPerformanceAdapter.getList(), "工作表现");
        map.put("dg1560475480986ModelCode", "BBEAM_1.0.0_patrolWorkerScore_WorkePerformance");
        map.put("dg1560475480986ListJson", list4.toString());
        map.put("dgLists['dg1560475480986']", list4.toString());


        map.put("operateType", Constant.Transition.SUBMIT);

        String url = "/BEAM/patrolWorkerScore/workerScoreHead/repairerScoreEdit/submit.action?__pc__" +
                "=cmVwYWlyZXJTY29yZUxpc3RfYWRkX2FkZF9CRUFNXzEuMC4wX3BhdHJvbFdvcmtlclNjb3JlX3JlcGFpcmVyU2NvcmVMaXN0fA__&_bapFieldPermissonModelCode_" +
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
        if ("本数据已经被其他人修改或删除，请刷新页面后重试".equals(errorMsg)) {
            loaderController.showMsgAndclose(ErrorMsgHelper.msgParse(errorMsg), false, 5000);
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
