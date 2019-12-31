package com.supcon.mes.module_hs_tsd.util;

import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_hs_tsd.model.bean.OperateItemEntity;
import com.supcon.mes.module_hs_tsd.model.dto.OperateItemEntityDto;

import java.util.ArrayList;
import java.util.List;

public class OperateItemHelper {

    public static List<OperateItemEntityDto> getOperateItemEntityDto(List<OperateItemEntity> operateItemEntityList){
        List<OperateItemEntityDto> operateItemEntityDtoList = new ArrayList<>();
        OperateItemEntityDto dto;
        for (OperateItemEntity entity: operateItemEntityList){
            dto = new OperateItemEntityDto();
            dto.rowIndex = String.valueOf(operateItemEntityList.indexOf(entity));
            dto.id = Util.strFormat2(entity.getId());
            dto.caution = Util.strFormat2(entity.getCaution());

            operateItemEntityDtoList.add(dto);
        }
        return operateItemEntityDtoList;
    }



}
