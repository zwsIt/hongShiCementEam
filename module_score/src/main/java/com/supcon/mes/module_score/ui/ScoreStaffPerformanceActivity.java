package com.supcon.mes.module_score.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
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
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
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
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.MyPickerController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;
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
import com.supcon.mes.module_score.model.api.ScoreInspectorStaffPerformanceAPI;
import com.supcon.mes.module_score.model.api.ScoreStaffPerformanceAPI;
import com.supcon.mes.module_score.model.api.ScoreStaffSubmitAPI;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceListEntity;
import com.supcon.mes.module_score.model.contract.ScoreInspectorStaffPerformanceContract;
import com.supcon.mes.module_score.model.contract.ScoreStaffPerformanceContract;
import com.supcon.mes.module_score.model.contract.ScoreStaffSubmitContract;
import com.supcon.mes.module_score.model.dto.ScoreStaffDto;
import com.supcon.mes.module_score.presenter.ScoreInspectorStaffPerformancePresenter;
import com.supcon.mes.module_score.presenter.ScoreInspectorStaffSubmitPresenter;
import com.supcon.mes.module_score.presenter.ScoreStaffPerformancePresenter;
import com.supcon.mes.module_score.ui.adapter.ScoreStaffPerformanceAdapter;
import com.supcon.mes.module_score.ui.adapter.ScoreStaffPerformanceNewAdapter;
import com.supcon.mes.module_score.ui.util.ScoreMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * ScoreStaffPerformanceActivity
 * created by zhangwenshuai1 2020/8/5
 * 绩效评分
 */
@Router(value = Constant.Router.SCORE_STAFF_PERFORMANCE)
@Presenter(value = {ScoreStaffPerformancePresenter.class, ScoreInspectorStaffSubmitPresenter.class})
@Controller(value = {ScoreCameraController.class})
public class ScoreStaffPerformanceActivity extends BaseRefreshRecyclerActivity<ScoreStaffPerformanceEntity> implements ScoreStaffPerformanceContract.View, ScoreStaffSubmitContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("recyclerViewEam")
    RecyclerView recyclerViewEam; // 设备责任到人

    @BindByTag("scoreStaff")
    CustomVerticalTextView scoreStaff;
    @BindByTag("staffScore")
    CustomVerticalTextView staffScore;
    @BindByTag("scoreTime")
    CustomDateView scoreTime;
    @BindByTag("submitBtn")
    Button submitBtn;

    /**
     * 日期控制器
     */
    private MyPickerController mDatePickController;
    private long longScoreTime;
    private ScoreStaffPerformanceNewAdapter scoreStaffPerformanceAdapter;
    private ScoreStaffEntity scoreStaffEntity;
    private ScoreStaffEntity oriScoreStaffEntity;
    private boolean isEdit,update;
//    private ScoreStaffEamAdapter scoreStaffEamAdapter;
//    private float avgScore = 0;// 设备平均分

    public ScoreStaffEntity getScoreStaffEntity() {
        return scoreStaffEntity;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_score_staff_performance;
    }
    @Override
    protected IListAdapter<ScoreStaffPerformanceEntity> createAdapter() {
        scoreStaffPerformanceAdapter = new ScoreStaffPerformanceNewAdapter(this);
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
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,""));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.setAdapter(scoreStaffPerformanceAdapter);
        scoreStaffPerformanceAdapter.setEditable(isEdit);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        rightBtn.setImageResource(R.drawable.ic_record);

        initDatePickController();

        scoreStaff.setEditable(isEdit);
        if (isEdit) {
            submitBtn.setVisibility(View.VISIBLE);
        }

        Spanned title = HtmlParser.buildSpannedText(String.format(getString(R.string.device_style7), "员工评分表", ""), new HtmlTagHandler());
        titleText.setText(title);
        if (scoreStaffEntity == null) {
            scoreStaffEntity = new ScoreStaffEntity();
            scoreStaffEntity.patrolWorker = new Staff();

            scoreStaffEntity.scoreStaff = new Staff();
            scoreStaffEntity.scoreStaff.name = EamApplication.getAccountInfo().staffName;
            scoreStaffEntity.scoreStaff.code = EamApplication.getAccountInfo().staffCode;
            scoreStaffEntity.scoreStaff.id = EamApplication.getAccountInfo().staffId;
        } else {
            scoreStaff.setEditable(false);
            rightBtn.setVisibility(View.VISIBLE);
        }
        scoreStaff.setKey("员工");
        scoreStaff.setContent(scoreStaffEntity.getPatrolWorker().name);
        staffScore.setContent(Util.big0(scoreStaffEntity.score));
        scoreStaffPerformanceAdapter.updateTotal(scoreStaffEntity.score);
        scoreStaffEntity.scoreData = (scoreStaffEntity.scoreData != null) ? scoreStaffEntity.scoreData : System.currentTimeMillis();
//        scoreTime.setContent(DateUtil.dateFormat(scoreStaffEntity.scoreData));
        Calendar calendar = Calendar.getInstance();
        scoreTime.setDate(DateUtil.dateFormat(DateUtil.dateFormat(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH),
                "yyyy-MM-dd"), "yyyy-MM-dd"));
        scoreStaffEntity.beforeScore = new BigDecimal(scoreStaffEntity.score).floatValue();
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
                presenterRouter.create(ScoreStaffPerformanceAPI.class).getStaffScore(scoreStaffEntity.getPatrolWorker().id, scoreStaffEntity.id);
            }
        });
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
        RxView.clicks(rightBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.BAPQuery.DATE,scoreStaffEntity.scoreData);
                        bundle.putLong(Constant.BAPQuery.ID,scoreStaffEntity.patrolWorker.id);
                        IntentRouter.go(context,Constant.Router.SCORE_MODIFY_LIST,bundle);
                    }
                });
        RxView.clicks(submitBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(Object o) throws Exception {
                        if (scoreStaffPerformanceAdapter.getItemCount() <= 0 ){
                            ToastUtils.show(context,"列表无数据可评分");
                            return false;
                        }
                        return true;
                    }
                })
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
                IntentRouter.go(ScoreStaffPerformanceActivity.this, Constant.Router.CONTACT_SELECT, bundle);
            }
        });

        scoreStaffPerformanceAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                updateTotalScore((ScoreStaffPerformanceEntity) obj);
            }
        });
        scoreTime.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
            } else {
                mDatePickController
                        .listener((year, month, day, hour, minute, second) -> {
                            String dateStr = year + "-" + month + "-" + day;
                            longScoreTime = DateUtil.dateFormat(dateStr, "yyyy-MM-dd");
                            scoreTime.setDate(DateUtil.dateFormat(longScoreTime, "yyyy-MM-dd"));
                            scoreStaffEntity.scoreData = longScoreTime;
                        })
                        .show(longScoreTime);
            }
        });
    }
    private void initDatePickController() {
        mDatePickController = new MyPickerController((ScoreStaffPerformanceActivity) context);
        mDatePickController.textSize(18);
        mDatePickController.setCycleDisable(false);
        mDatePickController.setSecondVisible(true);
        mDatePickController.setCanceledOnTouchOutside(true);
    }

    public void updateTotalScore(ScoreStaffPerformanceEntity scoreStaffPerformanceEntity) {
        scoreStaffEntity.score = scoreStaffPerformanceEntity.scoreNum;
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
                oriScoreStaffEntity.score = 100f;
                refreshListController.refreshBegin();
            }
        }
    }


    @SuppressLint("CheckResult")
    @Override
    @Deprecated
    public void getDutyEamSuccess(ScoreDutyEamEntity entity) {
        scoreStaffEntity.score = oriScoreStaffEntity.score;
        Flowable.fromIterable(scoreStaffPerformanceAdapter.getList())
                .filter(scoreStaffPerformanceEntity -> {
                    if (scoreStaffPerformanceEntity.viewType == ListType.TITLE.value() && "专业巡检".equals(scoreStaffPerformanceEntity.project)) {
//                        scoreStaffPerformanceEntity.score = scoreStaffPerformanceEntity.fraction;
                        scoreStaffPerformanceEntity.fraction = entity.professInspScore;
                        return true;
                    }else if (scoreStaffPerformanceEntity.viewType == ListType.TITLE.value() && "设备责任到人(现场管理、设备运行)".equals(scoreStaffPerformanceEntity.project)) {
//                        scoreStaffPerformanceEntity.score = scoreStaffPerformanceEntity.fraction;
                        scoreStaffPerformanceEntity.fraction = entity.avgScore;
                        return true;
                    }
                    return false;
                })
                .subscribe(scoreStaffPerformanceEntity -> {
                    scoreStaffPerformanceAdapter.notifyItemChanged(scoreStaffPerformanceAdapter.getList().indexOf(scoreStaffPerformanceEntity));// 更新标题
                    if (!isEdit || update)return;
                    scoreStaffPerformanceEntity.scoreNum = scoreStaffEntity.score - scoreStaffPerformanceEntity.score + scoreStaffPerformanceEntity.fraction; //总分
                    updateTotalScore(scoreStaffPerformanceEntity);
                });

//        if (entity.result != null && entity.result.size() > 0) {
//            ScoreDutyEamEntity scoreDutyEamEntity = (ScoreDutyEamEntity) entity.result.get(0);
//            ScoreDutyEamEntity scoreDutyEamEntityTitle = new ScoreDutyEamEntity();
//            scoreDutyEamEntityTitle.name = "设备责任到人";
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
    public void getStaffScoreSuccess(ScoreStaffPerformanceListEntity entity) {
        // 新增时，获取评分模板id
        if (scoreStaffEntity.id == -1 && entity.result != null && entity.result.size() > 0){
            ScoreStaffEntity.IdEntity soringId = new ScoreStaffEntity.IdEntity();
            soringId.id = entity.result.get(0).scoringId;
            scoreStaffEntity.soringId = soringId;
        }
        // 获取自动评分项目
        scoreStaffEntity.score = oriScoreStaffEntity.score;
        if (entity.result != null && entity.result.size() > 0 && isEdit && !update){
            String category = "";
            for (ScoreStaffPerformanceEntity performanceEntity : entity.result){
                // 自动评分
                if (performanceEntity.scoreType != null && performanceEntity.scoreType.id.equals(ScoreConstant.ScoreItemType.T1) && !performanceEntity.category.equals(category)){
                    category = performanceEntity.category;
                    performanceEntity.scoreNum = scoreStaffEntity.score - performanceEntity.score + performanceEntity.fraction;
                    updateTotalScore(performanceEntity);
                }
            }
        }

        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getStaffScoreFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    @Deprecated
    public void getDutyEamFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    private void doSubmit() {

        Map map = ScoreMapManager.createMap(scoreStaffEntity);

        map.put("viewselect", "patrolScoreEdit");
        map.put("datagridKey", "BEAM_patrolWorkerScore_workerScoreHead_patrolScoreEdit_datagrids");
        map.put("viewCode", "BEAM_1.0.0_patrolWorkerScore_patrolScoreEdit");

        List list4 = dgScoreStaffDto();
        map.put("dg1560224145331ModelCode", "BBEAM_1.0.0_patrolWorkerScore_WorkePerformance");
        map.put("dg1560224145331ListJson", list4.toString());
        map.put("dgLists['dg1560224145331']", list4.toString());


        map.put("operateType", Constant.Transition.SUBMIT);
        String url = "/BEAM/patrolWorkerScore/workerScoreHead/patrolScoreEdit/submit.action?__pc__" +
                "=cGF0cm9sU2NvcmVfYWRkX2FkZF9CRUFNXzEuMC4wX3BhdHJvbFdvcmtlclNjb3JlX3BhdHJvbFNjb3JlfA__&_bapFieldPermissonModelCode_" +
                "=BEAM_1.0.0_patrolWorkerScore_WorkerScoreHead&_bapFieldPermissonModelName_=WorkerScoreHead";
        presenterRouter.create(ScoreStaffSubmitAPI.class).doStaffSubmit(url, map);
    }

    @SuppressLint("CheckResult")
    public List<ScoreStaffDto> dgScoreStaffDto() {
        List<ScoreStaffPerformanceEntity> scorePerformanceEntities = scoreStaffPerformanceAdapter.getList();
        List<ScoreStaffDto> scorePerformanceDto = new ArrayList<>();
        Flowable.fromIterable(scorePerformanceEntities)
                .filter(scorePerformanceEntity -> scorePerformanceEntity.viewType == ListType.CONTENT.value())
                .subscribe(scorePerformanceEntity -> {
                    ScoreStaffDto scoreEamDto = new ScoreStaffDto();
                    scoreEamDto.id = Util.strFormat2(scorePerformanceEntity.id);
                    scoreEamDto.category = scorePerformanceEntity.category;
                    scoreEamDto.project = scorePerformanceEntity.project;
                    scoreEamDto.score = Util.big(scorePerformanceEntity.score);
                    scoreEamDto.grade = scorePerformanceEntity.grade;
                    scoreEamDto.item = scorePerformanceEntity.item;
                    scoreEamDto.itemScore = Util.big(scorePerformanceEntity.itemScore);
                    scoreEamDto.result = Util.strFormat2(scorePerformanceEntity.result);
                    scoreEamDto.fraction = Util.big(scorePerformanceEntity.scoreEamPerformanceEntity.fraction);
                    scoreEamDto.isItemValue = scorePerformanceEntity.isItemValue;
                    scoreEamDto.noItemValue = scorePerformanceEntity.noItemValue;
                    scoreEamDto.defaultNumVal = scorePerformanceEntity.defaultNumVal > 0 ? Util.strFormat2(scorePerformanceEntity.defaultNumVal) : "";

                    if (scorePerformanceEntity.defaultValueType != null && !TextUtils.isEmpty(scorePerformanceEntity.defaultValueType.id)){
                        ValueEntity valueEntity = new ValueEntity();
                        valueEntity.id = scorePerformanceEntity.defaultValueType.id;
                        scoreEamDto.defaultValueType = valueEntity;
                    }
                    if (scorePerformanceEntity.scoreType != null && !TextUtils.isEmpty(scorePerformanceEntity.scoreType.id)){
                        ValueEntity valueEntity = new ValueEntity();
                        valueEntity.id = scorePerformanceEntity.scoreType.id;
                        scoreEamDto.scoreType = valueEntity;
                    }
                    scoreEamDto.attachFileFileAddPaths = scorePerformanceEntity.getAttachFileFileAddPaths();
                    scoreEamDto.attachFileMultiFileIds = scorePerformanceEntity.getAttachFileMultiFileIds();
                    scoreEamDto.attachFileFileDeleteIds = scorePerformanceEntity.getAttachFileFileDeleteIds();
                    scoreEamDto.subScore = scorePerformanceEntity.subScore;
                    scorePerformanceDto.add(scoreEamDto);
                });
        return scorePerformanceDto;
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
