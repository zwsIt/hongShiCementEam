package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.RunStateDeviceEntity;

/**
 * Created by wangshizhan on 2017/11/28.
 * Email:wangshizhan@supcon.com
 */

public class DeviceSaveEvent extends BaseEntity {

    private RunStateDeviceEntity mRunStateDeviceEntity;


    public DeviceSaveEvent(RunStateDeviceEntity runStateDeviceEntity){
        this.mRunStateDeviceEntity = runStateDeviceEntity;
    }

    public RunStateDeviceEntity getRunStateDeviceEntity() {
        return mRunStateDeviceEntity;
    }

    public DeviceSaveEvent(){

    }
}
