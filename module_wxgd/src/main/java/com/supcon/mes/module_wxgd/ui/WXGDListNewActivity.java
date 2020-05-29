package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.ImageButton;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.activity.BaseMultiFragmentActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.ModulePermissonCheckController;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.ui.fragment.WorkFinishedListFragment;
import com.supcon.mes.module_wxgd.ui.fragment.WorkPendingListFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/5/29
 * Email zhangwenshuai1@supcon.com
 * Desc 维修工单新
 */
@Router(Constant.Router.WXGD_LIST_NEW)
public class WXGDListNewActivity extends BaseFragmentActivity {
    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("customSearchView")
    CustomSearchView titleSearchView;
    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customTab")
    CustomTab customTab;
    @BindByTag("viewPager")
    NoScrollViewPager viewPager;

    private WorkPendingListFragment mWorkPendingListFragment;
    private WorkFinishedListFragment mWorkFinishedListFragment;
    private Long deploymentId;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_tab_search_list;
    }
    @Override
    protected void onInit() {
        super.onInit();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        searchTitleBar.setTitleText(context.getResources().getString(R.string.workName));
        titleSearchView.setHint(getString(R.string.middleware_input_eam_name));
        initTab();
        initViewPager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initTab() {
        customTab.addTab(context.getResources().getString(R.string.middleware_pending));
        customTab.addTab(context.getResources().getString(R.string.work_finished));
        customTab.setCurrentTab(0);
    }

    private void initViewPager() {
        mWorkPendingListFragment = new WorkPendingListFragment();
        mWorkFinishedListFragment = new WorkFinishedListFragment();
        viewPager.setAdapter(new InnerFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void initData() {
        super.initData();
        ModulePermissonCheckController mModulePermissionCheckController = new ModulePermissonCheckController();
        mModulePermissionCheckController.checkModulePermission(EamApplication.getUserName().toLowerCase(), "work", result -> {
            if (result == null){
                searchTitleBar.disableRightBtn();
            }
            deploymentId = result;
        },null);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        searchTitleBar.leftBtn().setOnClickListener(v -> finish());
        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (deploymentId != null) {
                        WXGDEntity wxgdEntity = new WXGDEntity();
                        wxgdEntity.id = -1L;
                        wxgdEntity.workSource = new SystemCodeEntity("BEAM2003/08", "手动添加", "BEAM2003", null, null, 0, EamApplication.getIp());
                        wxgdEntity.pending = new PendingEntity();
                        wxgdEntity.pending.deploymentId = deploymentId;
                        wxgdEntity.pending.taskDescription = "派工";
                        wxgdEntity.eamID = new EamEntity();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY,wxgdEntity);
                        IntentRouter.go(context,Constant.Router.WXGD_DISPATCHER,bundle);
                    } else {
                        ToastUtils.show(context, "当前用户并未拥有创建单据权限！");
                    }
                });
        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    mWorkPendingListFragment.doSearch(charSequence.toString().trim());
                    mWorkFinishedListFragment.doSearch(charSequence.toString().trim());
                });
        KeyExpandHelper.doActionSearch(titleSearchView.editText(), true, () -> {
            mWorkPendingListFragment.doSearch(titleSearchView.getInput().trim());
            mWorkFinishedListFragment.doSearch(titleSearchView.getInput().trim());
        });
        customTab.setOnTabChangeListener(new CustomTab.OnTabChangeListener() {
            @Override
            public void onTabChanged(int current) {
                viewPager.setCurrentItem(current);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int i) {
                if (customTab.getCurrentPosition() != i){
                    customTab.setCurrentTab(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });
    }

    private class InnerFragmentPagerAdapter extends FragmentPagerAdapter {

        public InnerFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 1:
                    return mWorkFinishedListFragment;
                case 0:
                default:
                    return mWorkPendingListFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
