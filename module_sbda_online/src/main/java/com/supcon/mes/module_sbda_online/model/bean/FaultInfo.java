package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.ValueEntity;

/**
 * FaultInfo 隐患信息
 * created by zhangwenshuai1 2018/8/13
 */
public class FaultInfo extends BaseEntity {
    public Long id;
    public String describe;//隐患现象
    public Staff findStaffID;//发现人
    public ValueEntity repairType;//维修类型
    public ValueEntity priority;//优先级
    public long findTime;

    public Staff getFindStaffID() {
        if (findStaffID == null) {
            findStaffID = new Staff();
        }
        return findStaffID;
    }

    public ValueEntity getPriority() {
        if (priority == null) {
            priority = new ValueEntity();
        }
        return priority;
    }

    public ValueEntity getRepairType() {
        if (repairType == null) {
            repairType = new ValueEntity();
        }
        return repairType;
    }
}
