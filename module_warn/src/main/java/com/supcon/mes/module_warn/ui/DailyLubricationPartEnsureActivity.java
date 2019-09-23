package com.supcon.mes.module_warn.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.api.CompleteAPI;
import com.supcon.mes.module_warn.model.api.DailyLubricationWarnAPI;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskEntity;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskListEntity;
import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.contract.CompleteContract;
import com.supcon.mes.module_warn.model.contract.DailyLubricationWarnContract;
import com.supcon.mes.module_warn.presenter.CompletePresenter;
import com.supcon.mes.module_warn.presenter.DailyLubricationWarnPresenter;
import com.supcon.mes.module_warn.ui.adapter.DailyLubricationPartAdapter;

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
 * NFC扫码确认
 */
@Router(Constant.Router.DAILY_LUBRICATION_EARLY_PART_ENSURE_WARN)
@Presenter(value = {DailyLubricationWarnPresenter.class, CompletePresenter.class})
public class DailyLubricationPartEnsureActivity extends BaseRefreshRecyclerActivity<DailyLubricateTaskEntity> implements DailyLubricationWarnContract.View, CompleteContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("ensure")
    Button ensure;

    private final Map<String, Object> queryParam = new HashMap<>();
    private DailyLubricationPartAdapter dailyLubricationPartAdapter;
    private String eamCode;

    private NFCHelper nfcHelper;

    @Override
    protected IListAdapter<DailyLubricateTaskEntity> createAdapter() {
        dailyLubricationPartAdapter = new DailyLubricationPartAdapter(this);
        return dailyLubricationPartAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_daily_part_ensure_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        eamCode = getIntent().getStringExtra(Constant.IntentKey.EAM_CODE);
        dailyLubricationPartAdapter.setEditable(false);

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
        contentView.addItemDecoration(new SpaceItemDecoration(15));
        titleText.setText("日常润滑任务");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            queryParam.put(Constant.IntentKey.EAM_CODE, eamCode);
            Map<String, Object> pageQueryParams = new HashMap<>();
            pageQueryParams.put("page.pageNo", pageIndex);
            presenterRouter.create(DailyLubricationWarnAPI.class).getLubrications(queryParam, pageQueryParams);
        });

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());


        RxView.clicks(ensure)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    List<DailyLubricateTaskEntity> list = dailyLubricationPartAdapter.getList();
                    StringBuffer sourceIds = new StringBuffer();
                    Flowable.fromIterable(list)
                            .subscribe(lubricationWarnEntity -> {
                                if (TextUtils.isEmpty(sourceIds)) {
                                    sourceIds.append(lubricationWarnEntity.id);
                                } else {
                                    sourceIds.append(",").append(lubricationWarnEntity.id);
                                }
                            }, throwable -> {
                            }, () -> {
                                if (!TextUtils.isEmpty(sourceIds)) {
                                    Map<String, Object> param = new HashMap<>();
                                    param.put(Constant.BAPQuery.sourceIds, sourceIds);
                                    param.put("taskType", "BEAM_067/01");
                                    onLoading("处理中...");
                                    presenterRouter.create(CompleteAPI.class).dailyComplete(param);
                                } else {
                                    ToastUtils.show(this, "请选择操作项!");
                                }
                            });
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
        doDeal((String) nfcJson.get("textRecord"));
    }

    @SuppressLint("CheckResult")
    private void doDeal(String code) {
        List<DailyLubricateTaskEntity> list = dailyLubricationPartAdapter.getList();
        if (list == null)
            return;
        if (!code.equals(eamCode)) {
            SnackbarHelper.showMessage(contentView, "请确认设备(" + eamCode + ")!");
            return;
        }
        if (list.size() <= 0) {
            SnackbarHelper.showMessage(contentView, "没有润滑部位!");
            return;
        }
        StringBuffer sourceIds = new StringBuffer();
        Flowable.fromIterable(list)
                .subscribe(lubricationWarnEntity -> {
                    if (TextUtils.isEmpty(sourceIds)) {
                        sourceIds.append(lubricationWarnEntity.id);
                    } else {
                        sourceIds.append(",").append(lubricationWarnEntity.id);
                    }
                }, throwable -> {
                }, () -> {
                    if (!TextUtils.isEmpty(sourceIds)) {
                        Map<String, Object> param = new HashMap<>();
                        param.put(Constant.BAPQuery.sourceIds, sourceIds);
                        param.put("taskType", "BEAM_067/01");
                        onLoading("处理中...");
                        presenterRouter.create(CompleteAPI.class).dailyComplete(param);
                    } else {
                        ToastUtils.show(this, "当前设备没有润滑部位!");
                    }
                });
    }

    @Override
    public void getLubricationsSuccess(DailyLubricateTaskListEntity entity) {
        if (entity.pageNo == 1 && entity.result.size() <= 0) {
            ensure.setVisibility(View.GONE);
        } else {
            ensure.setVisibility(View.VISIBLE);
        }

        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getLubricationsFailed(String errorMsg) {
        ensure.setVisibility(View.GONE);
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }

    @Override
    public void dailyCompleteSuccess(DelayEntity entity) {
        onLoadSuccessAndExit("任务完成", () -> {
            onBackPressed();
            EventBus.getDefault().post(new RefreshEvent());
        });
    }

    @Override
    public void dailyCompleteFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
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

}
