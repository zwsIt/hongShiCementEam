package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

/**
 * LubricateOilsEntity 润滑油实体
 * created by zhangwenshuai1 2018/8/15
 */
public class RefLubricateEntity extends BaseEntity {
    public Long cid;
    public Long id;
    public LubricateOil lubricateOil;

    public Float lastDuration;
    public Float nextDuration;
    public Long lastTime;
    public Long nextTime;
    public Long period;
    public ValueEntity periodType;
    public ValueEntity periodUnit;

    public SystemCodeEntity oilType;//加换油
    public String remark;//备注
    public String claim;//要求
    public String content;//内容
    public String lubricatePart;//润滑部位
    public String version;
    public String unitName;//单位
    public Float sum;//用量

    public SparePartId sparePartId;//备件编码
    public AccessoryEamId accessoryEamId;//附属设备

    public SystemCodeEntity getOilType() {
        if (oilType == null) {
            oilType = new SystemCodeEntity();
        }
        return oilType;
    }

    public LubricateOil getLubricateOil() {
        if (lubricateOil == null) {
            lubricateOil = new LubricateOil();
        }
        return lubricateOil;
    }

    public void setLubricateOil(LubricateOil lubricateOil) {
        this.lubricateOil = lubricateOil;
    }
}
