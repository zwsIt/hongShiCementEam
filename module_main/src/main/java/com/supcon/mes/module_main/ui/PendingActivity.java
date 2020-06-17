package com.supcon.mes.module_main.ui;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CustomFilterBean;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.ui.fragment.CurrentWorkFragment;
import com.supcon.mes.module_main.ui.fragment.ProcessedFragment;
import com.supcon.mes.module_main.ui.fragment.SubordinatesWorkFragment;
import com.supcon.mes.module_main.ui.util.FilterHelper;

import org.w3c.dom.Text;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/5/11
 * Email zhangwenshuai1@supcon.com
 * Desc 工作提醒新
 */
@Router(Constant.Router.PENDING_LIST)
public class PendingActivity extends BaseFragmentActivity {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightTv")
    TextView rightTv;
    @BindByTag("tabLayout")
    TabLayout tabLayout;
    @BindByTag("viewPager")
    NoScrollViewPager viewPager;
    @BindByTag("tableFilter")
    CustomFilterView<CustomFilterBean> tableFilter;

    private CurrentWorkFragment mCurrentWorkFragment; // 待办
    private ProcessedFragment mProcessedFragment; // 已处理
    private SubordinatesWorkFragment mSubordinatesFragment; // 我的下属

    @Override
    protected int getLayoutID() {
        return R.layout.main_ac_pending_list;
    }
    @Override
    protected void onInit() {
        super.onInit();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.main_current_work));
        rightTv.setText(context.getResources().getString(R.string.main_warn));
//        initTab();
        initViewPager();
    }

    private void initTab() {
//        customTab.addTab(context.getResources().getString(R.string.main_pending));
//        customTab.addTab(context.getResources().getString(R.string.main_processed));
//        customTab.setCurrentTab(0);
    }

    private void initViewPager() {
        mCurrentWorkFragment = new CurrentWorkFragment();
        mProcessedFragment = new ProcessedFragment();
        mSubordinatesFragment = new SubordinatesWorkFragment();
        viewPager.setAdapter(new InnerFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager,true);
    }

    @Override
    protected void initData() {
        super.initData();
        initFilter();
    }
    private void initFilter() {
        tableFilter.setData(FilterHelper.createTableTypeList());
        tableFilter.setCurrentPosition(0);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        rightTv.setOnClickListener(v -> {
            IntentRouter.go(context, Constant.Router.WARN_PENDING_LIST);
            finish();
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
//                if (customTab.getCurrentPosition() != i){
//                    customTab.setCurrentTab(i);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tableFilter.setFilterSelectChangedListener(filterBean -> {
            CustomFilterBean customFilterBean = (CustomFilterBean)filterBean;
            mCurrentWorkFragment.doFilter(customFilterBean.id);
            mProcessedFragment.doFilter(customFilterBean.id);
            mSubordinatesFragment.doFilter(customFilterBean.id);
        });
    }

    private class InnerFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public InnerFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 1:
                    return mProcessedFragment;
                case 2:
                    return mSubordinatesFragment;
                case 0:
                default:
                    return mCurrentWorkFragment;
            }

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return getResources().getText(R.string.main_processed);
                case 2:
                    return getResources().getText(R.string.main_subordinates);
                case 0:
                default:
                    return getResources().getString(R.string.main_pending);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
