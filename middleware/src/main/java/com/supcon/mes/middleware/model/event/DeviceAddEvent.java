package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2017/11/28.
 * Email:wangshizhan@supcon.com
 */

public class DeviceAddEvent extends BaseEntity {

    private String devices;


    public DeviceAddEvent(String devices){
        this.devices = devices;
    }

    public String getDeviceEntity() {
        return devices;
    }

}
