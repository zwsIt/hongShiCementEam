package com.supcon.mes.module_warn.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.IntentRouter;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.api.CompleteAPI;
import com.supcon.mes.module_warn.model.api.TemporaryAPI;
import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.bean.TemLubricateTaskEntity;
import com.supcon.mes.module_warn.model.contract.CompleteContract;
import com.supcon.mes.module_warn.model.contract.TemporaryContract;
import com.supcon.mes.module_warn.presenter.CompletePresenter;
import com.supcon.mes.module_warn.presenter.TemporaryPresenter;
import com.supcon.mes.module_warn.ui.adapter.TemporaryAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * 临时润滑
 */
@Router(Constant.Router.TEMPORARY_LUBRICATION_EARLY_WARN)
@Presenter(value = {TemporaryPresenter.class, CompletePresenter.class})
public class TemporaryLubricationWarnActivity extends BaseRefreshActivity implements TemporaryContract.View, CompleteContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("contentView")
    RecyclerView recyclerView;
    @BindByTag("eamCode")
    CustomVerticalTextView eamCode;
    @BindByTag("eamName")
    CustomVerticalTextView eamName;
    @BindByTag("ensure")
    Button ensure;
    private NFCHelper nfcHelper;

    private TemporaryAdapter temporaryAdapter;
    private final Map<String, Object> queryParam = new HashMap<>();
    private EamType eamType;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_temporary;
    }

    @Override
    protected void onInit() {
        super.onInit();
        eamType = (EamType) getIntent().getSerializableExtra(Constant.IntentKey.EAM);
        EventBus.getDefault().register(this);
        nfcHelper = NFCHelper.getInstance();
        if (nfcHelper != null) {
            nfcHelper.setup(this);
            nfcHelper.setOnNFCListener(nfc -> {
                LogUtil.d("NFC Received : " + nfc);
                EventBus.getDefault().post(new NFCEvent(nfc));
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
        titleText.setText("临时润滑");

        refreshController.setAutoPullDownRefresh(false);
        refreshController.setPullDownRefreshEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        temporaryAdapter = new TemporaryAdapter(context);
        recyclerView.setAdapter((BaseListDataRecyclerViewAdapter) EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "请刷卡获取设备"));
    }

    @Override
    protected void initData() {
        super.initData();
        if (eamType != null) {
            eamCode.setContent(Util.strFormat(eamType.code));
            eamName.setContent(Util.strFormat(eamType.name));
            queryParam.put(Constant.IntentKey.EAM_CODE, Util.strFormat(eamType.code));
            refreshController.refreshBegin();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        refreshController.setOnRefreshListener(() -> {
            Map<String, Object> pageQueryParams = new HashMap<>();
            pageQueryParams.put("page.pageNo", 1);
            presenterRouter.create(TemporaryAPI.class).getTempLubrications(queryParam, pageQueryParams);
        });

        RxView.clicks(ensure)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    List<TemLubricateTaskEntity> list = temporaryAdapter.getList();
                    StringBuffer sourceIds = new StringBuffer();
                    Flowable.fromIterable(list)
                            .filter(temLubricateTaskEntity -> temLubricateTaskEntity.isCheck)
                            .subscribe(temLubricateTaskEntity -> {
                                temLubricateTaskEntity.isLubri = true;
                                if (TextUtils.isEmpty(sourceIds)) {
                                    sourceIds.append(temLubricateTaskEntity.id);
                                } else {
                                    sourceIds.append(",").append(temLubricateTaskEntity.id);
                                }
                            }, throwable -> {
                            }, () -> {
                                if (!TextUtils.isEmpty(sourceIds)) {
                                    Map<String, Object> param = new HashMap<>();
                                    param.put(Constant.BAPQuery.sourceIds, sourceIds);
                                    param.put("taskType", "BEAM_067/02");
                                    onLoading("处理中...");
                                    presenterRouter.create(CompleteAPI.class).dailyComplete(param);
                                } else {
                                    ToastUtils.show(context, "请选择操作项!");
                                }
                            });
                });
        eamCode.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
                IntentRouter.go(TemporaryLubricationWarnActivity.this, Constant.Router.EAM, bundle);
            }
        });
        eamName.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
                IntentRouter.go(TemporaryLubricationWarnActivity.this, Constant.Router.EAM, bundle);
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
        LogUtil.d("NFC_TAG", nfcEvent.getNfc());
        Map<String, Object> nfcJson = Util.gsonToMaps(nfcEvent.getNfc());
        if (nfcJson.get("textRecord") == null) {
            ToastUtils.show(context, "标签内容空！");
            return;
        }
        eamCode.setContent(String.valueOf(nfcJson.get("textRecord")));
        queryParam.put(Constant.IntentKey.EAM_CODE, nfcJson.get("textRecord"));
        refreshController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity != null) {
            if (commonSearchEvent.commonSearchEntity instanceof EamType) {
                EamType eamType = (EamType) commonSearchEvent.commonSearchEntity;
                eamCode.setContent(Util.strFormat(eamType.code));
                eamName.setContent(Util.strFormat(eamType.name));
                queryParam.put(Constant.IntentKey.EAM_CODE, Util.strFormat(eamType.code));
                refreshController.refreshBegin();
            }
        }
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
    public void dailyCompleteSuccess(DelayEntity entity) {
        onLoadSuccessAndExit("任务完成", () -> {
            back();
        });
    }

    @Override
    public void dailyCompleteFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void getTempLubricationsSuccess(CommonBAPListEntity entity) {
        if (entity.result != null && entity.result.size() > 0) {
            ensure.setVisibility(View.VISIBLE);
            TemLubricateTaskEntity dailyLubricateTaskEntity = (TemLubricateTaskEntity) entity.result.get(0);
            eamCode.setContent(dailyLubricateTaskEntity.getEamID().code);
            eamName.setContent(dailyLubricateTaskEntity.getEamID().name);
            temporaryAdapter.setList(entity.result);
            recyclerView.setAdapter(temporaryAdapter);
        } else {
            ensure.setVisibility(View.GONE);
            recyclerView.setAdapter((BaseListDataRecyclerViewAdapter) EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "当前设备没有待润滑任务"));
        }
        refreshController.refreshComplete();
    }

    @Override
    public void getTempLubricationsFailed(String errorMsg) {
        ensure.setVisibility(View.GONE);
        recyclerView.setAdapter((BaseListDataRecyclerViewAdapter) EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        SnackbarHelper.showError(rootView, errorMsg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (nfcHelper != null) {
            nfcHelper.release();
        }
    }
}
