package com.supcon.mes.module_acceptance.ui.util;

import android.annotation.SuppressLint;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceEditEntity;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceEntity;
import com.supcon.mes.module_acceptance.model.dto.AcceptanceDto;

import org.reactivestreams.Publisher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class AcceptanceMapManager {
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static Map<String, Object> createMap(AcceptanceEntity acceptanceEntity) {
        Map<String, Object> map = new HashMap<>();
        map.put("viewselect", "checkApplyEdit");
        map.put("datagridKey", "BEAM2_checkApply_checkApply_checkApplyEdit_datagrids");
        map.put("viewCode", "BEAM2_1.0.0_checkApply_checkApplyEdit");
        map.put("modelName", "CheckApply");
        map.put("id", acceptanceEntity.id);
        map.put("checkApply.createStaffId", Util.strFormat2(acceptanceEntity.getCheckStaff().id));
        map.put("checkApply.createTime", format.format(acceptanceEntity.createTime != null ? acceptanceEntity.createTime : System.currentTimeMillis()));
        map.put("checkApply.createPositionId", EamApplication.getAccountInfo().positionId);
        map.put("checkApply.dept.id", Util.strFormat2(acceptanceEntity.getDept().id));
        map.put("checkApply.faultID.id", Util.strFormat2(acceptanceEntity.getFaultID().id));
        map.put("checkApply.workID.id", Util.strFormat2(acceptanceEntity.getWorkID().id));
        map.put("checkApply.beamID.id", Util.strFormat2(acceptanceEntity.getBeamID().id));
        map.put("checkApply.checkStaff.id", Util.strFormat2(acceptanceEntity.getCheckStaff().id));
        map.put("checkApply.area.id", Util.strFormat2(acceptanceEntity.getArea().id));
        map.put("checkApply.applyDate", format.format(acceptanceEntity.applyDate != null ? acceptanceEntity.applyDate : System.currentTimeMillis()));
        map.put("checkApply.checkItem", Util.strFormat2(acceptanceEntity.checkItem));

        map.put("pendingId", acceptanceEntity.pending != null ? Util.strFormat2(acceptanceEntity.pending.id) : "");


        map.put("bap_validate_user_id", EamApplication.getAccountInfo().userId);
        return map;
    }


    @SuppressLint("CheckResult")
    public static List<AcceptanceDto> dataChange(List<AcceptanceEditEntity> acceptanceEditEntities) {
        List<AcceptanceDto> acceptanceDtos = new ArrayList<>();
        Flowable.fromIterable(acceptanceEditEntities)
                .flatMap(new Function<AcceptanceEditEntity, Publisher<AcceptanceEditEntity>>() {
                    @Override
                    public Publisher<AcceptanceEditEntity> apply(AcceptanceEditEntity acceptanceEditEntity) throws Exception {
                        if (acceptanceEditEntity.categorys.size() > 0) {
                            return Flowable.fromIterable(acceptanceEditEntity.categorys);
                        } else {
                            return Flowable.just(acceptanceEditEntity);
                        }
                    }
                })
                .subscribe(acceptanceEditEntity -> {
                    AcceptanceDto acceptanceDto = new AcceptanceDto();
                    acceptanceDto.id = Util.strFormat2(acceptanceEditEntity.id);
                    acceptanceDto.item = Util.strFormat2(acceptanceEditEntity.item);
                    acceptanceDto.result = Util.strFormat2(acceptanceEditEntity.result);
                    acceptanceDto.isItemValue = Util.strFormat2(acceptanceEditEntity.isItemValue);
                    acceptanceDto.noItemValue = Util.strFormat2(acceptanceEditEntity.noItemValue);
                    acceptanceDto.conclusion = Util.strFormat2(acceptanceEditEntity.conclusion);
                    acceptanceDto.defaultValue = Util.strFormat2(acceptanceEditEntity.defaultValue);
                    acceptanceDto.defaultValueType = acceptanceEditEntity.defaultValueType;

                    acceptanceDto.category = Util.strFormat2(acceptanceEditEntity.category);
                    acceptanceDto.total = Util.strFormat2(acceptanceEditEntity.total);
                    acceptanceDtos.add(acceptanceDto);
                });
        return acceptanceDtos;
    }
}
