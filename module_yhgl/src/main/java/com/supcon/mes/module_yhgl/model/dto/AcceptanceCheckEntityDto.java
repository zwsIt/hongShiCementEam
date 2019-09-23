package com.supcon.mes.module_yhgl.model.dto;

import com.supcon.common.com_http.BaseEntity;

/**
 * AcceptanceCheckEntityDto 验收实体传输对象
 * created by zhangwenshuai1 2018/9/5
 */
public class AcceptanceCheckEntityDto extends BaseEntity {

    public String checkTime;
    public IdDto checkStaff;
    public IdDto checkResult;
    public String remark;
    public String sort;
    public String rowIndex;
}
