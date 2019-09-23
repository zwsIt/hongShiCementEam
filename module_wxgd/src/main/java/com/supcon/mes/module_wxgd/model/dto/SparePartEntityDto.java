package com.supcon.mes.module_wxgd.model.dto;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * SparePartEntityDto  备件传输对象实体
 * created by zhangwenshuai1 2018/9/5
 */
public class SparePartEntityDto extends BaseEntity {
    public String id;
    public IdDto productID; // 物品对象
    public String sum;  // 数量
    public String rowIndex;  // 行索引
    public String checkbox; // 多选框
    public String sort;
    public String remark;
    public String useQuantity; // 领用量
    public IdDto useState; // 状态
    public IdDto periodType;
    public String period;
    public IdDto periodUnit;
    public String lastTime;
    public String nextTime;
    public String lastDuration;
    public String nextDuration;
    public String accessoryName;
    public String isRef;
    public String timesNum;
    public String standingCrop; // 现存量
    public String sparePartId; // 基础备件（来源备件更换到期）
    public String actualQuantity; // 实际用量
    public String version;
}
