package com.supcon.mes.module_warn.ui.util;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.JWXItem;
import com.supcon.mes.middleware.model.bean.LubricateOilsEntity;
import com.supcon.mes.middleware.model.bean.MaintainEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.SparePartEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.module_warn.model.bean.LubricationWarnEntity;
import com.supcon.mes.module_warn.model.bean.MaintenanceWarnEntity;
import com.supcon.mes.module_warn.model.bean.SparePartWarnEntity;

import java.util.ArrayList;
import java.util.List;

public class WXGDWarnManager {

    public static WXGDEntity lubri(LubricationWarnEntity lubricationWarnEntity) {
        WXGDEntity wxgdEntity = new WXGDEntity();
        wxgdEntity.eamID = lubricationWarnEntity.eamID;
        wxgdEntity.id = -1l;
        SystemCodeEntity workSource = new SystemCodeEntity();
        workSource.value = "润滑";
        workSource.id = "BEAM2003/02";
        wxgdEntity.workSource = workSource;
        SystemCodeEntity repairType = new SystemCodeEntity();
        repairType.value = "日常";
        repairType.id = "BEAM2005/01";
        wxgdEntity.repairType = repairType;
        wxgdEntity.workOrderContext = lubricationWarnEntity.getEamID().name
                + (TextUtils.isEmpty(lubricationWarnEntity.lubricatePart) ? "" : "润滑部位为" + lubricationWarnEntity.lubricatePart) + "的润滑预警";
        wxgdEntity.createTime = System.currentTimeMillis();
        PendingEntity pendingEntity = new PendingEntity();
//        pendingEntity.deploymentId = 1203l;
        pendingEntity.activityName = "预警";
        wxgdEntity.pending = pendingEntity;

        List<LubricateOilsEntity> lubricateOilsEntities = new ArrayList<>();
        LubricateOilsEntity lubricateOilsEntity = new LubricateOilsEntity();
        lubricateOilsEntity.lubricate = lubricationWarnEntity.lubricateOil;
        lubricateOilsEntity.oilType = lubricationWarnEntity.oilType;
        lubricateOilsEntity.lubricatingPart = lubricationWarnEntity.lubricatePart;
        lubricateOilsEntity.oilQuantity = lubricationWarnEntity.sum;

        JWXItem jwxItem = new JWXItem();
        jwxItem.id = lubricationWarnEntity.id;
        jwxItem.period = lubricationWarnEntity.period;
        jwxItem.periodType = lubricationWarnEntity.periodType;
        jwxItem.periodUnit = lubricationWarnEntity.periodUnit;
        jwxItem.accessoryEamId = lubricationWarnEntity.accessoryEamId;
        jwxItem.claim = lubricationWarnEntity.claim;
        jwxItem.content = lubricationWarnEntity.content;
        jwxItem.lastDuration = lubricationWarnEntity.lastDuration;
        jwxItem.nextDuration = lubricationWarnEntity.nextDuration;
        jwxItem.lastTime = lubricationWarnEntity.lastTime;
        jwxItem.nextTime = lubricationWarnEntity.nextTime;
        jwxItem.sparePartId = lubricationWarnEntity.sparePartId;
        lubricateOilsEntity.jwxItemID = jwxItem;
        lubricateOilsEntity.basicLubricate = "预警润滑不能删除";
        lubricateOilsEntities.add(lubricateOilsEntity);
        wxgdEntity.lubricateOils = lubricateOilsEntities;
        return wxgdEntity;
    }

    public static WXGDEntity mainten(MaintenanceWarnEntity maintenanceWarnEntity) {
        WXGDEntity wxgdEntity = new WXGDEntity();
        wxgdEntity.eamID = maintenanceWarnEntity.eamID;
        wxgdEntity.id = -1l;
        SystemCodeEntity workSource = new SystemCodeEntity();
        workSource.value = "维保";
        workSource.id = "BEAM2003/03";
        wxgdEntity.workSource = workSource;
        SystemCodeEntity repairType = new SystemCodeEntity();
        repairType.value = "日常";
        repairType.id = "BEAM2005/01";
        wxgdEntity.repairType = repairType;
        wxgdEntity.workOrderContext = maintenanceWarnEntity.getEamID().name
                + "维保内容为" + maintenanceWarnEntity.content + "的维保预警";
        wxgdEntity.createTime = System.currentTimeMillis();
        PendingEntity pendingEntity = new PendingEntity();
//        pendingEntity.deploymentId = 1203l;
        pendingEntity.activityName = "预警";
        wxgdEntity.pending = pendingEntity;

        List<MaintainEntity> maintainEntities = new ArrayList<>();
        MaintainEntity maintainEntity = new MaintainEntity();

        JWXItem jwxItem = new JWXItem();
        jwxItem.id = maintenanceWarnEntity.id;
        jwxItem.period = maintenanceWarnEntity.period;
        jwxItem.periodType = maintenanceWarnEntity.periodType;
        jwxItem.periodUnit = maintenanceWarnEntity.periodUnit;
        jwxItem.accessoryEamId = maintenanceWarnEntity.accessoryEamId;
        jwxItem.claim = maintenanceWarnEntity.claim;
        jwxItem.content = maintenanceWarnEntity.content;
        jwxItem.lastDuration = maintenanceWarnEntity.lastDuration;
        jwxItem.nextDuration = maintenanceWarnEntity.nextDuration;
        jwxItem.lastTime = maintenanceWarnEntity.lastTime;
        jwxItem.nextTime = maintenanceWarnEntity.nextTime;
        jwxItem.sparePartId = maintenanceWarnEntity.sparePartId;
        maintainEntity.jwxItemID = jwxItem;
        maintainEntity.basicJwx = "预警备件不能删除";
        maintainEntities.add(maintainEntity);
        wxgdEntity.maintainEntities = maintainEntities;
        return wxgdEntity;
    }

    public static WXGDEntity spare(SparePartWarnEntity sparePartWarnEntity) {
        WXGDEntity wxgdEntity = new WXGDEntity();
        wxgdEntity.eamID = sparePartWarnEntity.eamID;
        wxgdEntity.id = -1l;
        SystemCodeEntity workSource = new SystemCodeEntity();
        workSource.value = "备件";
        workSource.id = "BEAM2003/04";
        wxgdEntity.workSource = workSource;
        SystemCodeEntity repairType = new SystemCodeEntity();
        repairType.value = "日常";
        repairType.id = "BEAM2005/01";
        wxgdEntity.repairType = repairType;
        wxgdEntity.workOrderContext = sparePartWarnEntity.getEamID().name + "零部件为"
                + sparePartWarnEntity.getProductID().productName + "的零部件更换预警";
        wxgdEntity.createTime = System.currentTimeMillis();
        PendingEntity pendingEntity = new PendingEntity();
//        pendingEntity.deploymentId = 1203l;
        pendingEntity.activityName = "预警";
        wxgdEntity.pending = pendingEntity;
        wxgdEntity.repairSum = 1L;

        ArrayList<SparePartEntity> sparePartEntities = new ArrayList<>();
        SparePartEntity sparePartEntity = new SparePartEntity();
        sparePartEntity.accessoryName = sparePartWarnEntity.getAccessoryEamId().getAttachEamId().name;
        sparePartEntity.remark = sparePartWarnEntity.remark;
        sparePartEntity.period = sparePartWarnEntity.period;
        sparePartEntity.periodType = sparePartWarnEntity.periodType;
        sparePartEntity.periodUnit = sparePartWarnEntity.periodUnit;
        sparePartEntity.lastDuration = sparePartWarnEntity.lastDuration;
        sparePartEntity.nextDuration = sparePartWarnEntity.nextDuration;
        sparePartEntity.lastTime = sparePartWarnEntity.lastTime;
        sparePartEntity.nextTime = sparePartWarnEntity.nextTime;
        sparePartEntity.productID = sparePartWarnEntity.productID;
        sparePartEntity.sparePartId = sparePartWarnEntity.id;
        sparePartEntity.timesNum = 1;
        sparePartEntity.isWarn = true;//是否来自预警
        sparePartEntities.add(sparePartEntity);
        wxgdEntity.sparePart = sparePartEntities;
        return wxgdEntity;
    }
}
