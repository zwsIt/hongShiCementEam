package com.supcon.mes.module_main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseMultiFragmentActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AreaController;
import com.supcon.mes.middleware.controller.DepartmentController;
import com.supcon.mes.middleware.controller.OnlineStaffListController;
import com.supcon.mes.middleware.controller.RepairGroupController;
import com.supcon.mes.middleware.controller.SystemCodeController;
import com.supcon.mes.middleware.controller.UserInfoListController;
import com.supcon.mes.middleware.model.event.AppExitEvent;
import com.supcon.mes.middleware.model.event.DownloadDataEvent;
import com.supcon.mes.middleware.model.event.LoginValidEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_login.controller.SilentLoginController;
import com.supcon.mes.module_login.service.HeartBeatService;
import com.supcon.mes.module_main.BuildConfig;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.ui.fragment.EamFragment;
import com.supcon.mes.module_main.ui.fragment.MineFragment;
import com.supcon.mes.module_main.ui.fragment.WorkFragment;
import com.supcon.mes.module_txl.ui.fragment.TxlListFragment;
import com.supcon.mes.push.controller.DeviceTokenController;
import com.supcon.mes.push.controller.PendingController;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangshizhan on 2017/8/11.
 */

@Router(Constant.Router.MAIN_REDLION)
@Controller(value = {
        SystemCodeController.class, AreaController.class, RepairGroupController.class,
        UserInfoListController.class, DepartmentController.class, DeviceTokenController.class,
        SilentLoginController.class, PendingController.class, OnlineStaffListController.class})
public class MainActivity extends BaseMultiFragmentActivity {

    @BindByTag("tabRadioGroup")
    RadioGroup tabRadioGroup;

    @BindByTag("logo")
    ImageView logo;

    private String initIp = ""; // 记录原超时IP

    private NFCHelper nfcHelper;
    private WorkFragment workFragment;

    @Override
    protected int getLayoutID() {
        return R.layout.hs_ac_main;
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
                    EventBus.getDefault().post(new NFCEvent(nfc, "Main"));
                }
            });
        }

        initIp = EamApplication.getIp();
        Api.getInstance().setDebug(BuildConfig.DEBUG);
        LogUtil.showLog = BuildConfig.DEBUG;

        PushAgent.getInstance(context).onAppStart();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginValid(LoginValidEvent event) {
        LogUtil.w("MainActivity", String.valueOf(event.isLoginValid()));
        if (event.isLoginValid()) {
            getController(SilentLoginController.class).silentLogin(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downloadModules(DownloadDataEvent event) {
        List<String> modules = event.getModules();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constant.IntentKey.DOWNLOAD_MODULES, (ArrayList<String>) modules);
        IntentRouter.go(context, Constant.Router.SJXZ, bundle);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppExit(AppExitEvent event) {
        System.exit(0);
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fragmentLayout;
    }

    @Override
    public void createFragments() {
        workFragment = new WorkFragment();
        TxlListFragment txlListFragment = new TxlListFragment();
        txlListFragment.fitInstatusBarEnable(true);
        EamFragment eamFragment = new EamFragment();
        MineFragment mineFragment = new MineFragment();
        fragments.add(workFragment);
        fragments.add(txlListFragment);
        fragments.add(eamFragment);
        fragments.add(mineFragment);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        workFragment.onTouch(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void initView() {
        super.initView();
        showFragment(0);
        if (EamApplication.isHailuo()) {
            logo.setImageResource(R.drawable.tabbar_logo_hl);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!initIp.equals(EamApplication.getIp())) {
            getController(SystemCodeController.class).onInit();
            getController(AreaController.class).onInit();
            getController(DepartmentController.class).onInit();
            getController(RepairGroupController.class).onInit();
            getController(UserInfoListController.class).onInit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        tabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.tabRadioBtn1) {
                    showFragment(0);
                } else if (checkedId == R.id.tabRadioBtn2) {
                    showFragment(1);
                } else if (checkedId == R.id.tabRadioBtn3) {
                    showFragment(2);
                } else if (checkedId == R.id.tabRadioBtn4) {
                    showFragment(3);
                }
            }
        });
        
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取到Tag对象
        if (nfcHelper != null)
            nfcHelper.dealNFCTag(intent);
    }


    private long lastBackTime = 0L;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackTime > 2000) {
            lastBackTime = System.currentTimeMillis();
            SnackbarHelper.showMessage(rootView, "再按一次返回将退出APP");
        } else {
            System.exit(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (nfcHelper != null)
            nfcHelper.onPauseNFC(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HeartBeatService.stopLoginLoop(this);
        DeviceManager.getInstance().updateDatabase();
        EventBus.getDefault().unregister(this);
        if (nfcHelper != null) {
            nfcHelper.release();
        }
    }

    //1.触摸事件接口
    public interface WorkOnTouchListener {
        boolean onTouch(MotionEvent ev);
    }
    
    @Override
    public void showFragment(int selectIndex) {
        if(selectIndex ==0) StatusBarUtils.setWindowStatusBarColor(this,R.color.transparent);
        else StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        super.showFragment(selectIndex);
    }
    
}
