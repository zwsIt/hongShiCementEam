package com.supcon.mes.module_olxj.model.bean;

import com.supcon.common.com_http.BaseEntity;

public class OLXJExemptionEntity extends BaseEntity {

    public String concluse;
    public Long id;

    public OLXJWorkItem itemID;
    public OLXJWorkItem workHeadID;
    public OLXJWorkItemEntity nextItemID;

    public OLXJWorkItemEntity getNextItemID() {
        if (nextItemID == null) {
            nextItemID = new OLXJWorkItemEntity();
        }
        return nextItemID;
    }

    public OLXJWorkItem getItemID() {
        if (itemID == null) {
            itemID = new OLXJWorkItem();
        }
        return itemID;
    }
}
