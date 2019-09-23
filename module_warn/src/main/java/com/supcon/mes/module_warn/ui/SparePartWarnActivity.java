package com.supcon.mes.module_warn.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.ModulePermissonCheckController;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_warn.IntentRouter;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.api.SparePartWarnAPI;
import com.supcon.mes.module_warn.model.bean.SparePartWarnEntity;
import com.supcon.mes.module_warn.model.bean.SparePartWarnListEntity;
import com.supcon.mes.module_warn.model.contract.SparePartWarnContract;
import com.supcon.mes.module_warn.presenter.SparePartWarnPresenter;
import com.supcon.mes.module_warn.ui.adapter.SparePartWarnAdapter;
import com.supcon.mes.module_warn.ui.util.WXGDWarnManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 备件预警
 */
@Router(Constant.Router.SPARE_EARLY_WARN)
@Presenter(value = SparePartWarnPresenter.class)
public class SparePartWarnActivity extends BaseRefreshRecyclerActivity<SparePartWarnEntity> implements SparePartWarnContract.View {

    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;

    @BindByTag("customSearchView")
    CustomSearchView titleSearchView;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("warnRadioGroup")
    RadioGroup warnRadioGroup;

    @BindByTag("btnLayout")
    LinearLayout btnLayout;

    @BindByTag("dispatch")
    Button dispatch;
    @BindByTag("delay")
    Button delay;
    @BindByTag("overdue")
    Button overdue;

    private SparePartWarnAdapter sparePartWarnAdapter;

    private final Map<String, Object> queryParam = new HashMap<>();
    private String selecStr;
    private String url;
    private long nextTime = 0;
    private ModulePermissonCheckController mModulePermissonCheckController;
    private Long deploymentId;
    private long warnId;
    private String property;


    @Override
    protected IListAdapter<SparePartWarnEntity> createAdapter() {
        sparePartWarnAdapter = new SparePartWarnAdapter(this);
        return sparePartWarnAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_early_warn_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        warnId = getIntent().getLongExtra(Constant.IntentKey.WARN_ID, -1);
        property = getIntent().getStringExtra(Constant.IntentKey.PROPERTY);
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
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(15));
        //设置搜索框默认提示语
        titleSearchView.setHint("请输入设备编码");
        searchTitleBar.setTitleText("备件更换预警");
        searchTitleBar.setBackgroundResource(R.color.gradient_start);
        searchTitleBar.disableRightBtn();

        if (!TextUtils.isEmpty(property) && property.equals(Constant.PeriodType.RUNTIME_LENGTH)) {
            warnRadioGroup.check(R.id.warnRadioBtn2);
            url = "/BEAM/baseInfo/sparePart/data-dg1543250233613.action";
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mModulePermissonCheckController = new ModulePermissonCheckController();
        mModulePermissonCheckController.checkModulePermission(EamApplication.getUserName(), "work", new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                deploymentId = result;
            }
        }, null);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            if (queryParam.containsKey(Constant.BAPQuery.EAM_CODE)) {
                queryParam.remove(Constant.BAPQuery.EAM_CODE);
            }
            if (!TextUtils.isEmpty(selecStr)) {
                queryParam.put(Constant.BAPQuery.EAM_CODE, selecStr);
            }
            setRadioEnable(false);
            presenterRouter.create(SparePartWarnAPI.class).getSparePart(url, queryParam, pageIndex, warnId);
//            warnId = -1;
        });
        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        doSearchTableNo(charSequence.toString());
                    }
                });
        KeyExpandHelper.doActionSearch(titleSearchView.editText(), true, () ->
                doSearchTableNo(titleSearchView.getInput()));

        leftBtn.setOnClickListener(v -> onBackPressed());

        warnRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.warnRadioBtn1) {
                    url = "/BEAM/baseInfo/sparePart/data-dg1535424823416.action";
                } else if (checkedId == R.id.warnRadioBtn2) {
                    url = "/BEAM/baseInfo/sparePart/data-dg1543250233613.action";
                }
                Flowable.timer(500, TimeUnit.MILLISECONDS)
                        .subscribe(aLong -> doRefresh());
            }
        });
//        RxView.clicks(dispatch)
//                .throttleFirst(2, TimeUnit.SECONDS)
//                .subscribe(o -> {
//                    List<SparePartWarnEntity> list = sparePartWarnAdapter.getList();
//                    Bundle bundle = new Bundle();
//                    Flowable.fromIterable(list)
//                            .subscribeOn(Schedulers.io())
//                            .filter(sparePartWarnEntity -> sparePartWarnEntity.isCheck)
//                            .map(sparePartWarnEntity -> {
//                                WXGDEntity spare = WXGDWarnManager.spare(sparePartWarnEntity);
//                                spare.pending.deploymentId = deploymentId;
//                                return spare;
//                            })
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(wxgdEntity -> {
//                                bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY, wxgdEntity);
//                            });
//                    if (!bundle.containsKey(Constant.IntentKey.WXGD_ENTITY)) {
//                        ToastUtils.show(context, "请选择操作项！");
//                        return;
//                    }
//                    new CustomDialog(context)
//                            .twoButtonAlertDialog("确定生成工单?")
//                            .bindView(R.id.redBtn, "确定")
//                            .bindView(R.id.grayBtn, "取消")
//                            .bindClickListener(R.id.redBtn, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v12) {
////                                    if (!bundle.containsKey(Constant.IntentKey.WXGD_ENTITY)) {
////                                        ToastUtils.show(context, "未选择单据!");
////                                        return;
////                                    } else
//                                        IntentRouter.go(SparePartWarnActivity.this, Constant.Router.WXGD_WARN, bundle);
//                                }
//                            }, true)
//                            .bindClickListener(R.id.grayBtn, null, true).show();
//
//                });
        RxView.clicks(dispatch)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    List<SparePartWarnEntity> list = new ArrayList<>();
                    list.addAll(sparePartWarnAdapter.getList());
                    Flowable.just(list)
                            .subscribeOn(Schedulers.io())
                            .doOnNext(lubricationWarnEntities -> {
                                int length = lubricationWarnEntities.size();
                                for (int i = length - 1; i >= 0; i--) {
                                    if (!lubricationWarnEntities.get(i).isCheck) {
                                        lubricationWarnEntities.remove(i);
                                    }
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .filter(lubricationWarnEntities -> {
                                if (list.size() <= 0) {
                                    ToastUtils.show(context, "请选择操作项！");
                                    return false;
                                }
                                return true;
                            })
                            .observeOn(Schedulers.io())
                            .map(lubricationWarnEntity -> {
                                WXGDEntity lubri = WXGDWarnManager.spare(lubricationWarnEntity.get(0));
                                lubri.pending.deploymentId = deploymentId;
                                return lubri;
                            })
                            .map(wxgdEntity -> {
                                final Bundle bundle = new Bundle();
                                bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY, wxgdEntity);
                                bundle.putBoolean(Constant.IntentKey.ISWARN, true);
                                return Pair.create(wxgdEntity, bundle);
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(pair -> new CustomDialog(context)
                                    .twoButtonAlertDialog("确定生成工单?")
                                    .bindView(R.id.redBtn, "确定")
                                    .bindView(R.id.grayBtn, "取消")
                                    .bindClickListener(R.id.redBtn, v12 -> IntentRouter.go(SparePartWarnActivity.this, Constant.Router.WXGD_WARN, pair.second), true)
                                    .bindClickListener(R.id.grayBtn, null, true).show());
                });
        RxView.clicks(delay)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    List<SparePartWarnEntity> list = sparePartWarnAdapter.getList();
                    StringBuffer sourceIds = new StringBuffer();
                    Bundle bundle = new Bundle();
                    Flowable.fromIterable(list)
                            .filter(sparePartWarnEntity -> sparePartWarnEntity.isCheck)
                            .subscribe(sparePartWarnEntity -> {
                                bundle.putString(Constant.IntentKey.WARN_PEROID_TYPE, sparePartWarnEntity.periodType != null ? sparePartWarnEntity.periodType.id : "");
                                if (TextUtils.isEmpty(sourceIds)) {
                                    sourceIds.append(sparePartWarnEntity.id);
                                } else {
                                    sourceIds.append(",").append(sparePartWarnEntity.id);
                                }
                                if (!sparePartWarnEntity.isDuration() && nextTime < sparePartWarnEntity.nextTime) {
                                    nextTime = sparePartWarnEntity.nextTime;
                                }
                            }, throwable -> {
                            }, () -> {
                                if (!TextUtils.isEmpty(sourceIds)) {
                                    bundle.putString(Constant.IntentKey.WARN_SOURCE_TYPE, "BEAM062/03");
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
                    List<SparePartWarnEntity> list = sparePartWarnAdapter.getList();
                    StringBuffer sourceIds = new StringBuffer();
                    Bundle bundle = new Bundle();
                    Flowable.fromIterable(list)
                            .filter(sparePartWarnEntity -> sparePartWarnEntity.isCheck)
                            .subscribe(sparePartWarnEntity -> {
                                if (TextUtils.isEmpty(sourceIds)) {
                                    sourceIds.append(sparePartWarnEntity.id);
                                } else {
                                    sourceIds.append(",").append(sparePartWarnEntity.id);
                                }
                            }, throwable -> {
                            }, () -> {
                                if (!TextUtils.isEmpty(sourceIds)) {
                                    bundle.putString(Constant.IntentKey.WARN_SOURCE_TYPE, "BEAM062/03");
                                    bundle.putString(Constant.IntentKey.WARN_SOURCE_IDS, sourceIds.toString());
                                    bundle.putString(Constant.IntentKey.WARN_SOURCE_URL, "/BEAM/baseInfo/delayRecords/sp_DelayRecordsList-query.action");
                                    IntentRouter.go(this, Constant.Router.DELAY_RECORD, bundle);
                                } else {
                                    ToastUtils.show(this, "请选择操作项!");
                                }
                            });
                });

    }

    /**
     * 进行过滤查询
     */
    private void doRefresh() {
        refreshListController.refreshBegin();
    }

    public void doSearchTableNo(String search) {
        selecStr = search;
        refreshListController.refreshBegin();
    }


    @Override
    public void getSparePartSuccess(SparePartWarnListEntity entity) {
        if (entity.pageNo == 1 && entity.result.size() <= 0) {
            btnLayout.setVisibility(View.GONE);
        } else {
            btnLayout.setVisibility(View.VISIBLE);
        }
        refreshListController.refreshComplete(entity.result);
        setRadioEnable(true);
    }

    @Override
    public void getSparePartFailed(String errorMsg) {
        btnLayout.setVisibility(View.GONE);
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
        setRadioEnable(true);
    }

    public void setRadioEnable(boolean enable) {
        for (int i = 0; i < warnRadioGroup.getChildCount(); i++) {
            warnRadioGroup.getChildAt(i).setEnabled(enable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
