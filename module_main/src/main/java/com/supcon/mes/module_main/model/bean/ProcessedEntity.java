package com.supcon.mes.module_main.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamType;

public class ProcessedEntity extends BaseEntity {

    public Long createTime;
    public String dname; // 部门
    public String prostatus; // 状态
    public String name; // 流程名称
    public String staffname; // 待办人名称
    public String tableno; // 单据编号
    public EamType eamid; // 设备ID
    public String content; // 内容
    public String newstate; // 单据状态
    public String modelcode; // 模型编码
    public Long deploymentid; // 部署ID
    public Long tableid;
    public String processkey; // 单据流程关键字
    public String openurl; // 打开URl
    public Long workcreatetime; // 创建时间，（注：最终统一使用）
    public String worktableno; // 单据编号，（注：最终统一使用）

    public EamType getEamid() {
        if (eamid == null) {
            eamid = new EamType();
        }
        return eamid;
    }
}
