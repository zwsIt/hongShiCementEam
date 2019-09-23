package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by wangshizhan on 2017/11/17.
 * Email:wangshizhan@supcon.com
 */

@Entity
public class RunStateDeviceEntity extends BaseEntity {

    @Id(autoincrement = true)
    public Long localId;

    public long id ;                    //运行状态明细id
    public long eamId;                  //设备id

    public String eamCode;              //设备编号
    public String eamName;              //设备名称
    public String eamModel;             //设备型号
    public String eamType;              //设备类型

    public String place;                //区域位置

    public long runStateId;             //运行属性id
    public String runStateName;         //运行属性名称
    public String onOrOffId;            //开关状态id
    public String onOrOffName;          //开关状态名称

    public long startDate;              //开始时间
    public String reason;               //原因
    public String remark;               //备注
    @Transient
    transient public boolean isChecked = false;           //设备选择状态

    @Transient
    transient public boolean isSwiped = false;           //设备选择状态

    @Transient
    transient public boolean isInvalied = false;           //是否有效，小于设备最后审核时间

    public long parentId = -1;

    @Generated(hash = 1135999665)
    public RunStateDeviceEntity(Long localId, long id, long eamId, String eamCode,
            String eamName, String eamModel, String eamType, String place,
            long runStateId, String runStateName, String onOrOffId,
            String onOrOffName, long startDate, String reason, String remark,
            long parentId) {
        this.localId = localId;
        this.id = id;
        this.eamId = eamId;
        this.eamCode = eamCode;
        this.eamName = eamName;
        this.eamModel = eamModel;
        this.eamType = eamType;
        this.place = place;
        this.runStateId = runStateId;
        this.runStateName = runStateName;
        this.onOrOffId = onOrOffId;
        this.onOrOffName = onOrOffName;
        this.startDate = startDate;
        this.reason = reason;
        this.remark = remark;
        this.parentId = parentId;
    }

    @Generated(hash = 725110125)
    public RunStateDeviceEntity() {
    }

    public Long getLocalId() {
        return this.localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEamId() {
        return this.eamId;
    }

    public void setEamId(long eamId) {
        this.eamId = eamId;
    }

    public String getEamCode() {
        return this.eamCode;
    }

    public void setEamCode(String eamCode) {
        this.eamCode = eamCode;
    }

    public String getEamName() {
        return this.eamName;
    }

    public void setEamName(String eamName) {
        this.eamName = eamName;
    }

    public String getEamModel() {
        return this.eamModel;
    }

    public void setEamModel(String eamModel) {
        this.eamModel = eamModel;
    }

    public String getEamType() {
        return this.eamType;
    }

    public void setEamType(String eamType) {
        this.eamType = eamType;
    }

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getRunStateId() {
        return this.runStateId;
    }

    public void setRunStateId(long runStateId) {
        this.runStateId = runStateId;
    }

    public String getRunStateName() {
        return this.runStateName;
    }

    public void setRunStateName(String runStateName) {
        this.runStateName = runStateName;
    }

    public String getOnOrOffId() {
        return this.onOrOffId;
    }

    public void setOnOrOffId(String onOrOffId) {
        this.onOrOffId = onOrOffId;
    }

    public String getOnOrOffName() {
        return this.onOrOffName;
    }

    public void setOnOrOffName(String onOrOffName) {
        this.onOrOffName = onOrOffName;
    }

    public long getStartDate() {
        return this.startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getParentId() {
        return this.parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

}
