package com.supcon.mes.module_overhaul_workticket.util;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_overhaul_workticket.model.bean.HazardPointEntity;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresEntity;
import com.supcon.mes.module_overhaul_workticket.model.dto.SafetyMeasuresEntityDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WorkTicketHelper {
    public static List<HazardPointEntity> getHazardPointBySystemCode(List<SystemCodeEntity> systemCodeEntityList,String ids){
        List<HazardPointEntity> list = new ArrayList<>();
        HazardPointEntity hazardPointEntity;
        for (SystemCodeEntity systemCodeEntity:systemCodeEntityList){
            hazardPointEntity = new HazardPointEntity();
            hazardPointEntity.value = systemCodeEntity.value;
            hazardPointEntity.id = systemCodeEntity.id;

            if (ids != null && ids.contains(systemCodeEntity.id)){
                hazardPointEntity.checked = true;
            }
            list.add(hazardPointEntity);
        }
        return list;
    }

    public static List<SafetyMeasuresEntityDto> getSafetyMeasuresDto(List<SafetyMeasuresEntity> safetyMeasuresEntityList){
        List<SafetyMeasuresEntityDto> safetyMeasuresEntityDtoList = new ArrayList<>();
        SafetyMeasuresEntityDto dto;
        for (SafetyMeasuresEntity entity: safetyMeasuresEntityList){
            dto = new SafetyMeasuresEntityDto();
            dto.rowIndex = String.valueOf(safetyMeasuresEntityList.indexOf(entity));
            dto.id = Util.strFormat2(entity.getId());
            dto.isExecuted = Util.strFormat2(entity.isIsExecuted());
            dto.safetyMeasure = Util.strFormat2(entity.getSafetyMeasure());
            dto.operateType = Util.strFormat2(entity.getOperateType());

//            List<String> addPaths = new ArrayList<>();
//            addPaths = Arrays.asList(Util.strFormat2(entity.getAttachFileFileAddPaths()).split(","));

//            if (!TextUtils.isEmpty(entity.getAttachFileFileAddPaths())){
//                dto.attachFileFileAddPaths = Arrays.asList(Util.strFormat2(entity.getAttachFileFileAddPaths()).split(",")); // 数组方式
//            }
            dto.attachFileFileAddPaths = entity.getAttachFileFileAddPaths();
//            if (!TextUtils.isEmpty(entity.getAttachFileMultiFileIds())){
//                dto.attachFileMultiFileIds = Arrays.asList(Util.strFormat2(entity.getAttachFileMultiFileIds())); // 数组方式
//            }
            dto.attachFileMultiFileIds = entity.getAttachFileMultiFileIds();
//            if (!TextUtils.isEmpty(entity.getAttachFileFileDeleteIds())){
//                dto.attachFileFileDeleteIds = Arrays.asList(Util.strFormat2(entity.getAttachFileFileDeleteIds())); // 数组方式
//            }
            dto.attachFileFileDeleteIds = entity.getAttachFileFileDeleteIds();

                    safetyMeasuresEntityDtoList.add(dto);
        }
        return safetyMeasuresEntityDtoList;
    }



}
