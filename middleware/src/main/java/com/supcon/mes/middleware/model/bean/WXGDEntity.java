package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * WXGDEntity 维修工单实体
 * created by zhangwenshuai1 2018/8/8
 */
public class WXGDEntity extends BaseEntity implements Cloneable {

    public Long id;

    public Staff chargeStaff; //负责人

    public Staff dispatcher; //派单人

    public SystemCodeEntity checkResult; //验收结论

    public String claim; //要求

    public String content; //内容

    public WXGDEam eamID; //设备

    public FaultInfo faultInfo; //隐患信息

    public BigDecimal lastDuration; //上次执行时长(H)
    public Long lastTime; //上次执行时间

    public String lubricateOil; //润滑油
    public Long lubricateOilId; //润滑油ID

    public String lubricatingPart; //润滑部位
    public Long lubricatingPartId; //润滑部位ID

    public Long nextTime; //下次执行时间

    public SystemCodeEntity oilType; //加/换油

    public Integer period; //周期

    public SystemCodeEntity periodUnit; //周期单位

    public Long planEndDate; //计划完成时间

    public Long planStartDate; //计划开始时间

    public Long createTime; //创建时间

    public Long realEndDate; //实际完成时间

    public Long realStartDate; //实际开始时间

    public String receiptInfo; //派单标识

    public RepairGroupEntity repairGroup; //维修组

    public Long repairSum; //维修次数

    public Long sum; //数量

    public long version;//版本

    public BigDecimal thisDuration; //本次运行时长(H)

    public BigDecimal totalDuration; //累计运行时长(H)

    public String unitName; //单位

    public SystemCodeEntity workSource; //工单来源

    public SystemCodeEntity repairType; //维修类型

    public SystemCodeEntity workType; //工单状态

    public String tableNo;//单据编号

    public PendingEntity pending;  //待办

    public Long tableInfoId;//单据ID

    public ArrayList<SparePartEntity> sparePart;  //备件

    public List<LubricateOilsEntity> lubricateOils;  //润滑

    public List<RepairStaffEntity> repairStaffs;  //维修人员

    public List<AcceptanceCheckEntity> accceptanceCheck;  //验收

    public List<MaintainEntity> maintainEntities;//维保

    public JWXItem jwxItem; //业务规则

    public Boolean whetherBornSparePart; // 是否生成领用单

    public String repairAdvise; //维修建议

    public String workOrderContext; //工单内容

    public List<AttachmentEntity> attachmentEntities;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public PendingEntity getPending() {
        if (pending == null) {
            pending = new PendingEntity();
        }
        return pending;
    }

    public Staff getChargeStaff() {
        if (chargeStaff == null) {
            chargeStaff = new Staff();
        }
        return chargeStaff;
    }

    public Staff getDispatcher() {
        if (dispatcher == null) {
            dispatcher = new Staff();
        }
        return dispatcher;
    }
}
