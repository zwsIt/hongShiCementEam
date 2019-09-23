package com.supcon.mes.module_yhgl.model.bean;


import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Good;

import java.math.BigDecimal;

/**
 * SparePartRefEntity 备件清单实体(PC设备档案实体中)
 * created by zhangwenshuai1 2018/10/24
 */
public class SparePartRefEntity extends BaseEntity {

    /**
     * attrMap : null
     * cid : 1000
     * depleteSum : 12
     * id : 1013
     * productID : {"id":1003,"productBaseUnit":{},"productCode":"QX01","productCostPrice":null,"productModel":"bzb_001","productName":"油泵","productSpecif":"XLD_10_35_18.5"}
     * spareMemo : null
     * standingCrop : null
     * tableInfoId : null
     * valid : true
     * version : 0
     */

    private Long cid;
    private BigDecimal depleteSum; //数量
    private Long id;
    private Good productID;
    private String spareMemo; // 备注
    private BigDecimal standingCrop; // 现存量
    private Long tableInfoId;
    private boolean valid;
    private int version;

    public Long getCid() {
        return cid;
    }

    public BigDecimal getDepleteSum() {
        return depleteSum;
    }

    public Long getId() {
        return id;
    }

    public Good getProductID() {
        return productID;
    }

    public String getSpareMemo() {
        return spareMemo;
    }

    public BigDecimal getStandingCrop() {
        return standingCrop;
    }

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public boolean isValid() {
        return valid;
    }

    public int getVersion() {
        return version;
    }
}
