package com.supcon.mes.module_login.alone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.supcon.mes.middleware.model.event.LoginValidEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wangshizhan on 2018/5/10.
 * Email:wangshizhan@supcon.com
 * 接收来自其他模块调试APP的广播，单独调试APP缺少登陆功能
 */

public class LoginValidEventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(new LoginValidEvent(true));
    }
}
