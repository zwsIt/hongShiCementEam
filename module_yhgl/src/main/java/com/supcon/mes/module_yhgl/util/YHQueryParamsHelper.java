package com.supcon.mes.module_yhgl.util;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangshizhan on 2018/7/18
 * Email:wangshizhan@supcom.com
 */
public class YHQueryParamsHelper {

    public static FastQueryCondEntity createFastCondEntity(Map<String, Object> queryMap) {
        FastQueryCondEntity singleFastQueryCond = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        if (queryMap.containsKey(Constant.BAPQuery.EAM_NAME)) {
            Map<String, Object> eamMap = new HashMap<>();
            eamMap.put(Constant.BAPQuery.EAM_NAME, queryMap.get(Constant.BAPQuery.EAM_NAME));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(eamMap, "EAM_BaseInfo,EAM_ID,BEAM2_FAULT_INFOS,EAMID");
            singleFastQueryCond.subconds.add(joinSubcondEntity);

        }
        if (queryMap.containsKey(Constant.BAPQuery.YH_AREA)) {
            Map<String, Object> areaMap = new HashMap<>();
            areaMap.put(Constant.BAPQuery.YH_AREA, queryMap.get(Constant.BAPQuery.YH_AREA));
            List<BaseSubcondEntity> joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(areaMap);
            singleFastQueryCond.subconds.addAll(joinSubcondEntity);
        }

        Map<String, Object> otherMap = new HashMap<>();
        otherMap.putAll(queryMap);
        otherMap.remove(Constant.BAPQuery.EAM_NAME);
        otherMap.remove(Constant.BAPQuery.YH_AREA);

        List<BaseSubcondEntity> baseSubcondEntities = BAPQueryParamsHelper.crateSubcondEntity(otherMap);
        singleFastQueryCond.subconds.addAll(baseSubcondEntities);
        return singleFastQueryCond;
    }

}
