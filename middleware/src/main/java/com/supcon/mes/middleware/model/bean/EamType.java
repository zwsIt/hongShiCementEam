package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * EamType 设备类型
 * created by zhangwenshuai1 2018/8/13
 */
@Entity
public class EamType extends BaseEntity implements CommonSearchEntity, Cloneable, Comparable<EamType>{
    @Id
    public Long id;
    public Long cid;
    public String code;
    public String name;
    public String remark;
    public String layRec; // 层次结构
    public String fullPathName; // 层级全路径
    public Integer layNo; // 层次

    //    @Transient
    public long parentId;

    @Transient
    public EamEntity eamEntity;
    @Transient
    public Department useDept;
    @Transient
    public Area installPlace;//区域位置

    @Generated(hash = 1362527568)
    public EamType(Long id, Long cid, String code, String name, String remark, String layRec,
            String fullPathName, Integer layNo, long parentId) {
        this.id = id;
        this.cid = cid;
        this.code = code;
        this.name = name;
        this.remark = remark;
        this.layRec = layRec;
        this.fullPathName = fullPathName;
        this.layNo = layNo;
        this.parentId = parentId;
    }
    @Generated(hash = 246208364)
    public EamType() {
    }

    public Department getUseDept() {
        if (useDept == null) {
            useDept = new Department();
        }
        return useDept;
    }
    public Area getInstallPlace() {
        if (installPlace == null) {
            installPlace = new Area();
        }
        return installPlace;
    }
    @Override
    public String getSearchId() {
        return String.valueOf(id);
    }

    @Override
    public String getSearchName() {
        return name;
    }

    @Override
    public String getSearchCode() {
        return code;
    }

    @Override
    public String getSearchProperty() {
        return getUseDept().name;
    }

    @Override
    public EamType clone() throws CloneNotSupportedException {
        return (EamType) super.clone();
    }
    @Override
    public int compareTo(EamType o) {
        return layNo - o.layNo;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getLayRec() {
        return this.layRec;
    }
    public void setLayRec(String layRec) {
        this.layRec = layRec;
    }
    public String getFullPathName() {
        return this.fullPathName;
    }
    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }
    public Integer getLayNo() {
        return this.layNo;
    }
    public void setLayNo(Integer layNo) {
        this.layNo = layNo;
    }
    public long getParentId() {
        return this.parentId;
    }
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
    public Long getCid() {
        return this.cid;
    }
    public void setCid(Long cid) {
        this.cid = cid;
    }
}
