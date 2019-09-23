package com.supcon.mes.middleware.model.event;

import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.SparePartRefEntity;

/**
 * SparePartAddEvent 备件添加回调Event
 * created by zhangwenshuai1 2018/10/26
 */
public class SparePartAddEvent {
    private boolean flag;
    private SparePartRefEntity sparePartRefEntity;

    public SparePartAddEvent() {
    }

    public SparePartAddEvent(boolean flag, SparePartRefEntity sparePartRefEntity) {
        this.flag = flag;
        this.sparePartRefEntity = sparePartRefEntity;
    }

    public boolean isFlag() {
        return flag;
    }

    public SparePartRefEntity getSparePartRefEntity() {
        return sparePartRefEntity;
    }
}
