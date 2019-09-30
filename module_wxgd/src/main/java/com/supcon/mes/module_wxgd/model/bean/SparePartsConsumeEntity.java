package com.supcon.mes.module_wxgd.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AttachEamEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.RepairGroupEntity;
import com.supcon.mes.middleware.model.bean.Staff;

/**
 * 备件领用记录Entity(工单模块里)
 */
public class SparePartsConsumeEntity extends BaseEntity {

    public Good productID;//备件
    public WorkListBean workList; // 表头工单id

    public Float useQuantity; // 领用量
    public Float actualQuantity; // 消耗量

    public Staff receiptor;//领用人

    public Good getProductID() {
        if (productID == null) {
            productID = new Good();
        }
        return productID;
    }

    public Staff getReceiptor() {
        if (receiptor == null) {
            receiptor = new Staff();
        }
        return receiptor;
    }

    public WorkListBean getWorkList() {
        if (workList == null) {
            workList = new WorkListBean();
        }
        return workList;
    }

    public static class WorkListBean extends BaseEntity {

        public EamType eamID;

        private Staff chargeStaff;

        public String tableNo;

        public RepairGroupEntity repairGroup; //维修组

        public RepairGroupEntity getRepairGroup() {
            if (repairGroup == null) {
                repairGroup = new RepairGroupEntity();
            }
            return repairGroup;
        }

        public Staff getChargeStaff() {
            if (chargeStaff == null) {
                chargeStaff = new Staff();
            }
            return chargeStaff;
        }

        public EamType getEamID() {
            if (eamID == null) {
                eamID = new EamType();
            }
            return eamID;
        }
    }
}
