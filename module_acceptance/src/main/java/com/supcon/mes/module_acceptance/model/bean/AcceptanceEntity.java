package com.supcon.mes.module_acceptance.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.Department;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.Staff;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
public class AcceptanceEntity extends BaseEntity {


    public Area area;
    public EamType beamID;
    public String checkItem;
    public Staff checkStaff;
    public Long cid;
    public Department dept;
    public Long id;
    public PendingEntity pending;
    public String tableNo;
    public Long applyDate;
    public Long createTime;
    public TableNo faultID;
    public TableNo workID;

    public Staff getCheckStaff() {
        if (checkStaff == null) {
            checkStaff = new Staff();
        }
        return checkStaff;
    }

    public Department getDept() {
        if (dept == null) {
            dept = new Department();
        }
        return dept;
    }

    public EamType getBeamID() {
        if (beamID == null) {
            beamID = new EamType();
        }
        return beamID;
    }

    public Area getArea() {
        if (area == null) {
            area = new Area();
        }
        return area;
    }

    public TableNo getFaultID() {
        if (faultID == null) {
            faultID = new TableNo();
        }
        return faultID;
    }

    public TableNo getWorkID() {
        if (workID == null) {
            workID = new TableNo();
        }
        return workID;
    }

    public class TableNo extends BaseEntity {
        public Long id;
        public String tableNo;

    }

}
