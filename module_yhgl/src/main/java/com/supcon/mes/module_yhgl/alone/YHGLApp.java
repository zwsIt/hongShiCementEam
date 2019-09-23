package com.supcon.mes.module_yhgl.alone;


import com.supcon.mes.middleware.EamApplication;

/**
 * Created by wangshizhan on 2018/4/28.
 * Email:wangshizhan@supcon.com
 */

public class YHGLApp extends EamApplication {


    @Override
    public void onCreate() {
        setIsAlone(true);
        super.onCreate();

    }
}

