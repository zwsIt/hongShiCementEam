package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;

import java.util.List;
import java.util.Map;

import retrofit2.http.DELETE;

/**
 * Created by wangshizhan on 2018/8/14
 * Email:wangshizhan@supcom.com
 */
public class YHEntity extends BaseEntity {
    public Map attrMap;
    public Area areaInstall;
    public Staff findStaffID;
    public Staff chargeStaff;//负责人
    public Long cid;
    public String describe;
    public EamEntity eamID;
    public SystemCodeEntity faultInfoType; // 隐患类型:
    public SystemCodeEntity faultState; // 隐患状态
    public SystemCodeEntity downStream; // 处理方式
    public SystemCodeEntity sourceType; // 隐患来源
    public Long findTime;
    public Long id;
    public PendingEntity pending;
    public String remark;
    public SystemCodeEntity repairType;
    public String tableNo;
    public Long tableInfoId;
    public int status;
    public SystemCodeEntity priority;
    public RepairGroupEntity repiarGroup;

    public List<AttachmentEntity> attachmentEntities;
    @Deprecated
    public Boolean isOffApply; // 是否需要停电
    public SystemCodeEntity isPowerCut; // 是否需要停电

    public EamEntity getEamID() {
        if (eamID == null) {
            eamID = new EamEntity();
        }
        return eamID;
    }
}
