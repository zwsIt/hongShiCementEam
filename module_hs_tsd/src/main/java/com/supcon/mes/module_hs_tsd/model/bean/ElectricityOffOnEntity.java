package com.supcon.mes.module_hs_tsd.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.Department;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.Staff;

import java.util.List;
import java.util.Map;
/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/27
 * Email zhangwenshuai1@supcon.com
 * Desc 停/送电实体
 */
public class ElectricityOffOnEntity extends BaseEntity {
    public List<AttachmentEntity> attachmentEntities; // 附件
    private Map attrMap;
    private Staff applyStaff; // 申请人(发令人)
    private EamEntity eamID; // 设备
    private Long workRecordId; // 关联工单ID
    private String workRecordTableno; // 关联工单单据编号
    private Long applyDate; // 申请时间(停电时间)
    private Department applyCurrentDept; // 申请部门
    private Staff operateStaff; // 操作人
    private String workTask; // 工作任务
    private String remark;
    private EleOffOnTemplate eleTemplateId; // 停送电模板

    private Long cid;
    private Staff createStaff;
    private Long createStaffId;
    private Long createTime;
    private Long id;
    private PendingEntity pending;
    private int status;
    private Long tableInfoId;
    private String tableNo;
    private boolean valid;
    private int version;

    private Long deploymentId;

    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
    }

    public Staff getApplyStaff() {
        if (applyStaff == null){
            applyStaff = new Staff();
        }
        return applyStaff;
    }

    public void setApplyStaff(Staff applyStaff) {
        this.applyStaff = applyStaff;
    }

    public EamEntity getEamID() {
        if (eamID == null){
            eamID = new EamEntity();
        }
        return eamID;
    }

    public void setEamID(EamEntity eamID) {
        this.eamID = eamID;
    }

    public Long getWorkRecordId() {
        return workRecordId;
    }

    public void setWorkRecordId(Long workRecordId) {
        this.workRecordId = workRecordId;
    }

    public String getWorkRecordTableno() {
        return workRecordTableno;
    }

    public void setWorkRecordTableno(String workRecordTableno) {
        this.workRecordTableno = workRecordTableno;
    }

    public Long getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Long applyDate) {
        this.applyDate = applyDate;
    }

    public Staff getOperateStaff() {
        if (operateStaff == null){
            operateStaff = new Staff();
        }
        return operateStaff;
    }

    public void setOperateStaff(Staff operateStaff) {
        this.operateStaff = operateStaff;
    }

    public String getWorkTask() {
        return workTask;
    }

    public void setWorkTask(String workTask) {
        this.workTask = workTask;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Staff getCreateStaff() {
        return createStaff;
    }

    public void setCreateStaff(Staff createStaff) {
        this.createStaff = createStaff;
    }

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PendingEntity getPending() {
        return pending;
    }

    public void setPending(PendingEntity pending) {
        this.pending = pending;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Long deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Department getApplyCurrentDept() {
        return applyCurrentDept;
    }

    public void setApplyCurrentDept(Department applyCurrentDept) {
        this.applyCurrentDept = applyCurrentDept;
    }

    public EleOffOnTemplate getEleTemplateId() {
        if (eleTemplateId == null){
            eleTemplateId = new EleOffOnTemplate();
        }
        return eleTemplateId;
    }

    public void setEleTemplateId(EleOffOnTemplate eleTemplateId) {
        this.eleTemplateId = eleTemplateId;
    }
}
