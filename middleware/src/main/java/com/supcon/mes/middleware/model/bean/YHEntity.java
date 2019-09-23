package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wangshizhan on 2018/8/14
 * Email:wangshizhan@supcom.com
 */
public class YHEntity extends BaseEntity {
    public Map attrMap;
    public Area areaInstall;
    public Staff findStaffID;
    public Staff chargeStaff;//负责人
    public long cid;
    public String describe;
    public WXGDEam eamID;
    public SystemCodeEntity faultInfoType;
    public SystemCodeEntity faultState;
    public SystemCodeEntity downStream; // 处理方式
    public SystemCodeEntity sourceType;
    public long findTime;
    public long id;
    public PendingEntity pending;
    public String remark;
    public SystemCodeEntity repairType;
    public String tableNo;
    public long tableInfoId;
    public int status;
    public SystemCodeEntity priority;
    public RepairGroupEntity repiarGroup;

    public List<AttachmentEntity> attachmentEntities;

    public WXGDEam getEamID() {
        if (eamID == null) {
            eamID = new WXGDEam();
        }
        return eamID;
    }
}
