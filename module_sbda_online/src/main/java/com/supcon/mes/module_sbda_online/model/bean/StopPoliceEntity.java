package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

public class StopPoliceEntity extends BaseEntity {
    public Long id;
    public EamType eamID;//设备类型
    public Area installPlace;//区域位置
    public String auxiliary;

    public Long openTime;//开机时间
    public Long closedTime;//关机时间
    public SystemCodeEntity onOrOff;
    public Float totalHour;//持续时长
    
    public RecordId recordId;
    public static class EamInfo extends BaseEntity{
        public String eamName;
        public String eamId;
    }
    public static class RecordId extends BaseEntity {
        public SystemCodeEntity closedReason;
        public SystemCodeEntity closedType;
        public String id;
        public String reason;
    }

    public boolean checkNil() {
        return null == id;
    }

    public EamType getEamType() {
        if (eamID == null) {
            eamID = new EamType();
        }
        return eamID;
    }
}
