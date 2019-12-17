package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/11/5
 * Email zhangwenshuai1@supcon.com
 * Desc 润滑部位实体
 */
public class LubricatingPartEntity extends BaseEntity {

    /**
     * attrMap : null
     * cid : 1000
     * createStaff : null
     * createTime : null
     * eamTypeId : {"id":1000,"name":"普通设备"}
     * id : 1001
     * lubPart : 机器表面
     * remark :
     * status : null
     * tableInfoId : null
     * tableNo : null
     * valid : true
     * version : 0
     */

    private Map attrMap;
    private Long cid;
    private Staff createStaff;
    private Long createTime;
    private EamType eamTypeId;
    private Long id;
    private String lubPart;
    private String remark;
    private Integer status;
    private Long tableInfoId;
    private String tableNo;
    private Boolean valid;
    private int version;

    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
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

    public EamType getEamTypeId() {
        if (eamTypeId == null) {
            return eamTypeId = new EamType();
        }
        return eamTypeId;
    }

    public void setEamTypeId(EamType eamTypeId) {
        this.eamTypeId = eamTypeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLubPart() {
        return lubPart;
    }

    public void setLubPart(String lubPart) {
        this.lubPart = lubPart;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public Boolean isValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
