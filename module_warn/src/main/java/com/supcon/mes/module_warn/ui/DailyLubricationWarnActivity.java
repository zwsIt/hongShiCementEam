package com.supcon.mes.module_warn.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.event.TabEvent;
import com.supcon.mes.module_warn.ui.adapter.DailyLubricationWarnAdapter;
import com.supcon.mes.module_warn.ui.fragment.DailyLubricateReceiveTaskFragment;
import com.supcon.mes.module_warn.ui.fragment.DailyLubricateTaskFragment;
import com.supcon.mes.module_warn.ui.fragment.TemporaryTaskFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 日常润滑
 */
@Router(Constant.Router.DAILY_LUBRICATION_EARLY_WARN)
public class DailyLubricationWarnActivity extends BaseFragmentActivity {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("tablayout")
    TabLayout tablayout;
    @BindByTag("sbdaViewPager")
    NoScrollViewPager sbdaViewPager;

    private NFCHelper nfcHelper;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_daily_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        nfcHelper = NFCHelper.getInstance();
        if (nfcHelper != null) {
            nfcHelper.setup(this);
            nfcHelper.setOnNFCListener(new NFCHelper.OnNFCListener() {
                @Override
                public void onNFCReceived(String nfc) {
                    LogUtil.d("NFC Received : " + nfc);
                    EventBus.getDefault().post(new NFCEvent(nfc));
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("日常润滑任务");
        tablayout.setupWithViewPager(sbdaViewPager);

        DailyLubricationWarnAdapter dailyLubricationWarnAdapter = new DailyLubricationWarnAdapter(getSupportFragmentManager());
        dailyLubricationWarnAdapter.addFragment(DailyLubricateTaskFragment.newInstance(), "日常润滑任务领取");
        dailyLubricationWarnAdapter.addFragment(DailyLubricateReceiveTaskFragment.newInstance(), "日常润滑任务执行");
        dailyLubricationWarnAdapter.addFragment(TemporaryTaskFragment.newInstance(), "临时润滑任务");
        sbdaViewPager.setAdapter(dailyLubricationWarnAdapter);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void tabChange(TabEvent tabEvent) {
        tablayout.getTabAt(1).select();
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
}
