package com.supcon.mes.push;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.supcon.common.view.util.LogUtil;
import com.umeng.message.UmengMessageService;
import org.android.agoo.common.AgooConstants;

public class UmengNotificationService extends UmengMessageService {
    @Override
    public void onMessage(Context context, Intent intent) {
        LogUtil.i("UmengNotificationService onMessage");
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Intent intent1 = new Intent();
        intent1.setClass(context, MyNotificationService.class);
        intent1.putExtra("UmengMsg", message);
        context.startService(intent1);
    }
}
