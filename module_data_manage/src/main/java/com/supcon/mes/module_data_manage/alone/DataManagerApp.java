package com.supcon.mes.module_data_manage.alone;

import com.supcon.mes.middleware.EamApplication;

/**
 * Created by wangshizhan on 2018/4/28.
 * Email:wangshizhan@supcon.com
 */

public class DataManagerApp extends EamApplication {

    @Override
    public void onCreate() {
        setIsAlone(true);
        super.onCreate();

    }
}
