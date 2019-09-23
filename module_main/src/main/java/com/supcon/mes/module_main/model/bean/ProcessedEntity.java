package com.supcon.mes.module_main.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamType;

public class ProcessedEntity extends BaseEntity {

    public Long createTime;
    public String dname;
    public String prostatus;
    public String name;
    public String staffname;
    public String tableno;
    public EamType eamid;
    public String content;
    public String newstate;
    public String modelcode;
    public Long deploymentid;
    public Long tableid;

    public EamType getEamid() {
        if (eamid == null) {
            eamid = new EamType();
        }
        return eamid;
    }
}
