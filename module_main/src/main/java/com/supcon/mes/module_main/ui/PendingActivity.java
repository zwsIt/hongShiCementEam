package com.supcon.mes.module_main.ui;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.ui.fragment.CurrentWorkFragment;
import com.supcon.mes.module_main.ui.fragment.ProcessedFragment;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/5/11
 * Email zhangwenshuai1@supcon.com
 * Desc 工作提醒
 */
@Router(Constant.Router.PENDING_LIST)
public class PendingActivity extends BaseFragmentActivity {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("customTab")
    CustomTab customTab;
    @BindByTag("viewPager")
    NoScrollViewPager viewPager;

    private CurrentWorkFragment mCurrentWorkFragment;
    private ProcessedFragment mProcessedFragment;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_tab_list;
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
        initTab();
        initViewPager();
    }

    private void initTab() {
        customTab.addTab(context.getResources().getString(R.string.main_pending));
        customTab.addTab(context.getResources().getString(R.string.main_processed));
        customTab.setCurrentTab(0);
    }

    private void initViewPager() {
        mCurrentWorkFragment = new CurrentWorkFragment();
        mProcessedFragment = new ProcessedFragment();
        viewPager.setAdapter(new InnerFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        customTab.setOnTabChangeListener(new CustomTab.OnTabChangeListener() {
            @Override
            public void onTabChanged(int current) {
                viewPager.setCurrentItem(current);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (customTab.getCurrentPosition() != i){
                    customTab.setCurrentTab(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
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
                    return mProcessedFragment;
                case 0:
                default:
                    return mCurrentWorkFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
