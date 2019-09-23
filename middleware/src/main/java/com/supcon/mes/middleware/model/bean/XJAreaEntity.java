package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.EamApplication;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by wangshizhan on 2018/3/24.
 * Email:wangshizhan@supcon.com
 */

@Entity(indexes = {@Index(value = "areaId DESC,taskId DESC,ip DESC", unique = true)})
public class XJAreaEntity extends BaseEntity {

    @Id(autoincrement = true)
    public Long index;

    public Long areaId;       //区域ID

    public String areaName;     //区域名字

    public int areaOrder;    //区域顺序

    public String remark;   //备注

    public Long taskId;       //任务ID


    public String finishType;       // 0 ,1 是否结束
    public boolean isSign;          //是否签到
    public String signedTime;       //签到时间
    public String signType;         //签到类型
    public String signReason;       //签到原因
    public String exceptionType;    //0, 1 有缺陷存在

    public String signCode;         //签到编码

    public Long staffId = EamApplication.getAccountInfo().staffId;

    public boolean isDevice; //是否装置

    public Long eamId = null; // 设备ID，默认值null
    public String eamCode; // 设备编码
    public String eamName; // 设备名称
    public String eamModel; // 设备型号
    public String ip = MBapApp.getIp();
    public String guideImageName;

    @Generated(hash = 537022580)
    public XJAreaEntity(Long index, Long areaId, String areaName, int areaOrder,
            String remark, Long taskId, String finishType, boolean isSign, String signedTime,
            String signType, String signReason, String exceptionType, String signCode,
            Long staffId, boolean isDevice, Long eamId, String eamCode, String eamName,
            String eamModel, String ip, String guideImageName) {
        this.index = index;
        this.areaId = areaId;
        this.areaName = areaName;
        this.areaOrder = areaOrder;
        this.remark = remark;
        this.taskId = taskId;
        this.finishType = finishType;
        this.isSign = isSign;
        this.signedTime = signedTime;
        this.signType = signType;
        this.signReason = signReason;
        this.exceptionType = exceptionType;
        this.signCode = signCode;
        this.staffId = staffId;
        this.isDevice = isDevice;
        this.eamId = eamId;
        this.eamCode = eamCode;
        this.eamName = eamName;
        this.eamModel = eamModel;
        this.ip = ip;
        this.guideImageName = guideImageName;
    }

    @Generated(hash = 351469053)
    public XJAreaEntity() {
    }

    public Long getIndex() {
        return this.index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getAreaId() {
        return this.areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getAreaOrder() {
        return this.areaOrder;
    }

    public void setAreaOrder(int areaOrder) {
        this.areaOrder = areaOrder;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getFinishType() {
        return this.finishType;
    }

    public void setFinishType(String finishType) {
        this.finishType = finishType;
    }

    public boolean getIsSign() {
        return this.isSign;
    }

    public void setIsSign(boolean isSign) {
        this.isSign = isSign;
    }

    public String getSignedTime() {
        return this.signedTime;
    }

    public void setSignedTime(String signedTime) {
        this.signedTime = signedTime;
    }

    public String getSignType() {
        return this.signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSignReason() {
        return this.signReason;
    }

    public void setSignReason(String signReason) {
        this.signReason = signReason;
    }

    public String getExceptionType() {
        return this.exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getSignCode() {
        return this.signCode;
    }

    public void setSignCode(String signCode) {
        this.signCode = signCode;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public boolean getIsDevice() {
        return this.isDevice;
    }

    public void setIsDevice(boolean isDevice) {
        this.isDevice = isDevice;
    }

    public Long getEamId() {
        return this.eamId;
    }

    public void setEamId(Long eamId) {
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

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getGuideImageName() {
        return this.guideImageName;
    }

    public void setGuideImageName(String guideImageName) {
        this.guideImageName = guideImageName;
    }

    

    



}
