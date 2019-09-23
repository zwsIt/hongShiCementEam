package com.supcon.mes.module_score.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.TextUtils;
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
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.EamAPI;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.contract.EamContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.presenter.EamPresenter;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.IntentRouter;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.api.ScoreEamListAPI;
import com.supcon.mes.module_score.model.api.ScoreEamPerformanceAPI;
import com.supcon.mes.module_score.model.api.ScoreSubmitAPI;
import com.supcon.mes.module_score.model.bean.ScoreDeviceCheckResultEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamListEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceEntity;
import com.supcon.mes.module_score.model.contract.ScoreEamListContract;
import com.supcon.mes.module_score.model.contract.ScoreEamPerformanceContract;
import com.supcon.mes.module_score.model.contract.ScoreSubmitContract;
import com.supcon.mes.module_score.model.network.ScoreHttpClient;
import com.supcon.mes.module_score.presenter.ScoreEamListPresenter;
import com.supcon.mes.module_score.presenter.ScoreEamPerformancePresenter;
import com.supcon.mes.module_score.presenter.ScoreSubmitPresenter;
import com.supcon.mes.module_score.ui.adapter.ScoreEamPerformanceAdapter;
import com.supcon.mes.module_score.ui.util.ScoreMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 设备评分
 */
@Router(value = Constant.Router.SCORE_EAM_PERFORMANCE)
@Presenter(value = {ScoreEamListPresenter.class, ScoreEamPerformancePresenter.class, ScoreSubmitPresenter.class, EamPresenter.class})
public class ScoreEamPerformanceActivity extends BaseRefreshRecyclerActivity implements ScoreEamPerformanceContract.View, ScoreSubmitContract.View, EamContract.View, ScoreEamListContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("eamCode")
    CustomVerticalTextView eamCode;
    @BindByTag("eamName")
    CustomVerticalTextView eamName;
    @BindByTag("eamDept")
    CustomVerticalTextView eamDept;
    @BindByTag("eamScore")
    CustomVerticalTextView eamScore;
    @BindByTag("scoreStaff")
    CustomVerticalTextView scoreStaff;
    @BindByTag("scoreTime")
    CustomVerticalTextView scoreTime;

    private ScoreEamPerformanceAdapter scoreEamPerformanceAdapter;
    private ScoreEamEntity scoreEamEntity;
    private int scoreId = -1;
    private boolean isEdit;
    private NFCHelper nfcHelper;
    private String eamCodeStr;
    private String scoreTableNo;

    @Override
    protected IListAdapter createAdapter() {
        scoreEamPerformanceAdapter = new ScoreEamPerformanceAdapter(this);
        return scoreEamPerformanceAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_score_eam_performance;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        scoreTableNo = getIntent().getStringExtra(Constant.IntentKey.SCORETABLENO);
        scoreEamEntity = (ScoreEamEntity) getIntent().getSerializableExtra(Constant.IntentKey.SCORE_ENTITY);
        isEdit = getIntent().getBooleanExtra(Constant.IntentKey.isEdit, false);

        nfcHelper = NFCHelper.getInstance();
        if (nfcHelper != null) {
            nfcHelper.setup(this);
            nfcHelper.setOnNFCListener(new NFCHelper.OnNFCListener() {
                @Override
                public void onNFCReceived(String nfc) {
                    LogUtil.d("NFC Received : " + nfc);
                    EventBus.getDefault().post(new NFCEvent(nfc));
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        scoreEamPerformanceAdapter.setEditable(isEdit);
        rightBtn.setImageResource(R.drawable.sl_top_submit);
        eamCode.setEditable(isEdit);
        eamName.setEditable(isEdit);
        if (isEdit) {
            rightBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        update();
    }

    private void update() {
        Spanned title = HtmlParser.buildSpannedText(String.format(getString(R.string.device_style7), "设备评分表",
                scoreEamEntity != null ? "(" + scoreEamEntity.scoreTableNo + ")" : ""), new HtmlTagHandler());
        titleText.setText(title);
        if (scoreEamEntity != null) {
            scoreId = scoreEamEntity.id;
            eamCode.setContent(Util.strFormat(scoreEamEntity.getBeamId().code));
            eamName.setContent(Util.strFormat(scoreEamEntity.getBeamId().name));
            eamDept.setContent(Util.strFormat(scoreEamEntity.getBeamId().getUseDept().name));
        } else {
            scoreEamEntity = new ScoreEamEntity();
            scoreEamEntity.scoreStaff = new Staff();
            scoreEamEntity.scoreStaff.name = EamApplication.getAccountInfo().staffName;
            scoreEamEntity.scoreStaff.code = EamApplication.getAccountInfo().staffCode;
            scoreEamEntity.scoreStaff.id = EamApplication.getAccountInfo().staffId;
        }
        scoreEamEntity.scoreTime = scoreEamEntity.scoreTime != null ? scoreEamEntity.scoreTime : getYesterday();
        eamScore.setContent(Util.big0(scoreEamEntity.scoreNum));
        scoreStaff.setContent(scoreEamEntity.getScoreStaff().name);
        scoreTime.setContent(DateUtil.dateFormat(scoreEamEntity.scoreTime));
        scoreEamPerformanceAdapter.updateTotal(scoreEamEntity.scoreNum);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TextUtils.isEmpty(scoreTableNo)) {
                    if (!TextUtils.isEmpty(eamCodeStr)) {
                        Map<String, Object> params = new HashMap<>();
                        params.put(Constant.IntentKey.EAM_CODE, eamCodeStr);
                        presenterRouter.create(EamAPI.class).getEam(params, 1);
                    }
                    presenterRouter.create(ScoreEamPerformanceAPI.class).getScorPerformance(scoreId);
                } else {
                    Map<String, Object> queryParam = new HashMap<>();
                    queryParam.put(Constant.BAPQuery.SCORE_TABLE_NO, scoreTableNo);
                    presenterRouter.create(ScoreEamListAPI.class).getScoreList(queryParam, 1);
                }
            }
        });
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> new CustomDialog(context)
                        .twoButtonAlertDialog("确定提交设备评分吗?")
                        .bindView(R.id.redBtn, "确定")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, v12 -> {
                            if (scoreEamEntity.beamId == null) {
                                ToastUtils.show(ScoreEamPerformanceActivity.this, "请选择设备进行评分!");
                                return;
                            }
                            onLoading("正在处理中...");
                            doSubmit();
                        }, true)
                        .bindClickListener(R.id.grayBtn, null, true)
                        .show());
        eamCode.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                scoreEamEntity.beamId = null;
                eamCode.setContent("");
                eamName.setContent("");
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
            IntentRouter.go(ScoreEamPerformanceActivity.this, Constant.Router.EAM, bundle);
        });
        eamName.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    scoreEamEntity.beamId = null;
                    eamCode.setContent("");
                    eamName.setContent("");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
                IntentRouter.go(ScoreEamPerformanceActivity.this, Constant.Router.EAM, bundle);
            }
        });
        scoreEamPerformanceAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                ScoreEamPerformanceEntity scoreEamPerformanceEntity = (ScoreEamPerformanceEntity) obj;
                scoreEamEntity.scoreNum = scoreEamPerformanceEntity.scoreNum;
                eamScore.setContent(Util.big0(scoreEamEntity.scoreNum));
                scoreEamPerformanceAdapter.updateTotal(scoreEamEntity.scoreNum);
            }
        });
    }

    /**
     * @param
     * @description NFC事件
     * @author zhangwenshuai1
     * @date 2018/6/28
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCEvent nfcEvent) {
        if (isEdit) {
            LogUtil.d("NFC_TAG", nfcEvent.getNfc());
            Map<String, Object> nfcJson = Util.gsonToMaps(nfcEvent.getNfc());
            if (nfcJson.get("textRecord") == null) {
                ToastUtils.show(context, "标签内容空！");
                return;
            }
            scoreId = -1;
            eamCodeStr = (String) nfcJson.get("textRecord");
            refreshListController.refreshBegin();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {

        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity != null) {
            if (commonSearchEvent.commonSearchEntity instanceof EamType) {
                loaderController.showLoader("正在确认设备评分情况...");
                EamType eamType = (EamType) commonSearchEvent.commonSearchEntity;
                ScoreHttpClient.doCheckDevice(eamType.id, scoreTime.getContent())
                        .onErrorReturn(throwable -> {
                            ScoreDeviceCheckResultEntity scoreDeviceCheckResultEntity = new ScoreDeviceCheckResultEntity();
                            scoreDeviceCheckResultEntity.success = false;
                            scoreDeviceCheckResultEntity.msg = throwable.toString();
                            return scoreDeviceCheckResultEntity;
                        })
                        .subscribe(scoreDeviceCheckResultEntity -> {
                            if (scoreDeviceCheckResultEntity.success) {
                                loaderController.closeLoader();
                                eamCode.setContent(Util.strFormat(eamType.code));
                                eamName.setContent(Util.strFormat(eamType.name));
                                eamDept.setContent(Util.strFormat(eamType.getUseDept().name));
                                scoreEamEntity.beamId = eamType;
                            } else {
                                loaderController.showMsgAndclose(scoreDeviceCheckResultEntity.msg, false, 1500);
                            }
                        });
            }
        }
    }

    @Override
    public void getEamSuccess(CommonListEntity entity) {
        if (entity.result.size() > 0) {
            EamType eamType = (EamType) entity.result.get(0);
            eamCode.setContent(Util.strFormat(eamType.code));
            eamName.setContent(Util.strFormat(eamType.name));
            eamDept.setContent(Util.strFormat(eamType.getUseDept().name));
            scoreEamEntity.beamId = eamType;
            return;
        }
        SnackbarHelper.showError(rootView, "未查询到设备：" + eamCodeStr);
    }

    @Override
    public void getEamFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getScorPerformanceSuccess(List entity) {
        Flowable.fromIterable(((List<ScoreEamPerformanceEntity>) entity))
                .filter(scorePerformanceTitleEntity -> {
                    if (scorePerformanceTitleEntity.viewType == ListType.TITLE.value()) {
                        return true;
                    }
                    return false;
                })
                .subscribe(scorePerformanceTitleEntity -> {
                    if (scorePerformanceTitleEntity.scoreStandard.contains("设备运转率")) {
                        scorePerformanceTitleEntity.setTotalScore(scoreEamEntity.operationRate);
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("高质量运行")) {
                        scorePerformanceTitleEntity.setTotalScore(scoreEamEntity.highQualityOperation);
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("安全防护")) {
                        scorePerformanceTitleEntity.setTotalScore(scoreEamEntity.security);
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("外观标识")) {
                        scorePerformanceTitleEntity.setTotalScore(scoreEamEntity.appearanceLogo);
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("设备卫生")) {
                        scorePerformanceTitleEntity.setTotalScore(scoreEamEntity.beamHeath);
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("档案管理")) {
                        scorePerformanceTitleEntity.setTotalScore(scoreEamEntity.beamEstimate);
                    }
                    refreshListController.refreshComplete(entity);
                });
    }

    @Override
    public void getScorPerformanceFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    @Override
    public void getScoreListSuccess(ScoreEamListEntity entity) {
        if (entity.result != null && !TextUtils.isEmpty(scoreTableNo) && entity.result.size() == 1) {
            scoreEamEntity = entity.result.get(0);
            update();
            presenterRouter.create(ScoreEamPerformanceAPI.class).getScorPerformance(scoreId);
        } else {
            refreshListController.refreshComplete(null);
        }
    }

    @Override
    public void getScoreListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    private void doSubmit() {
        ScoreMapManager.dataChange(scoreEamPerformanceAdapter.getList(), scoreEamEntity);
        Map map = ScoreMapManager.createMap(scoreEamEntity);

        List list1 = ScoreMapManager.dataChange(scoreEamPerformanceAdapter.getList(), "设备运转率");
        map.put("dg1559647635248ModelCode", "BEAM_1.0.0_scorePerformance_OperateRate");
        map.put("dg1559647635248ListJson", list1.toString());
        map.put("dgLists['dg1559647635248']", list1.toString());

        List list2 = ScoreMapManager.dataChange(scoreEamPerformanceAdapter.getList(), "高质量运行");
        map.put("dg1559291491380ModelCode", "BEAM_1.0.0_scorePerformance_HighQualityRun");
        map.put("dg1559291491380ListJson", list2.toString());
        map.put("dgLists['dg1559291491380']", list2.toString());

        List list3 = ScoreMapManager.dataChange(scoreEamPerformanceAdapter.getList(), "安全防护");
        map.put("dg1559560003995ModelCode", "BEAM_1.0.0_scorePerformance_Security");
        map.put("dg1559560003995ListJson", list3.toString());
        map.put("dgLists['dg1559560003995']", list3.toString());

        List list4 = ScoreMapManager.dataChange(scoreEamPerformanceAdapter.getList(), "外观标识");
        map.put("dg1559619724464ModelCode", "BEAM_1.0.0_scorePerformance_OutwardMark");
        map.put("dg1559619724464ListJson", list4.toString());
        map.put("dgLists['dg1559619724464']", list4.toString());

        List list5 = ScoreMapManager.dataChange(scoreEamPerformanceAdapter.getList(), "设备卫生");
        map.put("dg1559631064719ModelCode", "BEAM_1.0.0_scorePerformance_Health");
        map.put("dg1559631064719ListJson", list5.toString());
        map.put("dgLists['dg1559631064719']", list5.toString());

        List list6 = ScoreMapManager.dataChange(scoreEamPerformanceAdapter.getList(), "档案管理");
        map.put("dg1559636766020ModelCode", "BEAM_1.0.0_scorePerformance_OutwardMark");
        map.put("dg1559636766020ListJson", list6.toString());
        map.put("dgLists['dg1559636766020']", list6.toString());

        map.put("operateType", Constant.Transition.SUBMIT);
        presenterRouter.create(ScoreSubmitAPI.class).doSubmit(map);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取到Tag对象
        if (nfcHelper != null)
            nfcHelper.dealNFCTag(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcHelper != null)
            nfcHelper.onPauseNFC(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (nfcHelper != null) {
            nfcHelper.release();
        }
    }

    public long getYesterday() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DAY_OF_MONTH, -1);
        return ca.getTimeInMillis();
    }

    @Override
    public void doSubmitSuccess(BapResultEntity entity) {
        onLoadSuccessAndExit("评分成功", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                EventBus.getDefault().post(new RefreshEvent());
                finish();
            }
        });
    }

    @Override
    public void doSubmitFailed(String errorMsg) {
        if ("本数据已经被其他人修改或删除，请刷页面后重试".equals(errorMsg)) {
            loaderController.showMsgAndclose(ErrorMsgHelper.msgParse(errorMsg), false, 5000);
        } else {
            onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
        }
    }

    @Override
    public void back() {
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }
}
