package com.supcon.mes.module_warn.ui.fragment;

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
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
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
import com.supcon.mes.module_warn.model.api.LubricationWarnAPI;
import com.supcon.mes.module_warn.model.bean.LubricationWarnEntity;
import com.supcon.mes.module_warn.model.bean.LubricationWarnListEntity;
import com.supcon.mes.module_warn.model.contract.LubricationWarnContract;
import com.supcon.mes.module_warn.presenter.LubricationWarnPresenter;
import com.supcon.mes.module_warn.ui.LubricationWarnActivity;
import com.supcon.mes.module_warn.ui.adapter.LubricationWarnAdapter;
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
 * ClassName
 * Created by zhangwenshuai1 on 2020/5/18
 * Email zhangwenshuai1@supcon.com
 * Desc 时间频率TimeFrequencyFragment
 */
@Presenter(value = LubricationWarnPresenter.class)
public class TimeFrequencyFragment extends BaseRefreshRecyclerFragment<LubricationWarnEntity> implements LubricationWarnContract.View {

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
    private long nextTime = 0;
    private LubricationWarnAdapter lubricationWarnAdapter;

    private final Map<String, Object> queryParam = new HashMap<>();
    private String searchContent;
    private ModulePermissonCheckController mModulePermissonCheckController;
    private Long deploymentId;
    private long warnId;


    @Override
    protected IListAdapter<LubricationWarnEntity> createAdapter() {
        lubricationWarnAdapter = new LubricationWarnAdapter(context);
        return lubricationWarnAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_warn_lubrication;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        warnId = getActivity().getIntent().getLongExtra(Constant.IntentKey.WARN_ID, -1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        refreshListController.refreshBegin();
        lubricationWarnAdapter.setCheckPosition(-1); // 还原选中状态
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {
        refreshListController.refreshBegin();
    }

    @Override
    protected void initView() {
        super.initView();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(15));

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
            if (!TextUtils.isEmpty(searchContent)) {
                queryParam.put(Constant.BAPQuery.EAM_CODE, searchContent);
            }
            String url = "/BEAM/baseInfo/jWXItem/data-dg1530747504994.action";
            presenterRouter.create(LubricationWarnAPI.class).getLubrication(url, queryParam, pageIndex, warnId);
//            warnId = -1;
        });

        RxView.clicks(dispatch)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    List<LubricationWarnEntity> list = new ArrayList<>();
                    list.addAll(lubricationWarnAdapter.getList());
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
                                WXGDEntity lubri = WXGDWarnManager.lubri(lubricationWarnEntity.get(0));
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
                                    .bindClickListener(R.id.redBtn, v12 -> IntentRouter.go(context, Constant.Router.WXGD_WARN, pair.second), true)
                                    .bindClickListener(R.id.grayBtn, null, true).show());

                });

        RxView.clicks(delay)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    List<LubricationWarnEntity> list = lubricationWarnAdapter.getList();
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
                                if (!lubricationWarnEntity.isDuration() /*&& nextTime < lubricationWarnEntity.nextTime*/) {
                                    nextTime = lubricationWarnEntity.nextTime;
                                }
                            }, throwable -> {
                            }, () -> {
                                if (!TextUtils.isEmpty(sourceIds)) {
                                    bundle.putString(Constant.IntentKey.WARN_SOURCE_TYPE, "BEAM062/01");
                                    bundle.putString(Constant.IntentKey.WARN_SOURCE_IDS, sourceIds.toString());
                                    bundle.putLong(Constant.IntentKey.WARN_NEXT_TIME, nextTime);
                                    IntentRouter.go(context, Constant.Router.DELAYDIALOG, bundle);
                                } else {
                                    ToastUtils.show(context, "请选择操作项!");
                                }
                            });

                });
        RxView.clicks(overdue)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    List<LubricationWarnEntity> list = lubricationWarnAdapter.getList();
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
                                    IntentRouter.go(context, Constant.Router.DELAY_RECORD, bundle);
                                } else {
                                    ToastUtils.show(context, "请选择操作项!");
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

    public void doSearch(String search) {
        searchContent = search;
        refreshListController.refreshBegin();
    }

    @Override
    public void getLubricationSuccess(LubricationWarnListEntity entity) {
        if (entity.pageNo == 1 && entity.result.size() <= 0) {
            btnLayout.setVisibility(View.GONE);
        } else {
            btnLayout.setVisibility(View.VISIBLE);
        }
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getLubricationFailed(String errorMsg) {
        btnLayout.setVisibility(View.GONE);
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
