package com.supcon.mes.module_overhaul_workticket.util;

import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_overhaul_workticket.model.bean.HazardPointEntity;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresEntity;
import com.supcon.mes.module_overhaul_workticket.model.dto.SafetyMeasuresEntityDto;

import java.util.ArrayList;
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
            dto.attachFileFileAddPaths = Util.strFormat2(entity.getAttachFileFileAddPaths());
            dto.attachFileMultiFileIds = Util.strFormat2(entity.getAttachFileMultiFileIds());
            dto.attachFileFileDeleteIds = Util.strFormat2(entity.getAttachFileFileDeleteIds());

            safetyMeasuresEntityDtoList.add(dto);
        }
        return safetyMeasuresEntityDtoList;
    }



}
