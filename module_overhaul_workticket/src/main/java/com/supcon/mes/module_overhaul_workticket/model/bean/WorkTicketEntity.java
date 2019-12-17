package com.supcon.mes.module_overhaul_workticket.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Department;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
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
    private DepartmentInfo workShop; // 车间

    private Long cid;
    private Staff createStaff;
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

    public DepartmentInfo getWorkShop() {
        if (workShop == null){
            workShop = new DepartmentInfo();
        }
        return workShop;
    }

    public void setWorkShop(DepartmentInfo workShop) {
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
}
