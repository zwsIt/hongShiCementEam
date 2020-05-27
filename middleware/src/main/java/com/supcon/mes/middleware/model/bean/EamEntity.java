package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.Department;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.ValueEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 */
@Entity
public class EamEntity extends BaseEntity implements CommonSearchEntity {
    @Id
    public Long id;
    public String code; // 设备资产编码
    public String name;

    public String state;//设备状态
    public String stateForDisplay;

    public Long eamTypeId;
    @ToOne(joinProperty = "eamTypeId") // //EamEntity和EamType是一对一关系，外键为eamTypeId
    public EamType eamType;//设备类型

    public Long areaId;
    @ToOne(joinProperty = "areaId")
    public Area installPlace;//区域位置
    //    public Long useDeptId;
//    @ToOne(joinProperty = "useDeptId")
    @Transient
    public Department useDept;//部门
    public String specif;//设备规格
    public String model;//设备型号
    //    public Long dutyStaffId;
//    @ToOne(joinProperty = "dutyStaffId")
    @Transient
    public Staff dutyStaff;//负责人
    public Long useDate;//启用日期
    public Long produceDate;//出厂日期
    public String produceCode;//出厂编号
    public String produceFirm;//制造厂
    public String installFirm;//安装单位
    public String areaNum;//设备位号（工艺编码）
    public Long fileDate;//建档日期
    public Float useYear;//使用年限
    public boolean haveRunState;//关注运行
    public String specialtyNew;//专业
    //    public String fileStateId;
//    @ToOne(joinProperty = "fileStateId")
    @Transient
    public SystemCodeEntity fileState;//建档标记
    public Long validDate;//有效期
    //    public String abcId;
//    @ToOne(joinProperty = "abcId")
    @Transient
    public SystemCodeEntity abc;// 重要等级
    public float score;//分数

    //    public Long electricStaffId;
//    @ToOne(joinProperty = "electricStaffId")
    @Transient
    public Staff electricStaff;//电气责任人
    //    public Long inspectionStaffId;
//    @ToOne(joinProperty = "inspectionStaffId")
    @Transient
    public Staff inspectionStaff;//巡检责任人
    //    public Long repairStaffId;
//    @ToOne(joinProperty = "repairStaffId")
    @Transient
    public Staff repairStaff;//机修责任人

    //    public Long scoreMerityId;
//    @ToOne(joinProperty = "scoreMerityId")
    @Transient
    public ScoreMerity scoreMerity;//设备评分

    @SerializedName(value = "eamAssetcode", alternate = {"eamAssetCode"})
    public String eamAssetCode; // 设备编码
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1908950980)
    private transient EamEntityDao myDao;


    @Generated(hash = 729480532)
    public EamEntity(Long id, String code, String name, String state, String stateForDisplay,
            Long eamTypeId, Long areaId, String specif, String model, Long useDate, Long produceDate,
            String produceCode, String produceFirm, String installFirm, String areaNum, Long fileDate,
            Float useYear, boolean haveRunState, String specialtyNew, Long validDate, float score,
            String eamAssetCode) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.state = state;
        this.stateForDisplay = stateForDisplay;
        this.eamTypeId = eamTypeId;
        this.areaId = areaId;
        this.specif = specif;
        this.model = model;
        this.useDate = useDate;
        this.produceDate = produceDate;
        this.produceCode = produceCode;
        this.produceFirm = produceFirm;
        this.installFirm = installFirm;
        this.areaNum = areaNum;
        this.fileDate = fileDate;
        this.useYear = useYear;
        this.haveRunState = haveRunState;
        this.specialtyNew = specialtyNew;
        this.validDate = validDate;
        this.score = score;
        this.eamAssetCode = eamAssetCode;
    }

    @Generated(hash = 933313075)
    public EamEntity() {
    }

    @Generated(hash = 1711814245)
    private transient Long installPlace__resolvedKey;
    @Generated(hash = 1947421707)
    private transient Long eamType__resolvedKey;


    public ScoreMerity getScoreMerity() {
        if (scoreMerity == null) {
            scoreMerity = new ScoreMerity();
        }
        return scoreMerity;
    }

    public boolean checkNil() {
        return null == id;
    }
    

    public Department getUseDept() {
        if (useDept == null) {
            useDept = new Department();
        }
        return useDept;
    }

    public Staff getDutyStaff() {
        if (dutyStaff == null) {
            dutyStaff = new Staff();
        }
        return dutyStaff;
    }

    public Staff getElectricStaff() {
        if (electricStaff == null) {
            electricStaff = new Staff();
        }
        return electricStaff;
    }

    public Staff getInspectionStaff() {
        if (inspectionStaff == null) {
            inspectionStaff = new Staff();
        }
        return inspectionStaff;
    }

    public Staff getRepairStaff() {
        if (repairStaff == null) {
            repairStaff = new Staff();
        }
        return repairStaff;
    }

    public SystemCodeEntity getFileState() {
        if (fileState == null) {
            fileState = new SystemCodeEntity();
        }
        return fileState;
    }

    @Override
    public String getSearchId() {
        return id == null ? "" : id.toString();
    }

    @Override
    public String getSearchName() {
        return name;
    }

    @Override
    public String getSearchCode() {
        return eamAssetCode;
    }

    @Override
    public String getSearchProperty() {
        return installPlace == null ? "" : installPlace.name;
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

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateForDisplay() {
        return this.stateForDisplay;
    }

    public void setStateForDisplay(String stateForDisplay) {
        this.stateForDisplay = stateForDisplay;
    }

    public String getSpecif() {
        return this.specif;
    }

    public void setSpecif(String specif) {
        this.specif = specif;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getUseDate() {
        return this.useDate;
    }

    public void setUseDate(Long useDate) {
        this.useDate = useDate;
    }

    public Long getProduceDate() {
        return this.produceDate;
    }

    public void setProduceDate(Long produceDate) {
        this.produceDate = produceDate;
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

    public String getInstallFirm() {
        return this.installFirm;
    }

    public void setInstallFirm(String installFirm) {
        this.installFirm = installFirm;
    }

    public String getAreaNum() {
        return this.areaNum;
    }

    public void setAreaNum(String areaNum) {
        this.areaNum = areaNum;
    }

    public Long getFileDate() {
        return this.fileDate;
    }

    public void setFileDate(Long fileDate) {
        this.fileDate = fileDate;
    }

    public Float getUseYear() {
        return this.useYear;
    }

    public void setUseYear(Float useYear) {
        this.useYear = useYear;
    }

    public boolean getHaveRunState() {
        return this.haveRunState;
    }

    public void setHaveRunState(boolean haveRunState) {
        this.haveRunState = haveRunState;
    }

    public String getSpecialtyNew() {
        return this.specialtyNew;
    }

    public void setSpecialtyNew(String specialtyNew) {
        this.specialtyNew = specialtyNew;
    }

    public Long getValidDate() {
        return this.validDate;
    }

    public void setValidDate(Long validDate) {
        this.validDate = validDate;
    }

    public float getScore() {
        return this.score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getEamAssetCode() {
        return this.eamAssetCode;
    }

    public void setEamAssetCode(String eamAssetCode) {
        this.eamAssetCode = eamAssetCode;
    }

    public Long getAreaId() {
        return this.areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 373879003)
    public void setInstallPlace(Area installPlace) {
        synchronized (this) {
            this.installPlace = installPlace;
            areaId = installPlace == null ? null : installPlace.getId();
            installPlace__resolvedKey = areaId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 701806949)
    public Area getInstallPlace() {
        Long __key = this.areaId;
        if (installPlace__resolvedKey == null || !installPlace__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AreaDao targetDao = daoSession.getAreaDao();
            Area installPlaceNew = targetDao.load(__key);
            synchronized (this) {
                installPlace = installPlaceNew;
                installPlace__resolvedKey = __key;
            }
        }
        return installPlace;
    }

    public Long getEamTypeId() {
        return this.eamTypeId;
    }

    public void setEamTypeId(Long eamTypeId) {
        this.eamTypeId = eamTypeId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 423986532)
    public EamType getEamType() {
        Long __key = this.eamTypeId;
        if (eamType__resolvedKey == null || !eamType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EamTypeDao targetDao = daoSession.getEamTypeDao();
            EamType eamTypeNew = targetDao.load(__key);
            synchronized (this) {
                eamType = eamTypeNew;
                eamType__resolvedKey = __key;
            }
        }
        return eamType;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1694060142)
    public void setEamType(EamType eamType) {
        synchronized (this) {
            this.eamType = eamType;
            eamTypeId = eamType == null ? null : eamType.getId();
            eamType__resolvedKey = eamTypeId;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 650996964)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEamEntityDao() : null;
    }

    public class ScoreMerity extends BaseEntity {
        public Long id;
        public String scoreTableNo; // 设备评分绩效单据编号
    }
}
