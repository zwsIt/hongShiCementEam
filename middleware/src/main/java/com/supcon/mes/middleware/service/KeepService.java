package com.supcon.mes.middleware.service;

import android.content.Intent;

import com.carmelo.library.KeepliveService;
import com.supcon.common.view.util.LogUtil;

/**
 * Created by wangshizhan on 2019/4/24
 * Email:wangshizhan@supcom.com
 */
public class KeepService extends KeepliveService {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i = super.onStartCommand(intent, flags, startId);
        //do something
        return i;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("KeepService onDestroy");
    }
}
