package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.constant.Constant;

import java.math.BigDecimal;

/**
 * SparePartEntity 备件实体
 * created by zhangwenshuai1 2018/8/15
 */
public class SparePartEntity extends BaseEntity {
    public Long id;
    public Float lastDuration;
    public Float nextDuration;
    public Long lastTime;
    public Long nextTime;
    public Long period;
    public ValueEntity periodType;
    public ValueEntity periodUnit;

    public Good productID;//备件
    public BigDecimal sum;  //计划领用量
    public int timesNum;//次数
    public String accessoryName;//附属设备
    public String remark;//备注
    public BigDecimal useQuantity; // 领用量
    public SystemCodeEntity useState; // 状态
    public BigDecimal standingCrop; // 现存量
    public Long sparePartId; // 基础备件（来源备件更换到期）
    public BigDecimal actualQuantity; // 实际用量
    public boolean isDeliveried; // 是否领用出库
    public boolean isRef; // 是否参照
    public String version;

    public boolean isWarn;//是否来自预警

    public SystemCodeEntity getUseState() {
        if (useState == null) {
            useState = new SystemCodeEntity();
            useState.id = Constant.SparePartUseStatus.NO_USE;
        }
        return useState;
    }
}
