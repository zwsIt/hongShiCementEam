package com.supcon.mes.module_sbda.alone;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.util.DeviceManager;

/**
 * Created by wangshizhan on 2018/5/3.
 * Email:wangshizhan@supcon.com
 */

public class SBDAApp extends EamApplication {
    @Override
    public void onCreate() {
        setIsAlone(true);
        super.onCreate();

        DeviceManager.getInstance().init();
    }
}
