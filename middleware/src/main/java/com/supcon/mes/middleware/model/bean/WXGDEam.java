package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * WXGDEam 维修工单_设备（部分字段）
 * created by zhangwenshuai1 2018/8/13
 */
public class WXGDEam extends BaseEntity implements CheckNil {

    public Long id;
    public String code;
    public String name;
    public String model;
    public EamType eamType;//设备类型
    public Area installPlace;//区域位置
    public Staff inspectionStaff;//巡检负责人

    @Override
    public boolean checkNil() {
        return null == id;
    }

    public Staff getInspectionStaff() {
        if (inspectionStaff == null) {
            inspectionStaff = new Staff();
        }
        return inspectionStaff;
    }

    public Area getInstallPlace() {
        if (installPlace==null) {
            installPlace = new Area();
        }
        return installPlace;
    }
}
