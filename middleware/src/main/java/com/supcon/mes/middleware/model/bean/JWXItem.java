package com.supcon.mes.middleware.model.bean;

import android.text.TextUtils;

import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

/**
 * JWXItem 业务规则实体
 * created by zhangwenshuai1 2018/9/18
 */
public class JWXItem extends BaseEntity {
    public Long id;
    public Long period;
    public ValueEntity periodType;//周期类型
    public ValueEntity periodUnit;//周期单位

    public AccessoryEamId accessoryEamId; //附属设备
    public String claim;//要求
    public String content;//内容
    public Float lastDuration;
    public Float nextDuration;
    public Long lastTime;
    public Long nextTime;
    public SparePartId sparePartId;//备件

    public EamEntity eamID;
    public LubricateOil lubricateOil; // 润滑油
    public String lubricatePart; // 润滑部位
    public SystemCodeEntity oilType; // 加/换油
    public BigDecimal sum; // 用量

    public ValueEntity getPeriodType() {
        if (periodType == null) {
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

    //是否润滑时长
    public boolean isDuration() {
        if (periodType != null && !TextUtils.isEmpty(periodType.id) && periodType.id.equals("BEAM014/02")) {
            return true;
        }
        return false;
    }


    public SparePartId getSparePartId() {
        if (sparePartId==null) {
            sparePartId = new SparePartId();
        }
        return sparePartId;
    }

    public AccessoryEamId getAccessoryEamId() {
        if (accessoryEamId==null) {
            accessoryEamId = new AccessoryEamId();
        }
        return accessoryEamId;
    }
}
