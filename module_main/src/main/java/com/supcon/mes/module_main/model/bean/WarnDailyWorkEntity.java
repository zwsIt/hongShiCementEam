package com.supcon.mes.module_main.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * Created by zhangwenshuai on 2020/6/2
 * Email:zhangwenshuai1@supcom.com
 * 预警提醒工作实体(PC 预警信息)
 */
public class WarnDailyWorkEntity extends BaseEntity {

    public Long createTime;
    public Long excuteTime; // 下次执行时间
    public Long nextDuration; // 下次执行时长
    public Long eamId; // 设备ID
    public String eamName; // 设备名称
    public String eamCode; // 设备资产编码
    @SerializedName(value = "eamAssetcode")
    public String eamAssetCode; // 设备编码
    public String content; // 内容
    public String overAllState; // 工作状态
    public SystemCodeEntity peroidType; // 周期类型
    @SerializedName(value = "soucreType")
    public String sourceType; // 来源
    @SerializedName(value = "tableId")
    public String tableInfoId;
    public String summary; // 摘要
    public Long dataId;

}
