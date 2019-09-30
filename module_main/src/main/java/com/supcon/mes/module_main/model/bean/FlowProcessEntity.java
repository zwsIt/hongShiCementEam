package com.supcon.mes.module_main.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * 单据流程实体
 */
public class FlowProcessEntity extends BaseEntity {
    public String flowProcess; // 流程
    public String time;   // 处理时间
    public boolean isFinish; // 流程是否结束
    public String dealStaff; // 流程处理人
}
