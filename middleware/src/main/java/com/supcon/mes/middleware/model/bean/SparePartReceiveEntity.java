package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

/**
 * 备件领用申请明细
 */
public class SparePartReceiveEntity extends BaseEntity {

    public Long id;
    public BigDecimal origDemandQuity;//申请数量
    public BigDecimal currDemandQuity;//领用量
    public String remark;//备注
    public Good sparePartId;//备件编码
    public BigDecimal price; // 单价
    public BigDecimal total; // 总价
    public BigDecimal defference; // 差额
    public BigDecimal actualNum; // 实发数量


    public Good getSparePartId() {
        if (sparePartId == null) {
            sparePartId = new Good();
        }
        return sparePartId;
    }
}
