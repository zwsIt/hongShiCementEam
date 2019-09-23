package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class RepairEntity extends BaseEntity {
    public FaultInfo faultInfo;
    public String tableNo;
    public ValueEntity workSource;
    public ValueEntity workState;

    public FaultInfo getFaultInfo() {
        if (faultInfo==null) {
            faultInfo = new FaultInfo();
        }
        return faultInfo;
    }

    public ValueEntity getWorkSource() {
        if (workSource==null) {
            workSource = new ValueEntity();
        }
        return workSource;
    }

    public ValueEntity getWorkState() {
        if (workState==null) {
            workState = new ValueEntity();
        }
        return workState;
    }
}
