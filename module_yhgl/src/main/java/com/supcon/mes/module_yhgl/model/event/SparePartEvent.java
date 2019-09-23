package com.supcon.mes.module_yhgl.model.event;

import com.supcon.mes.middleware.model.bean.SparePartEntity;

import java.util.List;

/**
 * SparePartEvent 备件EventBus回调
 * created by zhangwenshuai1 2018/9/6
 */
public class SparePartEvent {
    private List<SparePartEntity> list;
    private List<Long> dgDeletedIds;
    private boolean isDeliveried; // 是否生成领用出库

    public SparePartEvent(List<SparePartEntity> list, List<Long> dgDeletedIds) {
        this.list = list;
        this.dgDeletedIds = dgDeletedIds;
    }

    public SparePartEvent(List<SparePartEntity> list, List<Long> dgDeletedIds, boolean isDeliveried) {
        this.list = list;
        this.dgDeletedIds = dgDeletedIds;
        this.isDeliveried = isDeliveried;
    }

    public List<SparePartEntity> getList() {
        return list;
    }

    public List<Long> getDgDeletedIds() {
        return dgDeletedIds;
    }

    public boolean isDeliveried() {
        return isDeliveried;
    }
}
