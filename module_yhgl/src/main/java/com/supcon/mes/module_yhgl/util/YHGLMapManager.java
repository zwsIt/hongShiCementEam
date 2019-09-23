package com.supcon.mes.module_yhgl.util;

import android.annotation.SuppressLint;

import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.middleware.model.bean.LubricateOilsEntity;
import com.supcon.mes.middleware.model.bean.MaintainEntity;
import com.supcon.mes.middleware.model.bean.RepairStaffEntity;
import com.supcon.mes.middleware.model.bean.SparePartEntity;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_yhgl.model.dto.AcceptanceCheckEntityDto;
import com.supcon.mes.module_yhgl.model.dto.IdDto;
import com.supcon.mes.module_yhgl.model.dto.LubricateOilsEntityDto;
import com.supcon.mes.module_yhgl.model.dto.MaintainDto;
import com.supcon.mes.module_yhgl.model.dto.RepairStaffDto;
import com.supcon.mes.module_yhgl.model.dto.SparePartEntityDto;
import com.supcon.mes.module_yhgl.model.dto.StaffDto;

import java.text.SimpleDateFormat;
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
public class YHGLMapManager {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            sparePartEntityDto.isRef = Util.strFormat2(sparePartEntity.isRef);
            sparePartEntityDto.timesNum = Util.strFormat2(sparePartEntity.timesNum);
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
            lubricateOilsEntityDto.remark = lubricateOilsEntity.remark;
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
     * @description 验收结论提交
     * @param
     * @return
     * @author zhangwenshuai1 2019/9/20
     *
     */
    public static LinkedList<AcceptanceCheckEntityDto> translateCheckResultDto(List<AcceptanceCheckEntity> list){
        LinkedList<AcceptanceCheckEntityDto> checkEntityDtos = new LinkedList<>();
        AcceptanceCheckEntityDto acceptanceCheckEntityDto;
        IdDto idDto;
        for (AcceptanceCheckEntity checkEntity : list){
            acceptanceCheckEntityDto = new AcceptanceCheckEntityDto();
            acceptanceCheckEntityDto.sort = "0";
            acceptanceCheckEntityDto.rowIndex = "0";
            acceptanceCheckEntityDto.checkTime = DateUtil.dateTimeFormat(checkEntity.checkTime);
            idDto = new IdDto(Util.strFormat2(checkEntity.getCheckStaff().id));
            acceptanceCheckEntityDto.checkStaff = idDto;
            idDto = new IdDto(Util.strFormat2(checkEntity.getCheckResult().id));
            acceptanceCheckEntityDto.checkResult = idDto;
            checkEntityDtos.add(acceptanceCheckEntityDto);
        }
        return checkEntityDtos;
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

