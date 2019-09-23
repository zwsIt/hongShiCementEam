package com.supcon.mes.module_yhgl.model.event;

import com.supcon.mes.middleware.model.bean.LubricateOilsEntity;

import java.util.List;

/**
 * SparePartEvent 润滑油EventBus回调
 * created by zhangwenshuai1 2018/9/6
 */
public class LubricateOilsEvent {
    private List<LubricateOilsEntity> list;
    private List<Long> dgDeletedIds;

    public LubricateOilsEvent(List<LubricateOilsEntity> list, List<Long> dgDeletedIds) {
        this.list = list;
        this.dgDeletedIds = dgDeletedIds;
    }

    public List<LubricateOilsEntity> getList() {
        return list;
    }

    public List<Long> getDgDeletedIds() {
        return dgDeletedIds;
    }
}
