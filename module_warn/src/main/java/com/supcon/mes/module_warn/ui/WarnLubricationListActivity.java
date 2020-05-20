package com.supcon.mes.module_warn.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.UserPowerCheckController;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.ui.fragment.RunTimeFragment;
import com.supcon.mes.module_warn.ui.fragment.TimeFrequencyFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/5/18
 * Email zhangwenshuai1@supcon.com
 * Desc 预警润滑
 */
@Router(Constant.Router.LUBRICATION_EARLY_WARN)
@Controller(value = {UserPowerCheckController.class})
public class WarnLubricationListActivity extends BaseFragmentActivity {
    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;
    @BindByTag("customSearchView")
    CustomSearchView titleSearchView;
    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customTab")
    CustomTab customTab;
    @BindByTag("viewPager")
    NoScrollViewPager viewPager;

    private TimeFrequencyFragment mTimeFrequencyFragment;
    private RunTimeFragment mRunTimeFragment;
    private NFCHelper nfcHelper;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_tab_search_list;
    }
    @Override
    protected void onInit() {
        super.onInit();

        nfcHelper = NFCHelper.getInstance();
        if (nfcHelper != null) {
            nfcHelper.setup(this);
            nfcHelper.setOnNFCListener(nfc -> {
                LogUtil.d("NFC Received : " + nfc);
                Map<String, Object> nfcJson = Util.gsonToMaps(nfc);
                if (nfcJson.get("textRecord") == null) {
                    ToastUtils.show(context, "标签内容空！");
                    return;
                }
                titleSearchView.setInput((String) nfcJson.get("textRecord"));
//                    EventBus.getDefault().post(new NFCEvent(nfc));
            });
        }
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        searchTitleBar.setTitleText(context.getResources().getString(R.string.warn_lubrication));
        titleSearchView.setHint("请输入设备编码");
        searchTitleBar.disableRightBtn();
        initTab();
        initViewPager();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取到Tag对象
        if (nfcHelper != null)
            nfcHelper.dealNFCTag(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcHelper != null)
            nfcHelper.onPauseNFC(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (nfcHelper != null) {
            nfcHelper.release();
        }
    }

    private void initTab() {
        customTab.addTab(context.getResources().getString(R.string.time_frequency));
        customTab.addTab(context.getResources().getString(R.string.run_time));
        customTab.setCurrentTab(0);
    }

    private void initViewPager() {
        mTimeFrequencyFragment = new TimeFrequencyFragment();
        mRunTimeFragment = new RunTimeFragment();
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
        searchTitleBar.leftBtn().setOnClickListener(v -> finish());
        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    mTimeFrequencyFragment.doSearch(charSequence.toString().trim());
                    mRunTimeFragment.doSearch(charSequence.toString().trim());
                });
        KeyExpandHelper.doActionSearch(titleSearchView.editText(), true, () -> {
            mTimeFrequencyFragment.doSearch(titleSearchView.getInput().trim());
            mRunTimeFragment.doSearch(titleSearchView.getInput().trim());
        });
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
                    return mRunTimeFragment;
                case 0:
                default:
                    return mTimeFrequencyFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
