package com.supcon.mes.module_main.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 * 工作提醒Entity
 */
public class WaitDealtEntity extends BaseEntity {

    @SerializedName(value = "dataid") // 实际为tableId
    public Long tableId;     //tableId

    @SerializedName(value = "eamcode")
    public String eamCode;     //设备编码

    @SerializedName(value = "eamname")
    public String eamName;   //设备名

    @SerializedName(value = "excutetime")
    public Long excuteTime;   //下次执行时间

    @SerializedName(value = "soucretype")
    public String sourceType;  //来源:其他异常(工单/隐患单);检修作业票提醒;停送电提醒;巡检异常(工单/隐患单);;;;等

    @SerializedName(value = "nextduration")
    public Long nextDuration;  //下次执行时长

    @SerializedName(value = "overdateflag")
    public String overDateFlag;//是否超期 1 超期  0 正常

    public String state;//状态  派工  执行  验收

    @SerializedName(value = "processkey")
    public String processKey;//工作流编码

    @SerializedName(value = "peroidtype")
    public SystemCodeEntity peroidType;//时间类型

    @SerializedName(value = "istemp")
    public String isTemp;//是否临时巡检

    @SerializedName(value = "pendingid")
    public Long pendingId;//待办

    @SerializedName(value = "staffid")
    public Staff staffId;

    public String tableno;

    @SerializedName(value = "workTableno")
    public String workTableNo; // 注：统一使用

    @SerializedName(value = "openurl")
    public String openUrl;

    @SerializedName(value = "entrflag")
    public String entrFlag; // 委托标志

    public String content;//内容

    public boolean isCheck;

    public Long endtime;
    @SerializedName(value = "endtimeactual")
    public Long endTimeActual;
    @SerializedName(value = "tableid")  // 实际为tableInfoId
    public Long tableInfoId;

    public String summary; // 摘要

    public Staff getStaffid() {
        if (staffId == null) {
            staffId = new Staff();
        }
        return staffId;
    }
}
