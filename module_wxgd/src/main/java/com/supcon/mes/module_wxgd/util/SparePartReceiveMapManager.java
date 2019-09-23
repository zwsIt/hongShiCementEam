package com.supcon.mes.module_wxgd.util;

import android.annotation.SuppressLint;

import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.middleware.model.bean.LubricateOilsEntity;
import com.supcon.mes.middleware.model.bean.MaintainEntity;
import com.supcon.mes.middleware.model.bean.RepairStaffEntity;
import com.supcon.mes.middleware.model.bean.SparePartEntity;
import com.supcon.mes.middleware.model.bean.SparePartReceiveEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.model.dto.AcceptanceCheckEntityDto;
import com.supcon.mes.module_wxgd.model.dto.IdDto;
import com.supcon.mes.module_wxgd.model.dto.LubricateOilsEntityDto;
import com.supcon.mes.module_wxgd.model.dto.MaintainDto;
import com.supcon.mes.module_wxgd.model.dto.RepairStaffDto;
import com.supcon.mes.module_wxgd.model.dto.SparePartEntityDto;
import com.supcon.mes.module_wxgd.model.dto.SparePartReceiveDto;
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
public class SparePartReceiveMapManager {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public static Map<String, Object> createMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("bap_validate_user_id", EamApplication.getAccountInfo().userId);
        map.put("apply.createStaffId", EamApplication.getAccountInfo().staffId);
        map.put("apply.createTime", format.format(System.currentTimeMillis()));
        map.put("apply.createPositionId", EamApplication.getAccountInfo().positionId);
        map.put("viewselect", "sparePartEdit");
        map.put("datagridKey", "BEAM2_sparePart_apply_sparePartEdit_datagrids");
        map.put("viewCode", "BEAM2_1.0.0_sparePart_sparePartEdit");
        map.put("modelName", "Apply");
        map.put("apply.applyTime", sdf.format(System.currentTimeMillis()));
        map.put("taskDescription", "BEAM2_1.0.0.sparePartApply.task340");
        map.put("activityName", "task340");
        map.put("apply.applyStaff.id", EamApplication.getAccountInfo().staffId);

        map.put("__file_upload", true);
        return map;
    }

    public static LinkedList<SparePartReceiveDto> dataChange(List<SparePartReceiveEntity> sparePartReceiveEntities) {
        LinkedList<SparePartReceiveDto> sparePartReceiveDtos = new LinkedList<>();
        for (SparePartReceiveEntity sparePartReceiveEntity : sparePartReceiveEntities) {
            SparePartReceiveDto sparePartReceiveDto = new SparePartReceiveDto();
            sparePartReceiveDto.currDemandQuity = Util.strFormat2(sparePartReceiveEntity.currDemandQuity);
            sparePartReceiveDto.origDemandQuity = Util.strFormat2(sparePartReceiveEntity.origDemandQuity);
            sparePartReceiveDto.remark = Util.strFormat2(sparePartReceiveEntity.remark);
            IdDto idDto = new IdDto();
            idDto.id = Util.strFormat2(sparePartReceiveEntity.getSparePartId().id);
            sparePartReceiveDto.sparePartId = idDto;
            sparePartReceiveDtos.add(sparePartReceiveDto);
        }
        return sparePartReceiveDtos;
    }

}

