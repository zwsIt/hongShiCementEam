package com.supcon.mes.module_yhgl.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Range;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.ModulePermissonCheckController;
import com.supcon.mes.middleware.model.bean.YHEntityVo;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.util.TimeUtil;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;
import com.supcon.mes.module_yhgl.model.api.OfflineYHListAPI;
import com.supcon.mes.module_yhgl.model.bean.YHDtoListEntity;
import com.supcon.mes.module_yhgl.model.contract.OfflineYHListContract;
import com.supcon.mes.module_yhgl.presenter.OfflineYHListPresenter;
import com.supcon.mes.module_yhgl.ui.adapter.OfflineYHListAdapter;
import com.supcon.mes.module_yhgl.util.YHFilterHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.supcon.mes.middleware.constant.Constant.IntentKey.YHGL_ENTITY;

/**
 * Created by xushiyun on 2018/8/7
 * Email:ciruy.victory@gmail.com
 *
 * @author xushiyun
 */
@Router(Constant.Router.OFFLINE_YH_LIST)
@Presenter(OfflineYHListPresenter.class)
public class OfflineYHGLListActivity extends BaseRefreshRecyclerActivity<YHEntityVo>
        implements OfflineYHListContract.View {
    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;

    @BindByTag("rightBtn")
    AppCompatImageButton rightBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("customSearchView")
    CustomSearchView customSearchView;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;

    @BindByTag("contentView")
    RecyclerView contentView;

    OfflineYHListAdapter mAdapter;

    /**
     * 发现时间
     */
    @BindByTag("listDateFilter")
    CustomFilterView<FilterBean> listDateFilter;

    /**
     * 维修类型
     */
    @BindByTag("listWXTypeFilter")
    CustomFilterView<FilterBean> listWXTypeFilter;

    /**
     * 优先级
     */
    @BindByTag("listYHPriorityFilter")
    CustomFilterView<FilterBean> listYHPriorityFilter;

    /**
     * 单据状态
     */
    @BindByTag("listYHStatusFilter")
    CustomFilterView<FilterBean> listYHStatusFilter;

    Map<String, Object> queryParam = new HashMap<>();

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setWindowAnimations(R.style.activityAnimation);
    }

    @Override
    protected IListAdapter<YHEntityVo> createAdapter() {
        mAdapter = new OfflineYHListAdapter(context);
        return mAdapter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_yh_list;
    }

    @Override
    protected void initView() {
        super.initView();
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        customSearchView.setHint("搜索");
        searchTitleBar.enableRightBtn();

        setViewVisibility();
        setFilterViewDefaultData();
    }

    private void setFilterViewDefaultData() {
        listDateFilter.setData(YHFilterHelper.createDateFilter());
        listWXTypeFilter.setData(YHFilterHelper.createWXTypeFilter());
        listYHPriorityFilter.setData(YHFilterHelper.createPriorityFilter());
        //单据状态总共有三个选项可供选择，编辑中，待上传，全部
        listYHStatusFilter.setData(YHFilterHelper.createStatusFilter());
    }

    private void setViewVisibility() {
        listYHStatusFilter.setVisibility(View.VISIBLE);
    }

    @SuppressLint({"CheckResult", "NewApi"})
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> back());

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> OfflineYHGLListActivity.this.createYH());

        refreshListController.setOnRefreshPageListener(
                pageIndex -> presenterRouter.create(OfflineYHListAPI.class).queryYHList(pageIndex, queryParam));

        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> doSearchTableNo(charSequence.toString()));

        initFilterViews();

        mAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            YHEntityVo yhEntity = (YHEntityVo) obj;
            yhEntity.setSourceTypeManual();
            Bundle bundle = new Bundle();
            bundle.putSerializable(YHGL_ENTITY, yhEntity);

            IntentRouter.go(context, Constant.Router.OFFLINE_YH_EDIT, bundle);
        });

        searchTitleBar.setOnExpandListener(isExpand -> {
            if (isExpand) {
                customSearchView.setHint("搜索设备名称");
                customSearchView.setInputTextColor(R.color.hintColor);
            } else {
                customSearchView.setHint("搜索");
                customSearchView.setInputTextColor(R.color.black);
            }
        });
    }

    private void initFilterViews() {
        listDateFilter.setFilterSelectChangedListener(filterBean -> {
            updateFilterTimePeriod(TimeUtil.genTimePeriod(filterBean.name));
            doFilter();
        });

        listWXTypeFilter.setFilterSelectChangedListener(filterBean -> {
            if (filterBean.type == CustomFilterView.VIEW_TYPE_ALL) {
                queryParam.remove(Constant.BAPQuery.REPAIR_TYPE);
            } else {
                queryParam.put(Constant.BAPQuery.REPAIR_TYPE,
                        SystemCodeManager.getInstance().getSystemCodeEntity(Constant.SystemCode.YH_WX_TYPE, filterBean.name).id);
            }
            doFilter();
        });

        listYHPriorityFilter.setFilterSelectChangedListener(filterBean -> {
            if (filterBean.type == CustomFilterView.VIEW_TYPE_ALL) {
                queryParam.remove(Constant.BAPQuery.PRIORITY);
            } else {
                queryParam.put(Constant.BAPQuery.PRIORITY, SystemCodeManager.getInstance().getSystemCodeEntityId(Constant.SystemCode.YH_PRIORITY, filterBean.name));
            }
            doFilter();
        });
        listYHStatusFilter.setFilterSelectChangedListener(filterBean -> {
            if (filterBean.type == CustomFilterView.VIEW_TYPE_ALL) {
                queryParam.remove(Constant.BAPQuery.STATUS);
            } else {
                queryParam.put(Constant.BAPQuery.STATUS, "待上传".equals(filterBean.name));
            }
            doFilter();
        });
    }

    @SuppressLint("NewApi")
    private void updateFilterTimePeriod(Range<String> timePeriod) {
        if (TextUtils.isEmpty(timePeriod.getLower())) {
            queryParam.remove(Constant.BAPQuery.YH_DATE_START);
        } else {
            queryParam.put(Constant.BAPQuery.YH_DATE_START, timePeriod.getLower());
        }
        if (TextUtils.isEmpty(timePeriod.getUpper())) {
            queryParam.remove(Constant.BAPQuery.YH_DATE_END);
        } else {
            queryParam.put(Constant.BAPQuery.YH_DATE_END, timePeriod.getUpper());
        }
    }

    @Override
    protected void initData() {
        super.initData();
        ModulePermissonCheckController modulePermissonCheckController = new ModulePermissonCheckController();
        modulePermissonCheckController.checkModulePermission(EamApplication.getUserName(), "faultInfoFW", result -> {
        }, null);
    }

    private void createYH() {
        YHEntityVo yhEntity = YHEntityVo.createEntity();
        yhEntity.setFindStaffId(EamApplication.me().id);
        yhEntity.setFindStaffName(EamApplication.me().name);
        yhEntity.setFindDate(DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        yhEntity.setSourceTypeManual();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY, yhEntity);
        IntentRouter.go(context, Constant.Router.OFFLINE_YH_EDIT, bundle);
    }

    private void doFilter() {
        refreshListController.refreshBegin();
    }

    public void doSearchTableNo(String eamName) {
        queryParam.put(Constant.BAPQuery.EAM_NAME, eamName);
        doFilter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {
        doFilter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        doFilter();
    }


    @Override
    public void queryYHListSuccess(YHDtoListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void queryYHListFailed(String errorMsg) {
        refreshListController.refreshComplete();
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
    }
}
