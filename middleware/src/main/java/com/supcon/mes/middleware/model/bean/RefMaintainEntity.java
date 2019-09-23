package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

public class RefMaintainEntity extends BaseEntity {

    public Long id;
    public Long cid;
    public String claim;//要求
    public String content;//内容

    public Long lastTime;
    public Long nextTime;
    public Float lastDuration;//上次润滑时长
    public Float nextDuration;//下次润滑时长
    public Long period;
    public ValueEntity periodType;
    public ValueEntity periodUnit;

    public SparePartId sparePartId;//备件编码
    public AccessoryEamId accessoryEamId;//附属设备

    public AccessoryEamId getAccessoryEamId() {
        if (accessoryEamId == null) {
            accessoryEamId = new AccessoryEamId();
        }
        return accessoryEamId;
    }

    public SparePartId getSparePartId() {
        if (sparePartId == null) {
            sparePartId = new SparePartId();
        }
        return sparePartId;
    }

}
