package com.supcon.mes.push.event;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by wangshizhan on 2018/8/16
 * Email:wangshizhan@supcom.com
 */
public class DeviceTokenEvent implements Serializable {

    static final long serialVersionUID=536871008L;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    private String deviceToken;
    private boolean isLogin = true;

    public DeviceTokenEvent(String deviceToken){
        this.deviceToken = deviceToken;
    }

    public DeviceTokenEvent(String deviceToken, boolean isLogin){
        this.deviceToken = deviceToken;
        this.isLogin = isLogin;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
