package com.supcon.mes.module_wxgd.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.WXGDEntity;

/**
 * @description SparePartApplyHeaderInfoEntity 备件领用申请单表头实体
 * @author  2019/9/27
 */
public class SparePartApplyHeaderInfoEntity extends ResultEntity {
    /**
     * applyStaff : {"mainPosition":{"department":{"name":"虚拟部门"},"name":"保全处信息化工程师"},"name":"胡小飞"}
     * applyTime : 1569590740170
     * explain : null
     * id : 1006
     * remark : null
     * repairWork : {"content":"","eamID":{"code":"1917","name":"铰刀电机"},"id":1033}
     */

    private Staff applyStaff; // 申请人
    private Long applyTime; // 申请时间
    private String explain; // 使用说明
    private Long id;
    private String remark;
    private WXGDEntity repairWork; // 维修工单
    private Long createStaffId;
    private Long createTime;
    private Long deploymentId;
    private Long tableInfoId;
    private int version;

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public Long getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Long deploymentId) {
        this.deploymentId = deploymentId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public Staff    getApplyStaff() {
        if (applyStaff == null){
            return new Staff();
        }
        return applyStaff;
    }

    public void setApplyStaff(Staff applyStaff) {
        this.applyStaff = applyStaff;
    }

    public Long getApplyTime() {
        if (applyTime == null){
            return System.currentTimeMillis();
        }
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public WXGDEntity getRepairWork() {
        if (repairWork == null){
            return new WXGDEntity();
        }
        return repairWork;
    }

    public void setRepairWork(WXGDEntity repairWork) {
        this.repairWork = repairWork;
    }
}
