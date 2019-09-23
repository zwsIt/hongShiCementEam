package com.supcon.mes.middleware.util;

import android.text.TextUtils;

import com.supcon.common.view.App;
import com.supcon.common.view.util.NetWorkUtil;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.model.event.LoginValidEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wangshizhan on 2018/4/3.
 * Email:wangshizhan@supcon.com
 */

public class ErrorMsgHelper {

    public static String msgParse(String errorMsg){


        if(TextUtils.isEmpty(errorMsg)){

            return "请求错误！";

        }
        String msg2 = null;

        if(errorMsg.contains("404")){
            msg2 = "服务不存在，未部署或ip设置错误！";
        }
        else if(errorMsg.contains("403")){
            msg2 = "访问被拒绝，模块未授权！";
        }
        else if(errorMsg.contains("401")){
            EventBus.getDefault().post(new LoginValidEvent(true));
            msg2= "登陆失效,重新登陆中!";
        }
//        else if(errorMsg.contains("500")){
//            msg2 = "服务器错误！";
//        }
        else if (errorMsg.contains("SocketTimeoutException")) {
            msg2 = "请求超时，请检查网络情况！";
        } else if (errorMsg.contains("ConnectException")) {
            if (NetWorkUtil.isWifiConnected(App.getAppContext())) {
                msg2 = "无法连接到服务器 " + MBapApp.getIp() + ":" + MBapApp.getPort() + "!";
            } else {
                msg2 = "WIFI已断开!";
            }

        } else if (errorMsg.contains("No route to host")) {
            msg2 = "无法连接服务器!";
        } else if (errorMsg.contains("No address associated with hostname")) {
            msg2 = "无法解析服务器地址，请检查设置！";
        } else if (errorMsg.contains("Exception")) {
            msg2 = "获取数据失败！";
        } else {
            return errorMsg;
        }

        return msg2;
    }

}
