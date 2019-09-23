package com.supcon.mes.module_sbda.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.controller.RefreshRecyclerController;
import com.supcon.mes.mbap.adapter.RecyclerEmptyAdapter;
import com.supcon.mes.mbap.beans.EmptyViewEntity;
import com.supcon.mes.mbap.utils.KeyHelper;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceFilterType;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sbda.IntentRouter;
import com.supcon.mes.module_sbda.R;
import com.supcon.mes.module_sbda.model.api.SBDAListAPI;
import com.supcon.mes.module_sbda.model.bean.SBDAListEntity;
import com.supcon.mes.module_sbda.model.contract.SBDAListContract;
import com.supcon.mes.module_sbda.presenter.SBDAListPresenter;
import com.supcon.mes.module_sbda.ui.adapter.SBDAListAdapter;
import com.supcon.mes.module_sbda.util.FilterHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/3/30.
 */
@Router(Constant.Router.SBDA_LIST)
@Presenter(value = {SBDAListPresenter.class})
public class SBDAListActivity extends BaseRefreshRecyclerActivity<CommonDeviceEntity> implements SBDAListContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;

    @BindByTag("customSearchView")
    CustomSearchView titleSearchView;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("rightBtn")
    AppCompatImageButton rightBtn;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;

    @BindByTag("listTypeFilter")
    CustomFilterView listTypeFilter;

    @BindByTag("listAreaFilter")
    CustomFilterView listAreaFilter;

    @BindByTag("listStatusFilter")
    CustomFilterView listStatusFilter;

    private final Map<String, Object> queryParam = new HashMap<>();

    private SBDAListAdapter mSBDAListAdapter;

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setLoadMoreEnable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        //设置搜索框默认提示语
        titleSearchView.setHint("搜索内容");
        searchTitleBar.setBackgroundResource(R.color.gradient_start);
        searchTitleBar.disableRightBtn();

        contentView.setLayoutManager(new LinearLayoutManager(context));
        final int spacingInPixels = 1;
        contentView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        initEmptyView();
        initFilterView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void initFilterView() {
        listTypeFilter.setData(FilterHelper.createDeviceTypeFilter());
        listAreaFilter.setData(FilterHelper.createDeviceAreaFilter());
        listStatusFilter.setData(FilterHelper.createDeviceStatusFilter());
    }

    private void initEmptyView() {
         refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sbda_list;
    }


    @Override
    public void getSearchSBDASuccess(SBDAListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getSearchSBDAFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    @Override
    protected IListAdapter<CommonDeviceEntity> createAdapter() {
        mSBDAListAdapter = new SBDAListAdapter(this);
        return mSBDAListAdapter;
    }

    /**
     * 进行过滤查询
     */
    private void doFilter() {
        refreshListController.refreshBegin();
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        refreshListController.setOnRefreshPageListener((page) -> {
            String blur = queryParam.get("blur")==null?"":queryParam.get("blur").toString();
//            Log.e("test", String.valueOf(queryParam.get("blur")));
            presenterRouter.create(SBDAListAPI.class).getSearchSBDA(blur.trim(), queryParam, page, 20);
        });
        //点击触发事件跳转界面
        mSBDAListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            CommonDeviceEntity commonDeviceEntity = (CommonDeviceEntity) obj;
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.IntentKey.SBDA_ENTITY, commonDeviceEntity);
            IntentRouter.go(context, Constant.Router.SBDA_VIEW, bundle);
        });
        leftBtn.setOnClickListener(v -> onBackPressed());
//        titleSearchView.setOnListItemSelectedListener(s -> {
//
//        });

        RxTextView
                .textChanges(titleSearchView.editText())
                .skipInitialValue()
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(charSequence   -> {
//                        if (TextUtils.isEmpty(charSequence)) return;
                    doSearchTableNo(charSequence.toString());
                });

        listTypeFilter.setFilterSelectChangedListener(filterBean -> {
            queryParam.put(CommonDeviceFilterType.TYPE.name(), filterBean.name);
            doFilter();
        });
        listStatusFilter.setFilterSelectChangedListener(filterBean -> {
            queryParam.put(CommonDeviceFilterType.STATUS.name(), filterBean.name);
            doFilter();
        });
        listAreaFilter.setFilterSelectChangedListener(filterBean -> {
            queryParam.put(CommonDeviceFilterType.AREA.name(), filterBean.name);
            doFilter();
        });

        KeyHelper.doActionNext(titleSearchView.editText(), true, () -> {
            refreshListController.refreshBegin();
        });
    }

    public void doSearchTableNo(String blur){

        if(queryParam.containsKey("blur")){
            queryParam.remove("blur");
            if(TextUtils.isEmpty(blur)){
                refreshListController.refreshBegin();
            }
        }

        if(!TextUtils.isEmpty(blur)){
            queryParam.put("blur", blur);
            refreshListController.refreshBegin();

        }


    }


    private void resetFilterStatus() {
        listAreaFilter.setCurrentPosition(0);
        listTypeFilter.setCurrentPosition(0);
        listStatusFilter.setCurrentPosition(0);
        doFilter();
    }
}
