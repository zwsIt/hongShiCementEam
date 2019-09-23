package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.util.DeviceManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by wangshizhan on 2017/12/15.
 * Email:wangshizhan@supcon.com
 * 设备档案实体
 */

@Entity
public class CommonDeviceEntity extends BaseEntity implements CommonSearchEntity {

    @Id
    public long eamId;                  //设备id
    public long userId ;                //用户id

    public String eamCode;              //设备编号
    public String eamName;              //设备名称
    public String eamModel;             //设备型号
    public String eamType;              //设备类型
    public String eamPic;               //设备图片

    public String eamState;             //设备状态
    public String eamUseDept;           //使用部门
    public String eamSpecif;            //设备规格
    public String eamDutyStaff;         //责任人
    public String eamAbc;               //ABC分类
    public String eamUseDate;           //启用日期

    public String produceCode;          //出厂编号
    public String produceFirm;          //制造单位
    public String produceDate;            //出厂日期
    public String specialType;          //特种设备类型
    public String memo;                 //备注
    public boolean isMea;               //是否测量
    public String designFirm;           //设计单位
    public String otherSpecial;         //其他特殊设备
    public String installPlace;         //区域位置
    public String installFirm;          //安装单位
    public String fileState;            //建档标记
    public String fileDate;               //建档日期
    public String areaNum;              //设备位号
    public Float useYear;              //使用年限
    public String vendor;               //供应商
    public boolean isSpecial;           //是否特种设备
    public boolean haveRunState;        //关注运行
    public String specialty;            //专业

    public String ip = MBapApp.getIp();
    public String pinYin; // 拼音

    /**
     * @See EamPermission
     */
//    public long eamPermission;
    public String eamPermissionStr;

    public long updateTime;             //最新更新时间
    public long frequency;              //操作次数
    @Generated(hash = 586399776)
    public CommonDeviceEntity(long eamId, long userId, String eamCode,
            String eamName, String eamModel, String eamType, String eamPic,
            String eamState, String eamUseDept, String eamSpecif,
            String eamDutyStaff, String eamAbc, String eamUseDate,
            String produceCode, String produceFirm, String produceDate,
            String specialType, String memo, boolean isMea, String designFirm,
            String otherSpecial, String installPlace, String installFirm,
            String fileState, String fileDate, String areaNum, Float useYear,
            String vendor, boolean isSpecial, boolean haveRunState,
            String specialty, String ip, String pinYin, String eamPermissionStr,
            long updateTime, long frequency) {
        this.eamId = eamId;
        this.userId = userId;
        this.eamCode = eamCode;
        this.eamName = eamName;
        this.eamModel = eamModel;
        this.eamType = eamType;
        this.eamPic = eamPic;
        this.eamState = eamState;
        this.eamUseDept = eamUseDept;
        this.eamSpecif = eamSpecif;
        this.eamDutyStaff = eamDutyStaff;
        this.eamAbc = eamAbc;
        this.eamUseDate = eamUseDate;
        this.produceCode = produceCode;
        this.produceFirm = produceFirm;
        this.produceDate = produceDate;
        this.specialType = specialType;
        this.memo = memo;
        this.isMea = isMea;
        this.designFirm = designFirm;
        this.otherSpecial = otherSpecial;
        this.installPlace = installPlace;
        this.installFirm = installFirm;
        this.fileState = fileState;
        this.fileDate = fileDate;
        this.areaNum = areaNum;
        this.useYear = useYear;
        this.vendor = vendor;
        this.isSpecial = isSpecial;
        this.haveRunState = haveRunState;
        this.specialty = specialty;
        this.ip = ip;
        this.pinYin = pinYin;
        this.eamPermissionStr = eamPermissionStr;
        this.updateTime = updateTime;
        this.frequency = frequency;
    }
    @Generated(hash = 1508926651)
    public CommonDeviceEntity() {
    }
    public long getEamId() {
        return this.eamId;
    }
    public void setEamId(long eamId) {
        this.eamId = eamId;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
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
    public String getEamState() {
        return this.eamState;
    }
    public void setEamState(String eamState) {
        this.eamState = eamState;
    }
    public String getEamUseDept() {
        return this.eamUseDept;
    }
    public void setEamUseDept(String eamUseDept) {
        this.eamUseDept = eamUseDept;
    }
    public String getEamSpecif() {
        return this.eamSpecif;
    }
    public void setEamSpecif(String eamSpecif) {
        this.eamSpecif = eamSpecif;
    }
    public String getEamDutyStaff() {
        return this.eamDutyStaff;
    }
    public void setEamDutyStaff(String eamDutyStaff) {
        this.eamDutyStaff = eamDutyStaff;
    }
    public String getEamAbc() {
        return this.eamAbc;
    }
    public void setEamAbc(String eamAbc) {
        this.eamAbc = eamAbc;
    }
    public String getEamUseDate() {
        return this.eamUseDate;
    }
    public void setEamUseDate(String eamUseDate) {
        this.eamUseDate = eamUseDate;
    }
    public String getProduceCode() {
        return this.produceCode;
    }
    public void setProduceCode(String produceCode) {
        this.produceCode = produceCode;
    }
    public String getProduceFirm() {
        return this.produceFirm;
    }
    public void setProduceFirm(String produceFirm) {
        this.produceFirm = produceFirm;
    }
    public String getProduceDate() {
        return this.produceDate;
    }
    public void setProduceDate(String produceDate) {
        this.produceDate = produceDate;
    }
    public String getSpecialType() {
        return this.specialType;
    }
    public void setSpecialType(String specialType) {
        this.specialType = specialType;
    }
    public String getMemo() {
        return this.memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public boolean getIsMea() {
        return this.isMea;
    }
    public void setIsMea(boolean isMea) {
        this.isMea = isMea;
    }
    public String getDesignFirm() {
        return this.designFirm;
    }
    public void setDesignFirm(String designFirm) {
        this.designFirm = designFirm;
    }
    public String getOtherSpecial() {
        return this.otherSpecial;
    }
    public void setOtherSpecial(String otherSpecial) {
        this.otherSpecial = otherSpecial;
    }
    public String getInstallPlace() {
        return this.installPlace;
    }
    public void setInstallPlace(String installPlace) {
        this.installPlace = installPlace;
    }
    public String getInstallFirm() {
        return this.installFirm;
    }
    public void setInstallFirm(String installFirm) {
        this.installFirm = installFirm;
    }
    public String getFileState() {
        return this.fileState;
    }
    public void setFileState(String fileState) {
        this.fileState = fileState;
    }
    public String getFileDate() {
        return this.fileDate;
    }
    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }
    public String getAreaNum() {
        return this.areaNum;
    }
    public void setAreaNum(String areaNum) {
        this.areaNum = areaNum;
    }
    public Float getUseYear() {
        return this.useYear;
    }
    public void setUseYear(Float useYear) {
        this.useYear = useYear;
    }
    public String getVendor() {
        return this.vendor;
    }
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    public boolean getIsSpecial() {
        return this.isSpecial;
    }
    public void setIsSpecial(boolean isSpecial) {
        this.isSpecial = isSpecial;
    }
    public boolean getHaveRunState() {
        return this.haveRunState;
    }
    public void setHaveRunState(boolean haveRunState) {
        this.haveRunState = haveRunState;
    }
    public String getSpecialty() {
        return this.specialty;
    }
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
    public String getIp() {
        return this.ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getEamPermissionStr() {
        return this.eamPermissionStr;
    }
    public void setEamPermissionStr(String eamPermissionStr) {
        this.eamPermissionStr = eamPermissionStr;
    }
    public long getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    public long getFrequency() {
        return this.frequency;
    }
    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    @Override
    public String getSearchId() {
        return eamCode;
    }

    @Override
    public String getSearchName() {
        return eamName;
    }

    @Override
    public String getSearchProperty() {
        return installPlace;
    }
    public String getPinYin() {
        return this.pinYin;
    }
    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }
    public String getEamPic() {
        return this.eamPic;
    }
    public void setEamPic(String eamPic) {
        this.eamPic = eamPic;
    }
}
