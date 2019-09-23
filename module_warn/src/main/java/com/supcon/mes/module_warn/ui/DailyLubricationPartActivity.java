package com.supcon.mes.module_warn.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_warn.IntentRouter;
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
 * 手动确认延期
 */
@Router(Constant.Router.DAILY_LUBRICATION_EARLY_PART_WARN)
@Presenter(value = {DailyLubricationWarnPresenter.class, CompletePresenter.class})
public class DailyLubricationPartActivity extends BaseRefreshRecyclerActivity<DailyLubricateTaskEntity> implements DailyLubricationWarnContract.View, CompleteContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("btnLayout")
    LinearLayout btnLayout;
    @BindByTag("dispatch")
    Button dispatch;
    @BindByTag("delay")
    Button delay;
    @BindByTag("overdue")
    Button overdue;

    private final Map<String, Object> queryParam = new HashMap<>();
    private DailyLubricationPartAdapter dailyLubricationPartAdapter;
    private String eamCode;
    private boolean isEdit;
    private long nextTime = 0;

    @Override
    protected IListAdapter<DailyLubricateTaskEntity> createAdapter() {
        dailyLubricationPartAdapter = new DailyLubricationPartAdapter(this);
        return dailyLubricationPartAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_daily_part_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        eamCode = getIntent().getStringExtra(Constant.IntentKey.EAM_CODE);
        isEdit = getIntent().getBooleanExtra(Constant.IntentKey.isEdit, false);
        dailyLubricationPartAdapter.setEditable(isEdit);
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
        dispatch.setText("完成");
        if (!isEdit) {
            btnLayout.setVisibility(View.GONE);
        }
        titleText.setText("润滑信息");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            queryParam.put(Constant.IntentKey.EAM_CODE, eamCode);
            Map<String, Object> pageQueryParams = new HashMap<>();
            pageQueryParams.put("page.pageNo", pageIndex);
            presenterRouter.create(DailyLubricationWarnAPI.class).getLubrications(queryParam,pageQueryParams);
        });

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        RxView.clicks(dispatch)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    List<DailyLubricateTaskEntity> list = dailyLubricationPartAdapter.getList();
                    StringBuffer sourceIds = new StringBuffer();
                    Flowable.fromIterable(list)
                            .filter(lubricationWarnEntity -> lubricationWarnEntity.isCheck)
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
        RxView.clicks(delay)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    List<DailyLubricateTaskEntity> list = dailyLubricationPartAdapter.getList();
                    StringBuffer sourceIds = new StringBuffer();
                    Bundle bundle = new Bundle();
                    Flowable.fromIterable(list)
                            .filter(lubricationWarnEntity -> lubricationWarnEntity.isCheck)
                            .subscribe(lubricationWarnEntity -> {
                                bundle.putString(Constant.IntentKey.WARN_PEROID_TYPE, lubricationWarnEntity.periodType != null ? lubricationWarnEntity.periodType.id : "");
                                if (TextUtils.isEmpty(sourceIds)) {
                                    sourceIds.append(lubricationWarnEntity.id);
                                } else {
                                    sourceIds.append(",").append(lubricationWarnEntity.id);
                                }
                                if (!lubricationWarnEntity.isDuration() && nextTime < lubricationWarnEntity.nextTime) {
                                    nextTime = lubricationWarnEntity.nextTime;
                                }
                            }, throwable -> {
                            }, () -> {
                                if (!TextUtils.isEmpty(sourceIds)) {
                                    bundle.putString(Constant.IntentKey.WARN_SOURCE_TYPE, "BEAM062/01");
                                    bundle.putString(Constant.IntentKey.WARN_SOURCE_IDS, sourceIds.toString());
                                    bundle.putLong(Constant.IntentKey.WARN_NEXT_TIME, nextTime);
                                    IntentRouter.go(this, Constant.Router.DELAYDIALOG, bundle);
                                } else {
                                    ToastUtils.show(this, "请选择操作项!");
                                }
                            });

                });
        RxView.clicks(overdue)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    List<DailyLubricateTaskEntity> list = dailyLubricationPartAdapter.getList();
                    StringBuffer sourceIds = new StringBuffer();
                    Bundle bundle = new Bundle();
                    Flowable.fromIterable(list)
                            .filter(lubricationWarnEntity -> lubricationWarnEntity.isCheck)
                            .subscribe(lubricationWarnEntity -> {
                                if (TextUtils.isEmpty(sourceIds)) {
                                    sourceIds.append(lubricationWarnEntity.id);
                                } else {
                                    sourceIds.append(",").append(lubricationWarnEntity.id);
                                }
                            }, throwable -> {
                            }, () -> {
                                if (!TextUtils.isEmpty(sourceIds)) {
                                    bundle.putString(Constant.IntentKey.WARN_SOURCE_TYPE, "BEAM062/01");
                                    bundle.putString(Constant.IntentKey.WARN_SOURCE_IDS, sourceIds.toString());
                                    bundle.putString(Constant.IntentKey.WARN_SOURCE_URL, "/BEAM/baseInfo/delayRecords/delayRecordsList-query.action");
                                    IntentRouter.go(this, Constant.Router.DELAY_RECORD, bundle);
                                } else {
                                    ToastUtils.show(this, "请选择操作项!");
                                }
                            });
                });

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
    public void getLubricationsSuccess(DailyLubricateTaskListEntity entity) {
        if (entity.pageNo == 1 && entity.result.size() <= 0) {
            btnLayout.setVisibility(View.GONE);
        } else if (isEdit) {
            btnLayout.setVisibility(View.VISIBLE);
        }

        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getLubricationsFailed(String errorMsg) {
        btnLayout.setVisibility(View.GONE);
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }

    @Override
    public void dailyCompleteSuccess(DelayEntity entity) {
        EventBus.getDefault().post(new RefreshEvent());
        onLoadSuccessAndExit("任务完成", () -> refreshListController.refreshBegin());
    }

    @Override
    public void dailyCompleteFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(context);
    }


}
