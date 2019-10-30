package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * 备件领用申请明细
 */
public class SparePartReceiveEntity extends BaseEntity {

    public Long id;
    public Double origDemandQuity;//申请数量
    public Double currDemandQuity;//领用量
    public String remark;//备注
    public Good sparePartId;//备件编码
    public Double price; // 单价
    public Double total; // 总价
    public Double defference; // 差额
    public Double actualNum; // 实发数量


    public Good getSparePartId() {
        if (sparePartId == null) {
            sparePartId = new Good();
        }
        return sparePartId;
    }
}
