package com.supcon.mes.middleware.alone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.mbap.network.AloneHeadInterceptor;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.middleware.model.event.LoginValidEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;

/**
 * Created by wangshizhan on 2018/5/10.
 * Email:wangshizhan@supcon.com
 */

public class AloneManager {

    private String hostPackageName;
    private Context mHostContext;

    public AloneManager(String hostPackageName){
        try {
            // 尝试创建A应用的Context
            mHostContext = MBapApp.getAppContext().createPackageContext(hostPackageName, CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void setup(){

        resetHttp();
        startHeartbeat();
        EventBus.getDefault().register(this);

    }

    private void startHeartbeat() {



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginValidEvent event){
        if(event.isLoginValid()){

            Intent intent = new Intent();
            intent.setAction("AloneAppLoginValid");
            mHostContext.sendBroadcast(intent);
        }
    }


    private void resetHttp(){

        String ip           = SharedPreferencesUtils.getParam(mHostContext, MBapConstant.SPKey.IP, "");
        String port         = SharedPreferencesUtils.getParam(mHostContext, MBapConstant.SPKey.PORT, "");
        String url          = SharedPreferencesUtils.getParam(mHostContext, MBapConstant.SPKey.URL, "");
        String userName     = SharedPreferencesUtils.getParam(mHostContext, MBapConstant.SPKey.USER_NAME, "");
        String pwd          = SharedPreferencesUtils.getParam(mHostContext, MBapConstant.SPKey.PWD, "");
        boolean urlEnable   = SharedPreferencesUtils.getParam(mHostContext, MBapConstant.SPKey.URL_ENABLE, false);

        SharedPreferencesUtils.setParam(AloneApp.getAppContext(), MBapConstant.SPKey.IP, ip);
        SharedPreferencesUtils.setParam(AloneApp.getAppContext(), MBapConstant.SPKey.PORT, port);
        SharedPreferencesUtils.setParam(AloneApp.getAppContext(), MBapConstant.SPKey.URL, url);
        SharedPreferencesUtils.setParam(AloneApp.getAppContext(), MBapConstant.SPKey.USER_NAME, userName);
        SharedPreferencesUtils.setParam(AloneApp.getAppContext(), MBapConstant.SPKey.PWD, pwd);
        SharedPreferencesUtils.setParam(AloneApp.getAppContext(), MBapConstant.SPKey.URL_ENABLE, urlEnable);

        Api.getInstance().setHeadInterceptor(new AloneHeadInterceptor(mHostContext));
        Api.getInstance().rebuild();
    }

    public void release(){

        EventBus.getDefault().unregister(this);

    }

    public Context getHostContext() {
        return mHostContext;
    }
}
