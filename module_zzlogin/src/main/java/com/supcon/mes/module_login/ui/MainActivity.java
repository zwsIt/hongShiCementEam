package com.supcon.mes.module_login.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.activity.BaseMultiFragmentActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.BuildConfig;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.DataModule;
import com.supcon.mes.middleware.controller.AreaController;
import com.supcon.mes.middleware.controller.DepartmentController;
import com.supcon.mes.middleware.controller.RepairGroupController;
import com.supcon.mes.middleware.controller.SystemCodeController;
import com.supcon.mes.middleware.controller.UserInfoListController;
import com.supcon.mes.middleware.model.event.AppExitEvent;
import com.supcon.mes.middleware.model.event.DownloadDataEvent;
import com.supcon.mes.middleware.model.event.LoginValidEvent;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.controller.SilentLoginController;
import com.supcon.mes.module_login.service.HeartBeatService;
import com.supcon.mes.module_login.ui.fragment.MineFragment;
import com.supcon.mes.module_login.ui.fragment.WorkFragment;
import com.supcon.mes.push.controller.DeviceTokenController;
import com.supcon.mes.push.controller.PendingController;
import com.supcon.mes.push.event.DeviceTokenEvent;
import com.supcon.mes.push.event.PushOpenEvent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;


/**
 * Created by wangshizhan on 2017/8/11.
 */

@Router(Constant.Router.MAIN)
@Controller(value = {SystemCodeController.class, AreaController.class, RepairGroupController.class,
        UserInfoListController.class, DepartmentController.class, DeviceTokenController.class,
        SilentLoginController.class, PendingController.class})
public class MainActivity extends BaseMultiFragmentActivity {

    @BindByTag("drawerLayout")
    DrawerLayout drawerLayout;


    @BindByTag("leftDrawer")
    LinearLayout leftDrawer;

    WorkFragment workFragment;
    MineFragment mineFragment;

    private String initIp = ""; // 记录原超时IP

    @Override
    protected void onInit() {
        Api.getInstance().setDebug(BuildConfig.DEBUG);
        LogUtil.showLog = BuildConfig.DEBUG;
        super.onInit();
        EventBus.getDefault().register(this);

        initIp = EamApplication.getIp();


/*        //通讯录同步
        mCommonSearchStaffController = new ContractController();
        mCommonSearchStaffController.listStaff("", 1);*/

//        getController(SystemCodeController.class).onInit();
//        getController(AreaController.class).onInit();
//        getController(DepartmentController.class).onInit();
//        getController(RepairGroupController.class).onInit();
//        getController(UserInfoListController.class).onInit();

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
        return R.id.container;
    }

    @Override
    public void createFragments() {
//        fragments.add(new WorkFragment());
//        showFragment(0);
        workFragment = new WorkFragment();
        mineFragment = new MineFragment();

        fragments.add(workFragment);

        Flowable.timer(100, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(aLong -> {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.leftDrawer, mineFragment).commit();
                });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_main;
    }

    @Override
    protected void initView() {
        super.initView();
        showFragment(0);
//        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

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
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mSB2Controller.onDestroy();
        HeartBeatService.stopLoginLoop(this);
        DeviceManager.getInstance().updateDatabase();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void initListener() {
        super.initListener();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        super.initData();
//        checkData();
        Flowable.timer(10, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.leftDrawer, new MineFragment()).commit();
                    }
                });
    }

    public void toggleDrawer() {

        if (!drawerLayout.isDrawerOpen(leftDrawer)) {
            drawerLayout.openDrawer(leftDrawer);
        } else {
            drawerLayout.closeDrawer(leftDrawer);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void checkData() {
        if (EamApplication.getAccountInfo().userId != 0 /*|| !EamApplication.getAccountInfo().ip.equals(MBapApp.getIp())*/) {
            List<String> downloadModules = new ArrayList<>();
//            downloadModules.add(DataModule.XJ_BASE.getModuelName());
//            downloadModules.add(DataModule.EAM_BASE.getModuelName());
            downloadModules.add(DataModule.EAM_DEVICE.getModuelName());
            DownloadDataEvent event = new DownloadDataEvent(downloadModules);
            EventBus.getDefault().post(event);
        }
//        else if(SharedPreferencesUtils.getParam(context, Constant.SPKey.EAM_DEVICE_NEED_DOWNLOAD, false)){
//            List<String> downloadModules = new ArrayList<>();
//            downloadModules.add(DataModule.EAM_BASE.getModuelName());
//            downloadModules.add(DataModule.EAM_DEVICE.getModuelName());
//            DownloadDataEvent event = new DownloadDataEvent(downloadModules);
//            EventBus.getDefault().post(event);
//        }
        else {
            DeviceManager.getInstance().init();
        }
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

}
