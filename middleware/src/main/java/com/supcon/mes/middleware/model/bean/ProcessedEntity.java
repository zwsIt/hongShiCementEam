package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.EamType;

public class ProcessedEntity extends BaseEntity {

    public Long createTime;
    @SerializedName(value = "dname")
    public String departmentName; // 部门
    @SerializedName(value = "prostatus")
    public String proStatus; // 状态
    public String name; // 流程名称
    @SerializedName(value = "staffname")
    public String staffName; // 待办人名称
//    public String tableno; // 单据编号
    @SerializedName(value = "eamid")
    public EamEntity eamId; // 设备ID
    public String content; // 内容
    @SerializedName(value = "newstate")
    public String tableState; // 过滤条件：单据状态  ：待派工、待执行、待通知、待验收、已结束、作废
    @SerializedName(value = "modelcode")
    public String modelCode; // 模型编码
    @SerializedName(value = "deploymentid")
    public Long deploymentId; // 部署ID
    @SerializedName(value = "tableid") // 实际是tableInfoId
    public Long tableInfoId;
    @SerializedName(value = "processkey")
    public String processKey; // 单据流程关键字
    @SerializedName(value = "openurl")
    public String openUrl; // 打开URl
    @SerializedName(value = "workcreatetime")
    public Long workCreateTime; // 创建时间，（注：最终统一使用）
    @SerializedName(value = "worktableno")
    public String workTableNo; // 单据编号，（注：最终统一使用）
    public String newUrl;
    public String summary; // 摘要

    @SerializedName(value = "dataId")
    public Long tableId;

    public EamEntity getEamId() {
        if (eamId == null) {
            eamId = new EamEntity();
        }
        return eamId;
    }
}
