package com.supcon.mes.module_yhgl.model.dto;

/**
 * @Description: 工作发起提交实体
 * @Author: zhangwenshuai
 * @CreateDate: 2020/5/23 9:10
 */
public class WorkStartDTO {
    private Long initStaffId;       // 工作发起人
    private Long workContactStaffId;// 工作联络人
    private Long eamId;             // 设备ID
    private Long planFinishTime;    // 计划完成时间
    private String priority;        // 优先级
    private String content;         // 工作内容

    public Long getInitStaffId() {
        return initStaffId;
    }

    public void setInitStaffId(Long initStaffId) {
        this.initStaffId = initStaffId;
    }

    public Long getWorkContactStaffId() {
        return workContactStaffId;
    }

    public void setWorkContactStaffId(Long workContactStaffId) {
        this.workContactStaffId = workContactStaffId;
    }

    public Long getEamId() {
        return eamId;
    }

    public void setEamId(Long eamId) {
        this.eamId = eamId;
    }

    public Long getPlanFinishTime() {
        return planFinishTime;
    }

    public void setPlanFinishTime(Long planFinishTime) {
        this.planFinishTime = planFinishTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}