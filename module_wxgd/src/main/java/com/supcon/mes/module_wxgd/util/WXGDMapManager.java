package com.supcon.mes.module_wxgd.util;

import android.annotation.SuppressLint;

import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.middleware.model.bean.LubricateOilsEntity;
import com.supcon.mes.middleware.model.bean.MaintainEntity;
import com.supcon.mes.middleware.model.bean.RepairStaffEntity;
import com.supcon.mes.middleware.model.bean.SparePartEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.model.dto.AcceptanceCheckEntityDto;
import com.supcon.mes.module_wxgd.model.dto.IdDto;
import com.supcon.mes.module_wxgd.model.dto.LubricateOilsEntityDto;
import com.supcon.mes.module_wxgd.model.dto.MaintainDto;
import com.supcon.mes.module_wxgd.model.dto.RepairStaffDto;
import com.supcon.mes.module_wxgd.model.dto.SparePartEntityDto;
import com.supcon.mes.module_wxgd.model.dto.StaffDto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/4
 * ------------- Description -------------
 * 提交接口参数
 */
public class WXGDMapManager {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public static Map<String, Object> createMap(WXGDEntity mWXGDEntity) {
        Map<String, Object> map = new HashMap<>();
        map.put("workRecord.createPositionId", EamApplication.getAccountInfo().positionId);
        map.put("modelName", "WorkRecord");
        map.put("bap_validate_user_id", EamApplication.getAccountInfo().userId);
        map.put("workRecord.createStaffId", EamApplication.getAccountInfo().staffId);
        map.put("workRecord.createTime", format.format(mWXGDEntity.createTime));
//DealInfo di = taskService.take(pendingId, deploymentId, workRecord.getId(), creatorService.getStaffFromSession(), workFlowVar);
        map.put("workRecord.version", mWXGDEntity.version);
        map.put("workRecord.workOrderContext", Util.strFormat2(mWXGDEntity.workOrderContext));
        map.put("deploymentId", mWXGDEntity.pending != null ? Util.strFormat2(mWXGDEntity.pending.deploymentId) : "");
        map.put("webSignetFlag", false);
        map.put("pendingId", mWXGDEntity.pending != null ? Util.strFormat2(mWXGDEntity.pending.id) : "");
        map.put("workRecord.repairGroup.id", mWXGDEntity.repairGroup != null ? Util.strFormat2(mWXGDEntity.repairGroup.id) : "");
        map.put("workRecord.faultInfo.id", mWXGDEntity.faultInfo != null ? Util.strFormat2(mWXGDEntity.faultInfo.id) : "");
        if (mWXGDEntity.id != -1) {
            map.put("id", Util.strFormat2(mWXGDEntity.id));
            map.put("workRecord.id", Util.strFormat2(mWXGDEntity.id));
        }
        map.put("workRecord.eamID.id", (mWXGDEntity.eamID != null && mWXGDEntity.eamID.id != null) ? Util.strFormat2(mWXGDEntity.eamID.id) : "");
        map.put("workRecord.planStartDate", mWXGDEntity.planStartDate == null ? "" : format.format(mWXGDEntity.planStartDate));
        map.put("workRecord.planEndDate", mWXGDEntity.planEndDate == null ? "" : format.format(mWXGDEntity.planEndDate));
        map.put("workRecord.repairAdvise", Util.strFormat2(mWXGDEntity.repairAdvise));
        map.put("workRecord.workSource.id", mWXGDEntity.workSource != null ? Util.strFormat2(mWXGDEntity.workSource.id) : "");
        map.put("workRecord.workSource.value", mWXGDEntity.workSource != null ? Util.strFormat2(mWXGDEntity.workSource.value) : "");
        map.put("workRecord.repairType.id", mWXGDEntity.repairType != null ? Util.strFormat2(mWXGDEntity.repairType.id) : "");
        map.put("workRecord.repairType.value", mWXGDEntity.repairType != null ? Util.strFormat2(mWXGDEntity.repairType.value) : "");
        map.put("workRecord.content", Util.strFormat2(mWXGDEntity.content));
        map.put("workRecord.claim", Util.strFormat2(mWXGDEntity.claim));
        map.put("workRecord.period", mWXGDEntity.period == null ? "" : mWXGDEntity.period);
        map.put("workRecord.thisDuration", Util.strFormat2(mWXGDEntity.thisDuration));
        map.put("workRecord.totalDuration", Util.strFormat2(mWXGDEntity.totalDuration));
        map.put("workRecord.lastDuration", Util.strFormat2(mWXGDEntity.lastDuration));
        map.put("workRecord.lastTime", mWXGDEntity.lastTime != null ? format.format(mWXGDEntity.lastTime) : "");
        map.put("workRecord.realEndDate", mWXGDEntity.realEndDate == null ? "" : format.format(mWXGDEntity.realEndDate));
        map.put("workRecord.periodUnit.id", mWXGDEntity.periodUnit != null ? Util.strFormat2(mWXGDEntity.periodUnit.id) : "");
        map.put("workRecord.periodUnit.value", mWXGDEntity.periodUnit != null ? Util.strFormat2(mWXGDEntity.periodUnit.value) : "");
        map.put("__file_upload", true);

        map.put("workRecord.dispatcher.id", mWXGDEntity.getDispatcher().id != null ? mWXGDEntity.getDispatcher().id : EamApplication.getAccountInfo().staffId);
        return map;
    }

    //维修人员转提交需要
    public static LinkedList<RepairStaffDto> translateStaffDto(List<RepairStaffEntity> staffs) {
        LinkedList<RepairStaffDto> staffSubmitEntities = new LinkedList<>();
        StaffDto staff;
        for (int i = 0; i < staffs.size(); i++) {
            RepairStaffDto repairStaffDto = new RepairStaffDto();
            repairStaffDto.id = staffs.get(i).id == null ? "" : String.valueOf(staffs.get(i).id);
            repairStaffDto.version = staffs.get(i).version == null ? "" : staffs.get(i).version;
            repairStaffDto.rowIndex = String.valueOf(i);

            staff = new StaffDto();
            staff.id = staffs.get(i).repairStaff == null ? "" : String.valueOf(staffs.get(i).repairStaff.id);
            repairStaffDto.repairStaff = staff;
            repairStaffDto.startTime = staffs.get(i).startTime != null ? format.format(staffs.get(i).startTime) : "";
            repairStaffDto.endTime = staffs.get(i).endTime != null ? format.format(staffs.get(i).endTime) : "";
            repairStaffDto.workHour = staffs.get(i).workHour == null ? "" : String.valueOf(staffs.get(i).workHour);
            repairStaffDto.sort = staffs.get(i).sort == null ? "" : String.valueOf(staffs.get(i).sort);
            repairStaffDto.rowIndex = String.valueOf(i);
            repairStaffDto.remark = staffs.get(i).remark;
            repairStaffDto.timesNum = Util.strFormat2(staffs.get(i).timesNum);
            staffSubmitEntities.add(repairStaffDto);
        }
        return staffSubmitEntities;
    }

    /**
     * @param
     * @return
     * @description 转化传输备件
     * @author zhangwenshuai1 2018/9/5
     */
    public static LinkedList<SparePartEntityDto> translateSparePartDto(List<SparePartEntity> list) {
        LinkedList<SparePartEntityDto> sparePartEntityDtos = new LinkedList<>();
        SparePartEntityDto sparePartEntityDto;
        IdDto idDto;
        String index;
        for (SparePartEntity sparePartEntity : list) {
            sparePartEntityDto = new SparePartEntityDto();
            sparePartEntityDto.id = sparePartEntity.id == null ? "" : String.valueOf(sparePartEntity.id);
            idDto = new IdDto();
            idDto.id = sparePartEntity.productID == null ? "" : String.valueOf(sparePartEntity.productID.id);
            sparePartEntityDto.productID = idDto;
            sparePartEntityDto.checkbox = "true";
            sparePartEntityDto.version = sparePartEntity.version != null ? sparePartEntity.version : "";
            sparePartEntityDto.sum = sparePartEntity.sum == null ? "" : String.valueOf(sparePartEntity.sum);
            index = String.valueOf(list.indexOf(sparePartEntity));
            sparePartEntityDto.sort = index;
            sparePartEntityDto.rowIndex = index;
            sparePartEntityDto.remark = sparePartEntity.remark;
            sparePartEntityDto.standingCrop = sparePartEntity.standingCrop == null ? "" : String.valueOf(sparePartEntity.standingCrop);
            sparePartEntityDto.useQuantity = sparePartEntity.useQuantity == null ? "" : String.valueOf(sparePartEntity.useQuantity);
            sparePartEntityDto.sparePartId = sparePartEntity.sparePartId == null ? "" : String.valueOf(sparePartEntity.sparePartId);
            sparePartEntityDto.actualQuantity = (sparePartEntity.actualQuantity != null && sparePartEntity.actualQuantity.intValue() != 0) ?
                    String.valueOf(sparePartEntity.actualQuantity) : "";
            idDto = new IdDto();
            idDto.id = sparePartEntity.useState == null ? "" : Util.strFormat2(sparePartEntity.useState.id);
            sparePartEntityDto.useState = idDto;
            idDto = new IdDto();
            idDto.id = sparePartEntity.periodType != null ? Util.strFormat2(sparePartEntity.periodType.id) : "";
            sparePartEntityDto.periodType = idDto;
            sparePartEntityDto.period = Util.strFormat2(sparePartEntity.period);
            idDto = new IdDto();
            idDto.id = sparePartEntity.periodUnit != null ? Util.strFormat2(sparePartEntity.periodUnit.id) : "";
            sparePartEntityDto.periodUnit = idDto;
            sparePartEntityDto.lastTime = sparePartEntity.lastTime != null ? format.format(sparePartEntity.lastTime) : "";
            sparePartEntityDto.nextTime = sparePartEntity.nextTime != null ? format.format(sparePartEntity.nextTime) : "";
            sparePartEntityDto.lastDuration = Util.strFormat2(sparePartEntity.lastDuration);
            sparePartEntityDto.nextDuration = Util.strFormat2(sparePartEntity.nextDuration);
            sparePartEntityDto.accessoryName = Util.strFormat2(sparePartEntity.accessoryName);
            sparePartEntityDto.timesNum = Util.strFormat2(sparePartEntity.timesNum);
            sparePartEntityDto.isRef = Util.strFormat2(sparePartEntity.isRef);
            sparePartEntityDtos.add(sparePartEntityDto);
        }

        return sparePartEntityDtos;
    }

    /**
     * @param
     * @return
     * @description 转化传输润滑油
     * @author zhangwenshuai1 2018/9/5
     */
    public static LinkedList<LubricateOilsEntityDto> translateLubricateOilsDto(List<LubricateOilsEntity> list) {
        LinkedList<LubricateOilsEntityDto> lubricateOilsEntityDtos = new LinkedList<>();
        LubricateOilsEntityDto lubricateOilsEntityDto;
        IdDto idDto;
        for (LubricateOilsEntity lubricateOilsEntity : list) {
            lubricateOilsEntityDto = new LubricateOilsEntityDto();
            lubricateOilsEntityDto.id = lubricateOilsEntity.id == null ? "" : Util.strFormat2(lubricateOilsEntity.id);
            idDto = new IdDto();
            idDto.id = lubricateOilsEntity.lubricate == null ? "" : Util.strFormat2(lubricateOilsEntity.lubricate.id);
            lubricateOilsEntityDto.lubricate = idDto;

            idDto = new IdDto();
            idDto.id = lubricateOilsEntity.oilType == null ? "" : Util.strFormat2(lubricateOilsEntity.oilType.id);
            lubricateOilsEntityDto.oilType = idDto;
            idDto = new IdDto();
            idDto.id = lubricateOilsEntity.jwxItemID == null ? "" : Util.strFormat2(lubricateOilsEntity.jwxItemID.id);
            lubricateOilsEntityDto.jwxItemID = idDto;
            lubricateOilsEntityDto.oilQuantity = lubricateOilsEntity.oilQuantity == null ? "" : Util.strFormat2(lubricateOilsEntity.oilQuantity);
            lubricateOilsEntityDto.remark = Util.strFormat2(lubricateOilsEntity.remark);
            lubricateOilsEntityDto.lubricatingPart = Util.strFormat2(lubricateOilsEntity.lubricatingPart);

            lubricateOilsEntityDtos.add(lubricateOilsEntityDto);
        }
        return lubricateOilsEntityDtos;
    }

    /**
     * @param
     * @return
     * @description 转化传输润滑油
     * @author zhangwenshuai1 2018/9/5
     */
    public static LinkedList<MaintainDto> translateMaintainDto(List<MaintainEntity> list) {
        LinkedList<MaintainDto> maintainDtos = new LinkedList<>();
        for (MaintainEntity maintainEntity : list) {
            MaintainDto maintainDto = new MaintainDto();
            maintainDto.id = Util.strFormat2(maintainEntity.id);
            IdDto idDto = new IdDto();
            idDto.id = Util.strFormat2(maintainEntity.getJwxItem().id);
            maintainDto.jwxItemID = idDto;
            maintainDtos.add(maintainDto);
        }
        return maintainDtos;
    }

    /**
     * @param
     * @return
     * @description 转化传输验收
     * @author zhangwenshuai1 2018/9/5
     */
    public static List<AcceptanceCheckEntityDto> translateAcceptChkDto(List<AcceptanceCheckEntity> list) {
        List<AcceptanceCheckEntityDto> acceptanceCheckEntityDtos = new ArrayList<>();
        AcceptanceCheckEntityDto acceptanceCheckEntityDto;
        SystemCodeEntity checkResult;
        for (AcceptanceCheckEntity entity : list) {
            acceptanceCheckEntityDto = new AcceptanceCheckEntityDto();

            IdDto idDto = new IdDto();
            idDto.id = entity.checkStaff == null ? "" : Util.strFormat2(entity.checkStaff.id);
            acceptanceCheckEntityDto.checkStaff = idDto;
            idDto = new IdDto();
            idDto.id = entity.checkResult == null ? "" : Util.strFormat2(entity.checkResult.id);
            acceptanceCheckEntityDto.checkResult = idDto;

            acceptanceCheckEntityDto.checkTime = entity.checkTime == null ? "" : DateUtil.dateFormat(entity.checkTime, "yyyy-MM-dd HH:mm:ss");
            acceptanceCheckEntityDto.remark = entity.remark;
            acceptanceCheckEntityDtos.add(acceptanceCheckEntityDto);
        }
        return acceptanceCheckEntityDtos;
    }

    /**
     * @param
     * @return
     * @description dataGrid删除数据id参数封装
     * @author zhangwenshuai1 2018/9/18
     */
    public static Map<String, Object> dgDeleted(Map<String, Object> map, List<Long> list, String dg) {
        if (list.size() <= 0)
            return map;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(",");
            map.put(dg + "DeletedIds[" + i + "]", list.get(i).toString());
        }
        map.put("dgDeletedIds['" + dg + "']", sb.substring(0, sb.length() - 1));
        return map;
    }
}

