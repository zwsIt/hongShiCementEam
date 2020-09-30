package com.supcon.mes.module_acceptance.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.Department;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.FaultInfo;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 * 设备验收list实体(验收申请)
 */
public class AcceptanceEntity extends BaseEntity {


    public Area area; // 地点
    public EamEntity beamID; // 设备
    public String checkItem; // 检修项目
    public Staff checkStaff; // 验收人
    public Long cid;
    public Department dept; // 车间
    public Long id;
    public PendingEntity pending;
    public String tableNo;
    public Long applyDate; // 申请日期
    public Long createTime;
    public Staff createStaff;
    public FaultInfo faultID; // 隐患单
    public WXGDEntity workID; // 工单
    public SystemCodeEntity checkResult; // 验收结论
    public Long deploymentId;

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

    public EamEntity getBeamID() {
        if (beamID == null) {
            beamID = new EamEntity();
        }
        return beamID;
    }

    public Area getArea() {
        if (area == null) {
            area = new Area();
        }
        return area;
    }

    public FaultInfo getFaultID() {
        if (faultID == null) {
            faultID = new FaultInfo();
        }
        return faultID;
    }

    public WXGDEntity getWorkID() {
        if (workID == null) {
            workID = new WXGDEntity();
        }
        return workID;
    }

    public class TableNo extends BaseEntity {
        public Long id;
        public String tableNo;

    }

}
