package com.supcon.mes.module_yhgl.model.event;

import com.supcon.mes.middleware.model.bean.MaintainEntity;

import java.util.List;

/**
 * SparePartEvent 维修人员EventBus回调
 * created by zhangwenshuai1 2018/9/6
 */
public class MaintenanceEvent {
    private List<MaintainEntity> list;

    private List<Long> dgDeletedIds;

    public MaintenanceEvent(List<MaintainEntity> list, List<Long> dgDeletedIds) {
        this.list = list;
        this.dgDeletedIds = dgDeletedIds;
    }


    public List<MaintainEntity> getList() {
        return list;
    }

    public List<Long> getDgDeletedIds() {
        return dgDeletedIds;
    }

}
