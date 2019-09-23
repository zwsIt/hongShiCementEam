package com.supcon.mes.middleware.model.bean;


import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

/**
 * SparePartRefEntity 备件清单实体(PC设备档案实体中)
 * created by zhangwenshuai1 2018/10/24
 * 参照
 */
public class SparePartRefEntity extends BaseEntity {

    public AccessoryEamId accessoryEamId;//附属设备
    private Long cid;
    private BigDecimal depleteSum; //数量
    private Long id;

    private Float lastDuration;
    private Float nextDuration;
    private Long lastTime;
    private Long nextTime;

    private Long period;
    private ValueEntity periodType;
    private ValueEntity periodUnit;

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

    public void setProductID(Good productID) {
        this.productID = productID;
    }

    public AccessoryEamId getAccessoryEamId() {
        if (accessoryEamId == null) {
            accessoryEamId = new AccessoryEamId();
        }
        return accessoryEamId;
    }

    public Float getLastDuration() {
        return lastDuration;
    }

    public Float getNextDuration() {
        return nextDuration;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public Long getNextTime() {
        return nextTime;
    }

    public Long getPeriod() {
        return period;
    }

    public ValueEntity getPeriodType() {
        if (periodType==null) {
            periodType = new ValueEntity();
        }
        return periodType;
    }

    public ValueEntity getPeriodUnit() {
        if (periodUnit == null) {
            periodUnit = new ValueEntity();
        }
        return periodUnit;
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
