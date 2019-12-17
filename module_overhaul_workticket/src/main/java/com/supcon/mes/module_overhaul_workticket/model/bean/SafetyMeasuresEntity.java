package com.supcon.mes.module_overhaul_workticket.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_overhaul_workticket.constant.OperateType;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/10
 * Email zhangwenshuai1@supcon.com
 * Desc 安全措施(检修工作票PT)
 */
public class SafetyMeasuresEntity extends BaseEntity {

    /**
     * headId : {"id":1000}
     * id : 1000
     * isExecuted : false
     * safetyMeasure : 已按照规定召开检修例会
     * sort : 0
     * version : 0
     */
    private WorkTicketEntity headId; // 表头检修作业票
    private Long id;
    private boolean isExecuted; // 是否执行
    private String safetyMeasure; // 安全措施
    private int sort;
    private int version;

    // 自定义字段
    private int type = OperateType.CONFIRM.getType(); // 操作类型：视频、拍照、nfc、确认; 默认确认

    public WorkTicketEntity getHeadId() {
        return headId;
    }

    public void setHeadId(WorkTicketEntity headId) {
        this.headId = headId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isIsExecuted() {
        return isExecuted;
    }

    public void setIsExecuted(boolean isExecuted) {
        this.isExecuted = isExecuted;
    }

    public String getSafetyMeasure() {
        return safetyMeasure;
    }

    public void setSafetyMeasure(String safetyMeasure) {
        this.safetyMeasure = safetyMeasure;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
