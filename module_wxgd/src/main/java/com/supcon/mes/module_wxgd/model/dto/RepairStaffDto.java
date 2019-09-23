package com.supcon.mes.module_wxgd.model.dto;

import com.supcon.common.com_http.BaseEntity;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/6
 * ------------- Description -------------
 */
public class RepairStaffDto extends BaseEntity {

    public String id;
    public StaffDto repairStaff;//维修人员
    public String startTime;//实际开始时间
    public String endTime;//实际结束时间
    public String version;//版本（版本锁问题）
    public String sort;
    public String timesNum;//次数
    public String workHour;//工时(H)
    public String remark;//备注
    public String rowIndex;//
}
