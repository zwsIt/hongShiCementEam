package com.supcon.mes.middleware.util;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.supcon.mes.middleware.service.KeepService;

/**
 * Created by wangshizhan on 2019/4/24
 * Email:wangshizhan@supcom.com
 */
public class ProcessHelper {

    private static final int GRAY_SERVICE_ID = 23232;

    private static class ProcessHelperHolder{
        private static ProcessHelper instance = new ProcessHelper();
    }


    public static ProcessHelper getInstance() {

        return ProcessHelperHolder.instance;
    }

    private ProcessHelper() {
    }

    public void startService(Context context){

        Intent innerIntent = new Intent(context, KeepService.class);
        context.startService(innerIntent);

    }

    public void setForceground(Service service){

        //设置service为前台服务，提高优先级
        if (Build.VERSION.SDK_INT < 18) {
            //Android4.3以下 ，此方法能有效隐藏Notification上的图标
            service.startForeground(GRAY_SERVICE_ID, new Notification());
        } else if(Build.VERSION.SDK_INT>18 && Build.VERSION.SDK_INT<25){
            //Android4.3 - Android7.0，此方法能有效隐藏Notification上的图标
            Intent innerIntent = new Intent(service, KeepService.class);
            service.startService(innerIntent);
            service.startForeground(GRAY_SERVICE_ID, new Notification());
        }else{
            //Android7.1 google修复了此漏洞，暂无解决方法（现状：Android7.1以上app启动后通知栏会出现一条"正在运行"的通知消息）
            service.startForeground(GRAY_SERVICE_ID, new Notification());
        }


    }


}
