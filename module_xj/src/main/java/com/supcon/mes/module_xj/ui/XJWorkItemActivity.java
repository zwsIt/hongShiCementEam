package com.supcon.mes.module_xj.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.XJAreaEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntityDao;
import com.supcon.mes.middleware.model.event.FilterSearchTabEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.ThermometerEvent;
import com.supcon.mes.middleware.util.SP2ThermometerHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.middleware.model.factory.SearchContentFactory;
import com.supcon.mes.module_xj.IntentRouter;
import com.supcon.mes.module_xj.R;
import com.supcon.mes.module_xj.model.api.XJWorkItemAPI;
import com.supcon.mes.module_xj.model.bean.XJWorkItemListEntity;
import com.supcon.mes.module_xj.model.contract.XJWorkItemContract;
import com.supcon.mes.module_xj.presenter.XJWorkItemPresenter;
import com.supcon.mes.module_xj.ui.fragment.XJHandledWorkItemFragment;
import com.supcon.mes.module_xj.ui.fragment.XJUnhandledWorkItemFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/1/16.
 * Email:wangshizhan@supcon.com
 */

@Router(Constant.Router.XJITEM_LIST)
@Presenter(value = {XJWorkItemPresenter.class})
public class XJWorkItemActivity extends BasePresenterActivity implements XJWorkItemContract.View {

    XJUnhandledWorkItemFragment mXJUnhandledItemFragment;
    XJHandledWorkItemFragment mXJHandledItemFragment;


    @BindByTag("xjItemVP")
    NoScrollViewPager xjItemVP;

    @BindByTag("xjItemTab")
    CustomTab xjItemTab;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("faultListBtn")
    Button faultListBtn;
//    @BindByTag("faultListImgView")
//    ImageView faultListImgView;

    private XJAreaEntity mXJAreaEntity;

    @BindByTag("filterNameLl")
    RelativeLayout filterNameLl;
    @BindByTag("filterNameTv")
    TextView filterNameTv;

    @BindByTag("deleteIv")
    ImageView deleteIv;
//    String areaName; //区域名称

    Map deviceNames = new HashMap<String, String>();


    String getFilterName() {
        String filterName = filterNameTv.getText().toString();
        if (TextUtils.equals(filterName, "不限")) return "";
        return filterName;
    }

    Boolean isFinished = false;

    private SinglePickController mSinglePickController;


    @Override
    protected int getLayoutID() {
        return R.layout.ac_work_item;
    }

    @Override
    protected void onInit() {
        super.onInit();
        mXJAreaEntity = (XJAreaEntity) getIntent().getSerializableExtra(Constant.IntentKey.XJAREA_ENTITY);
//        areaName = mXJAreaEntity.areaName;
        isFinished = getIntent().getStringExtra(Constant.IntentKey.XJPATH_STATUS).equals(Constant.XJPathStateType.PAST_CHECK_STATE);
        EventBus.getDefault().register(this);
        mSinglePickController = new SinglePickController(this);
        mSinglePickController.textSize(18);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(mXJAreaEntity.areaName);
        initTabs();
        initViewPager();
    }


    private void initTabs() {
        xjItemTab.addTab("未完成");
        xjItemTab.addTab("已完成");
    }

    private void initViewPager() {

        mXJUnhandledItemFragment = new XJUnhandledWorkItemFragment();
        mXJHandledItemFragment = new XJHandledWorkItemFragment();

        xjItemVP.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));

        int height = Util.getStatusBarHeight(context);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) xjItemVP.getLayoutParams();
        lp.height = getResources().getDisplayMetrics().heightPixels - height - DisplayUtil.dip2px(50 + 38, this);
        xjItemVP.setLayoutParams(lp);
        xjItemVP.setOffscreenPageLimit(2);
        xjItemTab.setCurrentTab(isFinished ? 1 : 0);
        xjItemVP.setCurrentItem(isFinished ? 1 : 0);
    }

    @Subscribe
    public void onReceiveFilterSearchEvent(FilterSearchTabEvent filterSearchTabEvent) {
        filterNameTv.setText(filterSearchTabEvent.tag.getTagName());
        deleteIv.setVisibility(View.VISIBLE);
        refreshData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(filterNameLl)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle = new Bundle();
                    Map<String, Object> param = new HashMap<>();
                    param.put(Constant.FilterSearchParam.AREA_ID, mXJAreaEntity.areaId);
                    bundle.putSerializable(Constant.IntentKey.FILTER_SEARCH_TYPE, SearchContentFactory.FilterType.DEVICE);
                    bundle.putString(Constant.IntentKey.FILTER_SEARCH_PARAM, GsonUtil.gsonString(param));
                    IntentRouter.go(context, Constant.Router.COMMON_FILETER_SEARCH, bundle);
                });

        deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterNameTv.setText(null);
                refreshData();
                deleteIv.setVisibility(View.GONE);
            }
        });

        leftBtn.setOnClickListener(v -> onBackPressed());

        xjItemTab.setOnTabChangeListener(current -> {
            xjItemVP.setCurrentItem(current);

        });

        xjItemVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (xjItemTab.getCurrentPosition() != position) {
                    xjItemTab.setCurrentTab(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        RxView.clicks(faultListBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
//                            Bundle bundle = new Bundle();
//                            bundle.putLong("taskId",mXJAreaEntity.taskId);
//                            bundle.putLong("workId",mXJAreaEntity.areaId);
//                            IntentRouter.go(context,Constant.Router.XJ_QXGL_LIST,bundle);
                            IntentRouter.go(context, Constant.Router.OFFLINE_YH_LIST);
                        }
                );
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        EventBus.getDefault().post(new RefreshEvent());
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        super.initData();
        LogUtil.i("initData XJWorkItemActivity");

        Flowable.fromIterable(EamApplication.dao().getXJWorkItemEntityDao().queryBuilder().where(XJWorkItemEntityDao.Properties.Ip.eq(EamApplication.getIp())).distinct().list())
                .compose(RxSchedulers.io_main())
                .subscribe(xjWorkItemEntity -> {
                    if (!deviceNames.keySet().contains(xjWorkItemEntity.equipmentId)) {
                        deviceNames.put(xjWorkItemEntity.equipmentId, xjWorkItemEntity.equipmentName);
                    }
                });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        this.mRefreshEvent = event;
        refreshData();
    }

    public void refreshData() {
        Log.i("mXJAreaEntity:", mXJAreaEntity.toString());
        presenterRouter.create(XJWorkItemAPI.class).getXJWorkItemList(mXJAreaEntity.areaId, mXJAreaEntity.taskId, getFilterName(), null);
    }

    private RefreshEvent mRefreshEvent;

    @Override
    public void getXJWorkItemListSuccess(XJWorkItemListEntity entity) {
        //暂时先不用这个方法来直接进行刷新
//        mXJUnhandledItemFragment.setUnhandledWorkItemList(entity.result);
//        if (null != mRefreshEvent && null != mRefreshEvent.action && mRefreshEvent.action.equals(Constant.RefreshAction.XJ_WORK_END)){
//            mXJUnhandledItemFragment.removeItem(mRefreshEvent.pos);
//        }else {
        mXJUnhandledItemFragment.setUnhandledWorkItemList(entity.result);
//        }


//        if (null != mRefreshEvent && null != mRefreshEvent.action && mRefreshEvent.action.equals(Constant.RefreshAction.XJ_WORK_REINPUT)){
//            mXJHandledItemFragment.removeItem(mRefreshEvent.pos);
//        }else {
        mXJHandledItemFragment.setHandledWorkItemList(entity.result);
//        }


        mRefreshEvent = null;
    }

    @Override
    public void getXJWorkItemListFailed(String errorMsg) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mXJUnhandledItemFragment.onActivityResult(requestCode, resultCode, data);
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return mXJUnhandledItemFragment;
                case 1:
                    return mXJHandledItemFragment;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
