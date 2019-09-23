package com.supcon.mes.module_main.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.Department;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.ValueEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 */
public class EamEntity extends BaseEntity {
    public Long id;
    public String code;
    public String name;

    public String state;//设备状态
    public String stateForDisplay;
    public EamType eamType;//设备类型
    public Area installPlace;//区域位置

    public Department useDept;//部门
    public String specif;//设备规格
    public String model;//设备型号
    public Staff dutyStaff;//负责人
    public Long useDate;//启用日期
    public Long produceDate;//出厂日期
    public String produceCode;//出厂编号
    public String produceFirm;//制造厂
    public String installFirm;//安装单位
    public String areaNum;//设备位号
    public Long fileDate;//建档日期
    public Float useYear;//使用年限
    public boolean haveRunState;//关注运行
    public String specialtyNew;//专业
    public ValueEntity fileState;//建档标记
    public Long validDate;//有效期
    public String abc;//资产abc
    public String abcForDisplay;//资产abc
    public float score;//分数

    public Staff electricStaff;//电气责任人
    public Staff inspectionStaff;//巡检责任人
    public Staff repairStaff;//机修责任人

    public ScoreMerity scoreMerity;//设备评分

    public ScoreMerity getScoreMerity() {
        if (scoreMerity == null) {
            scoreMerity = new ScoreMerity();
        }
        return scoreMerity;
    }

    public boolean checkNil() {
        return null == id;
    }

    public EamType getEamType() {
        if (eamType == null) {
            eamType = new EamType();
        }
        return eamType;
    }

    public Area getInstallPlace() {
        if (installPlace == null) {
            installPlace = new Area();
        }
        return installPlace;
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

    public ValueEntity getFileState() {
        if (fileState == null) {
            fileState = new ValueEntity();
        }
        return fileState;
    }

    public class ScoreMerity {
        public Long id;
        public String scoreTableNo;
    }
}
