package com.supcon.mes.module_sbda.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.fragment.BaseFragment;
import com.supcon.common.view.base.fragment.BasePresenterFragment;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.module_sbda.R;

/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/5/17.
 * Desc: TODO 这里真的想换一个思路,之前的代码因为多次维护所以失去了之前整洁,优美和逻辑性
 * 首先,原来的Tab和ViewPager这两个我将其视作一个大的Fragment,与SearchFragment公用一块区域
 * 然后是标题导航搜索栏,这里我希望设置回调接口用于设置对应的点击触发事件,组件相关的信息交互通过EventBus进行
 * 每个视图处理自己模块对应的事件,因为我并不希望所有的逻辑都杂在同一个组件中进行处理,太难看了
 * <p>
 * 大概的思路如上,接下来是AddDeviceFragment需要执行的相关逻辑操作,这个Fragment的作用是充当原来的TabLayout+ViewPager
 * 需要处理的事情是:
 * 1.接收搜索的结果,并在自己对应的item上作出标识
 * 2.发送自己的搜索状态,并在底部的组件中显示对应的已经选中的item的数目
 * 3.通过Fragment改版之后需要注意的就是这些,其余的相关功能并没有明显的改变
 */
public class AddDeviceFragment extends BaseFragment {
    private RecentDeviceFragment mRecentDeviceFragment;
    private MyDeviceFragment mMyDeviceFragment;

    @BindByTag("tab")
    CustomTab tab;

    @BindByTag("viewPager")
    ViewPager viewPager;

    @Override
    protected void initView() {
        super.initView();
        initTabs();
        initViewPager();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_add_device;
    }

    private void initTabs() {
        if(tab == null) LogUtil.e("tab is null");
        else LogUtil.e("tab is not null");
        tab.addTab("我的设备");
        tab.addTab("常用设备");

        tab.setCurrentTab(0);

    }

    private void initViewPager() {

        mMyDeviceFragment = new MyDeviceFragment();
        mRecentDeviceFragment = new RecentDeviceFragment();

        viewPager.setAdapter(new MyFragmentAdapter(getActivity().getSupportFragmentManager()));

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
        lp.height = getResources().getDisplayMetrics().heightPixels - DisplayUtil.dip2px(40 + 50 + 28, getContext());
        viewPager.setLayoutParams(lp);
        viewPager.setOffscreenPageLimit(1);
    }

    @Override
    protected void initListener() {
        super.initListener();
        tab.setOnTabChangeListener(current -> {
            viewPager.setCurrentItem(current);
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //tabLayout和viewpager两者联动
                if (tab.getCurrentPosition() != position) {
                    tab.setCurrentTab(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {

        MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return mMyDeviceFragment;
                case 1:
                    return mRecentDeviceFragment;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
