package com.supcon.mes.module_sparepartapply_hl.model.event;

import com.supcon.mes.middleware.model.bean.SparePartReceiveEntity;

import java.util.List;

/**
 * SparePartApplyDetailEvent 备件领用申请EventBus回调
 * created by zhangwenshuai1 2018/9/6
 */
public class SparePartApplyDetailEvent {
    private List<SparePartReceiveEntity> list;
    private List<Long> dgDeletedIds;

    public SparePartApplyDetailEvent(List<SparePartReceiveEntity> list, List<Long> dgDeletedIds) {
        this.list = list;
        this.dgDeletedIds = dgDeletedIds;
    }

    public List<SparePartReceiveEntity> getList() {
        return list;
    }

    public List<Long> getDgDeletedIds() {
        return dgDeletedIds;
    }
}
