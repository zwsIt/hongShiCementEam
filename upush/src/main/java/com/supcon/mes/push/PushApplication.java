package com.supcon.mes.push;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.push.controller.PushController;

/**
 * Created by wangshizhan on 2019/8/9
 * Email:wangshizhan@supcom.com
 */
public class PushApplication extends EamApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initPush();
    }

    private void initPush() {

        PushController pushController = new PushController();
        pushController.onInit();

    }
}
