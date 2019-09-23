package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

/**
 * RepairStaffEntity 维修人员
 * created by zhangwenshuai1 2018/8/15
 */
public class RepairStaffEntity extends BaseEntity {

    public Long id;
    public Staff repairStaff;//维修人员
    public Long startTime;//实际开始时间
    public Long endTime;//实际结束时间
    public String version;//版本
    public String sort;
    public Integer timesNum;//次数
    public BigDecimal workHour;//工时(H)
    public String remark;//备注


}
