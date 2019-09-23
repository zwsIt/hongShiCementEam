package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * 备件领用申请记录
 */
public class SparePartReceiveEntity extends BaseEntity {

    public Long id;
    public Float origDemandQuity;//申请数量
    public Float currDemandQuity;//领用量
    public String remark;//备注
    public SparePartId sparePartId;//备件编码

    public SparePartId getSparePartId() {
        if (sparePartId == null) {
            sparePartId = new SparePartId();
        }
        return sparePartId;
    }
}
