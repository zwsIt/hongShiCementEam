package com.supcon.mes.module_acceptance.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_acceptance.IntentRouter;
import com.supcon.mes.module_acceptance.R;
import com.supcon.mes.module_acceptance.model.api.AcceptanceListAPI;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceEntity;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceListEntity;
import com.supcon.mes.module_acceptance.model.contract.AcceptanceListContract;
import com.supcon.mes.module_acceptance.presenter.AcceptanceListPresenter;
import com.supcon.mes.module_acceptance.ui.adapter.AcceptanceListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
@Router(value = Constant.Router.ACCEPTANCE_LIST)
@Presenter(value = AcceptanceListPresenter.class)
public class AcceptanceListActivity extends BaseRefreshRecyclerActivity<AcceptanceEntity> implements AcceptanceListContract.View {

    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;

    @BindByTag("rightBtn")
    AppCompatImageButton rightBtn;

    @BindByTag("customSearchView")
    CustomSearchView customSearchView;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;


    @BindByTag("contentView")
    RecyclerView contentView;

    private AcceptanceListAdapter acceptanceListAdapter;

    private final Map<String, Object> queryParam = new HashMap<>();
    private String selecStr;
    private String tableNo;
    private EamType eamType;

    @Override
    protected IListAdapter createAdapter() {
        acceptanceListAdapter = new AcceptanceListAdapter(this);
        return acceptanceListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_acceptance_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        tableNo = getIntent().getStringExtra(Constant.IntentKey.TABLENO);
        eamType = (EamType) getIntent().getSerializableExtra(Constant.IntentKey.EAM);
        if (eamType != null) {
            selecStr = eamType.name;
        }
    }


    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        searchTitleBar.setBackgroundResource(R.color.gradient_start);
        searchTitleBar.disableRightBtn();

        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(15));
        customSearchView.setHint("请输入设备");
        searchTitleBar.enableRightBtn();
        customSearchView.setInput(selecStr);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onBackPressed();
                    }
                });

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.isEdit, true);
                    bundle.putSerializable(Constant.IntentKey.EAM, eamType);
                    IntentRouter.go(AcceptanceListActivity.this, Constant.Router.ACCEPTANCE_EDIT, bundle);
                });

        refreshListController.setOnRefreshPageListener(pageIndex -> {
            if (queryParam.containsKey(Constant.BAPQuery.EAM_NAME)) {
                queryParam.remove(Constant.BAPQuery.EAM_NAME);
            }
            if (queryParam.containsKey(Constant.BAPQuery.EAM_CODE)) {
                queryParam.remove(Constant.BAPQuery.EAM_CODE);
            }
            if (!TextUtils.isEmpty(selecStr)) {
                if (Util.isContainChinese(selecStr)) {
                    queryParam.put(Constant.BAPQuery.EAM_NAME, selecStr);
                } else {
                    queryParam.put(Constant.BAPQuery.EAM_CODE, selecStr);
                }
            }
            if (queryParam.containsKey(Constant.BAPQuery.TABLE_NO)) {
                queryParam.remove(Constant.BAPQuery.TABLE_NO);
            }
            if (!TextUtils.isEmpty(tableNo)) {
                queryParam.put(Constant.BAPQuery.TABLE_NO, tableNo);
            }
            presenterRouter.create(AcceptanceListAPI.class).getAcceptanceList(queryParam, pageIndex);
        });

        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        doSearchTableNo(charSequence.toString());
                    }
                });
        KeyExpandHelper.doActionSearch(customSearchView.editText(), true, () ->
                doSearchTableNo(customSearchView.getInput()));

        acceptanceListAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                AcceptanceEntity item = acceptanceListAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.IntentKey.ACCEPTANCE_ENTITY, item);
                IntentRouter.go(AcceptanceListActivity.this, Constant.Router.ACCEPTANCE_EDIT, bundle);
            }
        });

    }

    public void doSearchTableNo(String search) {
        selecStr = search;
        refreshListController.refreshBegin();
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
    public void getAcceptanceListSuccess(AcceptanceListEntity entity) {
        if (entity.result != null) {
            refreshListController.refreshComplete(entity.result);
            if (!TextUtils.isEmpty(tableNo)) {
                if (entity.result.size() == 1) {
                    AcceptanceEntity acceptanceEntity = entity.result.get(0);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.IntentKey.ACCEPTANCE_ENTITY, acceptanceEntity);
                    IntentRouter.go(AcceptanceListActivity.this, Constant.Router.ACCEPTANCE_EDIT, bundle);
                    back();
                }
            }
        } else {
            refreshListController.refreshComplete(null);
        }
    }

    @Override
    public void getAcceptanceListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
