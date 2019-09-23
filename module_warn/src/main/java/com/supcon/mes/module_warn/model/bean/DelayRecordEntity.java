package com.supcon.mes.module_warn.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.ValueEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 延期列表
 */
public class DelayRecordEntity extends BaseEntity {

    public Long afterDelDate;
    public Long beforeDelDate;
    public Long cid;
    public Long id;
    public Staff dealStaff;
    public Long dealTime;
    public EamType delayEamId;
    public String delayReason;
    public Long period;
    public ValueEntity periodUnit;//周期单位

    public Staff getDealStaff() {
        if (dealStaff == null) {
            dealStaff = new Staff();
        }
        return dealStaff;
    }

    public EamType getDelayEamId() {
        if (delayEamId == null) {
            delayEamId = new EamType();
        }
        return delayEamId;
    }
}
