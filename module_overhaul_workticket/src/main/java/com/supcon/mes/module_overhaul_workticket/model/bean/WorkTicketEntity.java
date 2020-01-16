package com.supcon.mes.module_overhaul_workticket.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Department;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/10
 * Email zhangwenshuai1@supcon.com
 * Desc 检修工作票实体
 */
public class WorkTicketEntity extends BaseEntity {
    private Map attrMap;
    private Staff chargeStaff; // 检修负责人
    private EamEntity eamId; // 设备
//    private SystemCodeEntity hazardsourContrpoint; // 危险源控制点
    private String hazardsourContrpoint; // 危险源控制点：id
    private String hazardsourContrpointForDisplay; // 危险源控制点:值
    private SystemCodeEntity riskAssessment; // 风险评估
    private WXGDEntity workList; // 工单
    private Department workShop; // 车间
    private Long offApplyId; // 停电作业票ID
    @SerializedName(value = "offApplyTableinfoid")
    private Long offApplyTableInfoId; // 停电作业票tableInfoId
    @SerializedName(value = "offApplyTableno")
    private String offApplyTableNo; // 停电作业票单据编号
    private String remark;
    private Staff centContRoom; // 中控室人员
    private Staff contrDirectorStaff; // 调度室主任
    private Staff securityChiefStaff; // 安保科科长
    private Staff securityStaff; // 安全员
    private SystemCodeEntity flowStatus; // 工作流状态：WorkTicket_003/01：编辑；WorkTicket_003/02：中控室确认；WorkTicket_003/03：安全员审核；WorkTicket_003/04：领导审批；WorkTicket_003/05：生效；WorkTicket_003/06：作废；

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

    public Staff getChargeStaff() {
        if (chargeStaff == null){
            chargeStaff = new Staff();
        }
        return chargeStaff;
    }

    public void setChargeStaff(Staff chargeStaff) {
        this.chargeStaff = chargeStaff;
    }

    public EamEntity getEamId() {
        if (eamId ==null){
            eamId = new EamEntity();
        }
        return eamId;
    }

    public void setEamId(EamEntity eamId) {
        this.eamId = eamId;
    }

    public String getHazardsourContrpoint() {
        return hazardsourContrpoint;
    }

    public void setHazardsourContrpoint(String hazardsourContrpoint) {
        this.hazardsourContrpoint = hazardsourContrpoint;
    }

    public SystemCodeEntity getRiskAssessment() {
        if (riskAssessment == null){
            riskAssessment = new SystemCodeEntity();
        }
        return riskAssessment;
    }

    public void setRiskAssessment(SystemCodeEntity riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public WXGDEntity getWorkList() {
        if (workList == null){
            workList = new WXGDEntity();
        }
        return workList;
    }

    public void setWorkList(WXGDEntity workList) {
        this.workList = workList;
    }

    public Department getWorkShop() {
        if (workShop == null){
            workShop = new Department();
        }
        return workShop;
    }

    public void setWorkShop(Department workShop) {
        this.workShop = workShop;
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

    public String getHazardsourContrpointForDisplay() {
        return hazardsourContrpointForDisplay;
    }

    public void setHazardsourContrpointForDisplay(String hazardsourContrpointForDisplay) {
        this.hazardsourContrpointForDisplay = hazardsourContrpointForDisplay;
    }

    public Long getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Long deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Long getOffApplyId() {
        return offApplyId;
    }

    public void setOffApplyId(Long offApplyId) {
        this.offApplyId = offApplyId;
    }

    public String getOffApplyTableNo() {
        return offApplyTableNo;
    }

    public void setOffApplyTableNo(String offApplyTableNo) {
        this.offApplyTableNo = offApplyTableNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getOffApplyTableInfoId() {
        return offApplyTableInfoId;
    }

    public void setOffApplyTableInfoId(Long offApplyTableInfoId) {
        this.offApplyTableInfoId = offApplyTableInfoId;
    }

    public Staff getCentContRoom() {
        if (centContRoom == null){
            centContRoom = new Staff();
        }
        return centContRoom;
    }

    public void setCentContRoom(Staff centContRoom) {
        this.centContRoom = centContRoom;
    }

    public Staff getContrDirectorStaff() {
        if (contrDirectorStaff == null){
            contrDirectorStaff = new Staff();
        }
        return contrDirectorStaff;
    }

    public void setContrDirectorStaff(Staff contrDirectorStaff) {
        this.contrDirectorStaff = contrDirectorStaff;
    }

    public Staff getSecurityChiefStaff() {
        if (securityChiefStaff == null){
            securityChiefStaff = new Staff();
        }
        return securityChiefStaff;
    }

    public void setSecurityChiefStaff(Staff securityChiefStaff) {
        this.securityChiefStaff = securityChiefStaff;
    }

    public Staff getSecurityStaff() {
        if (securityStaff == null){
            securityStaff = new Staff();
        }
        return securityStaff;
    }

    public void setSecurityStaff(Staff securityStaff) {
        this.securityStaff = securityStaff;
    }

    public SystemCodeEntity getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(SystemCodeEntity flowStatus) {
        this.flowStatus = flowStatus;
    }
}
