package com.supcon.mes.module_yhgl.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.App;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.ModulePermissonCheckController;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.AreaDao;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;
import com.supcon.mes.module_yhgl.model.api.YHListAPI;
import com.supcon.mes.module_yhgl.model.bean.YHListEntity;
import com.supcon.mes.module_yhgl.model.contract.YHListContract;
import com.supcon.mes.module_yhgl.presenter.YHListPresenter;
import com.supcon.mes.module_yhgl.ui.adapter.YHListAdapter;
import com.supcon.mes.module_yhgl.util.YHFilterHelper;
import com.supcon.mes.sb2.model.event.BarcodeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

import static com.supcon.mes.middleware.constant.Constant.IntentKey.YHGL_ENTITY;


/**
 * Created by wangshizhan on 2018/8/7
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.YH_LIST)
@Presenter(YHListPresenter.class)
public class YHGLListActivity extends BaseRefreshRecyclerActivity<YHEntity> implements YHListContract.View {
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

    YHListAdapter mAdapter;

    @BindByTag("radioGroup1")
    RadioGroup radioGroup1;

    @BindByTag("listDateFilter")
    CustomFilterView<FilterBean> listDateFilter;

    @BindByTag("listWXTypeFilter")
    CustomFilterView<FilterBean> listWXTypeFilter;

    @BindByTag("listYHTypeFilter")
    CustomFilterView<FilterBean> listYHTypeFilter;

    @BindByTag("listAreaFilter")
    CustomFilterView<FilterBean> listAreaFilter;

    @BindByTag("listYHPriorityFilter")
    CustomFilterView<FilterBean> listYHPriorityFilter;

    private Boolean hasAddForumPermission;

    Map<String, Object> queryParam = new HashMap<>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private ModulePermissonCheckController mModulePermissonCheckController;
    private Long deploymentId;

    private boolean hasNext = true;
    private boolean isShow;

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
        isShow = true;
        getWindow().setWindowAnimations(R.style.activityAnimation);
    }

    @Override
    protected IListAdapter<YHEntity> createAdapter() {
        mAdapter = new YHListAdapter(context);
        return mAdapter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mAdapter.onDestroy();//释放下载资源
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_yh_list;
    }

    @Override
    protected void initView() {
        super.initView();
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(1, context)));

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        customSearchView.setHint("搜索");
        searchTitleBar.enableRightBtn();
        searchTitleBar.setTitleText("隐患单列表");

        listDateFilter.setData(YHFilterHelper.createDateFilter());
        listWXTypeFilter.setData(YHFilterHelper.createWXTypeFilter());
        listAreaFilter.setData(YHFilterHelper.createAreaFilter());
        listYHTypeFilter.setData(YHFilterHelper.createYHTypeFilter());
        listYHPriorityFilter.setData(YHFilterHelper.createPriorityFilter());

        YHFilterHelper.addview(this, radioGroup1, YHFilterHelper.createWorkSource(), 1000);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> back());

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (null != hasAddForumPermission && hasAddForumPermission) {
                            YHGLListActivity.this.createYH();
                        } else {
                            ToastUtils.show(context, "当前用户并未拥有创建单据权限！");
                        }
                    }
                });

        refreshListController.setOnRefreshPageListener(
                pageIndex -> {
                    if (mAdapter != null)
                        mAdapter.onDestroy();//刷新时暂停下载图片
                    presenterRouter.create(YHListAPI.class).queryYHList(pageIndex, queryParam);
                });

        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence.toString())) {
                        doSearchTableNo(charSequence.toString());
                    }
                });

        KeyExpandHelper.doActionSearch(customSearchView.editText(), true, () -> {
            doSearchTableNo(customSearchView.getInput().trim());
        });


        listDateFilter.setFilterSelectChangedListener(filterBean -> {
            LogUtil.w("" + filterBean.toString());
            Calendar calendar = Calendar.getInstance();
            String startTime;
            String endTime;
            switch (filterBean.name) {
                case Constant.Date.TODAY:
                    startTime = new StringBuilder(sdf.format(new Date())).append(Constant.TimeString.START_TIME).toString();
                    endTime = new StringBuilder(sdf.format(new Date())).append(Constant.TimeString.END_TIME).toString();
                    break;
                case Constant.Date.YESTERDAY:
                    calendar.add(Calendar.DAY_OF_WEEK, -1);
                    startTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.START_TIME).toString();
                    endTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.END_TIME).toString();
                    break;
                case Constant.Date.THREEDAY:
                    calendar.add(Calendar.DAY_OF_WEEK, +1); //明天
                    startTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.START_TIME).toString();

                    calendar.add(Calendar.DAY_OF_WEEK, +2);  //后两天
                    endTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.END_TIME).toString();
                    break;
                case Constant.Date.THIS_WEEK:
                    /*calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);*/
                    int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    if (day_of_week == 0) {
                        day_of_week = 7;
                    }
                    calendar.add(Calendar.DATE, -day_of_week + 1);
                    startTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.START_TIME).toString();

                    Calendar calendar2 = Calendar.getInstance();
//                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                    day_of_week = calendar2.get(Calendar.DAY_OF_WEEK) - 1;
                    if (day_of_week == 0) {
                        day_of_week = 7;
                    }
                    calendar2.add(Calendar.DATE, -day_of_week + 7);
                    endTime = new StringBuilder(sdf.format(calendar2.getTime())).append(Constant.TimeString.END_TIME).toString();
                    break;
                case Constant.Date.THIS_MONTH:
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    startTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.START_TIME).toString();

                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    endTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.END_TIME).toString();
                    break;
                default:
                    startTime = endTime = null;
                    break;
            }

            if (startTime == null) {
                queryParam.remove(Constant.BAPQuery.YH_DATE_START);
            } else {
                queryParam.put(Constant.BAPQuery.YH_DATE_START, startTime);
            }
            if (endTime == null) {
                queryParam.remove(Constant.BAPQuery.YH_DATE_END);
            } else {
                queryParam.put(Constant.BAPQuery.YH_DATE_END, endTime);
            }

            doFilter();

        });


        listWXTypeFilter.setFilterSelectChangedListener(new CustomFilterView.FilterSelectChangedListener<FilterBean>() {
            @Override
            public void onFilterSelected(FilterBean filterBean) {
                if (filterBean.type == CustomFilterView.VIEW_TYPE_ALL) {
                    queryParam.remove(Constant.BAPQuery.REPAIR_TYPE);
                } else
                    queryParam.put(Constant.BAPQuery.REPAIR_TYPE, SystemCodeManager.getInstance().getSystemCodeEntity(Constant.SystemCode.YH_WX_TYPE, filterBean.name).id);

                doFilter();
            }
        });

        listYHTypeFilter.setFilterSelectChangedListener(filterBean -> {
            if (filterBean.type == CustomFilterView.VIEW_TYPE_ALL) {
                queryParam.remove(Constant.BAPQuery.FAULT_INFO_TYPE);
            } else
                queryParam.put(Constant.BAPQuery.FAULT_INFO_TYPE, SystemCodeManager.getInstance().getSystemCodeEntity(Constant.SystemCode.QX_TYPE, filterBean.name).id);

            doFilter();
        });

        listAreaFilter.setFilterSelectChangedListener(new CustomFilterView.FilterSelectChangedListener<FilterBean>() {
            @Override
            public void onFilterSelected(FilterBean filterBean) {
                if (filterBean.type == CustomFilterView.VIEW_TYPE_ALL) {
                    queryParam.remove(Constant.BAPQuery.YH_AREA);
                } else {
                    Area area = EamApplication.dao().getAreaDao().queryBuilder()
                            .where(AreaDao.Properties.Name.eq(filterBean.name)).unique();
                    queryParam.put(Constant.BAPQuery.YH_AREA, area);
                }

                doFilter();
            }
        });

        listYHPriorityFilter.setFilterSelectChangedListener(new CustomFilterView.FilterSelectChangedListener<FilterBean>() {
            @Override
            public void onFilterSelected(FilterBean filterBean) {
                if (filterBean.type == CustomFilterView.VIEW_TYPE_ALL) {
                    queryParam.remove(Constant.BAPQuery.PRIORITY);
                } else {
                    queryParam.put(Constant.BAPQuery.PRIORITY, SystemCodeManager.getInstance().getSystemCodeEntityId(Constant.SystemCode.YH_PRIORITY, filterBean.name));
                }
                doFilter();
            }
        });

        mAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            YHEntity yhEntity = (YHEntity) obj;
            Bundle bundle = new Bundle();
            bundle.putSerializable(YHGL_ENTITY, yhEntity);

            if ("审核".equals(yhEntity.pending.taskDescription)) {
                IntentRouter.go(context, Constant.Router.YH_VIEW, bundle);
            } else if ("编辑".equals(yhEntity.pending.taskDescription)) {
                IntentRouter.go(context, Constant.Router.YH_EDIT, bundle);
            } else
                IntentRouter.go(context, Constant.Router.YH_VIEW, bundle);

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

        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(group.getCheckedRadioButtonId());
            generateWorkSourceFilter(radioButton.getText().toString());
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mModulePermissonCheckController = new ModulePermissonCheckController();
        mModulePermissonCheckController.checkModulePermission(EamApplication.getUserName(), "faultInfoFW", new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                deploymentId = result;
                hasAddForumPermission = true;
            }
        }, null);
    }

    private void createYH() {

        YHEntity yhEntity = new YHEntity();
        yhEntity.findStaffID = EamApplication.me();
        yhEntity.findTime = System.currentTimeMillis();
        yhEntity.cid = SharedPreferencesUtils.getParam(App.getAppContext(), Constant.CID, 0L);
        yhEntity.id = -1;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY, yhEntity);
        bundle.putSerializable(Constant.IntentKey.DEPLOYMENT_ID, deploymentId);
        IntentRouter.go(context, Constant.Router.YH_EDIT, bundle);

    }

    /**
     * @param
     * @return
     * @description 封装来源过滤条件
     * @author zhangwenshuai1 2018/8/14
     */
    private void generateWorkSourceFilter(String workSource) {
        String workSourceId = "";
        switch (workSource) {
            case Constant.WorkSource_CN.patrolcheck:
                workSourceId = Constant.YhglWorkSource.patrolcheck;
                break;
            case Constant.WorkSource_CN.lubrication:
                workSourceId = Constant.YhglWorkSource.lubrication;
                break;
            case Constant.WorkSource_CN.maintenance:
                workSourceId = Constant.YhglWorkSource.maintenance;
                break;
            case Constant.WorkSource_CN.sparepart:
                workSourceId = Constant.YhglWorkSource.sparepart;
                break;
            case Constant.WorkSource_CN.other:
                workSourceId = Constant.YhglWorkSource.other;
                break;
            default:
                break;
        }
        queryParam.put(Constant.BAPQuery.SOURCE_TYPE, workSourceId);
        doFilter();
    }

    private void doFilter() {
        refreshListController.refreshBegin();
    }

    @SuppressLint("CheckResult")
    public void doSearchTableNo(String eamName) {
        queryParam.put(Constant.BAPQuery.EAM_NAME, eamName);
        doFilter();
//        if (queryParam.containsKey(Constant.BAPQuery.TABLE_NO) || queryParam.containsKey(Constant.BAPQuery.EAM_NAME)) {
//            queryParam.remove(Constant.BAPQuery.TABLE_NO);
//            queryParam.remove(Constant.BAPQuery.EAM_NAME);
//            if (TextUtils.isEmpty(eamName)) {
//                refreshListController.refreshBegin();
//            }
//        }
//
//        if (!TextUtils.isEmpty(eamName)) {
//
//            if(Util.isContainChinese(eamName)){
//                queryParam.put(Constant.BAPQuery.EAM_NAME, eamName);
//            }
//            else {
//                queryParam.put(Constant.BAPQuery.TABLE_NO, eamName);
//            }
//            refreshListController.refreshBegin();
//
//        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {

        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {

        refreshListController.refreshBegin();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarCode(BarcodeEvent barcodeEvent) {
        String code = barcodeEvent.getScanCode();
        Log.e("code", code);
        if (isShow) {
            customSearchView.setInput(code);
            customSearchView.editText().setSelection(code.length());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShow = false;
    }


    @Override
    public void queryYHListSuccess(YHListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void queryYHListFailed(String errorMsg) {
        refreshListController.refreshComplete();
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
    }
}
