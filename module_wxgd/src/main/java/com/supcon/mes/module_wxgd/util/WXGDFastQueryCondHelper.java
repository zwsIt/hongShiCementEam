package com.supcon.mes.module_wxgd.util;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * WXGDFastQueryCondHelper 维修工单快速查询helper
 * created by zhangwenshuai1 2018/9/18
 */
public class WXGDFastQueryCondHelper {

    public static FastQueryCondEntity createFastQueryCond(Map<String, Object> queryMap) {
        FastQueryCondEntity singleFastQueryCond = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        if (queryMap.containsKey(Constant.BAPQuery.EAM_NAME)) {
            Map<String, Object> nameParam = new HashMap<>();
            nameParam.put(Constant.BAPQuery.EAM_NAME, Objects.requireNonNull(queryMap.get(Constant.BAPQuery.EAM_NAME)));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(nameParam, "EAM_BaseInfo,EAM_ID,BEAM2_WORK_RECORDS,EAMID");
            singleFastQueryCond.subconds.add(joinSubcondEntity);
        }
        if (queryMap.containsKey(Constant.BAPQuery.WXGD_PRIORITY)) {
            Map<String, Object> areaMap = new HashMap<>();
            areaMap.put(Constant.BAPQuery.WXGD_PRIORITY, queryMap.get(Constant.BAPQuery.WXGD_PRIORITY));
            List<BaseSubcondEntity> joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(areaMap);
            singleFastQueryCond.subconds.addAll(joinSubcondEntity);
        }

        Map<String, Object> otherMap = new HashMap<>();
        otherMap.putAll(queryMap);
        otherMap.remove(Constant.BAPQuery.EAM_NAME);
        otherMap.remove(Constant.BAPQuery.WXGD_PRIORITY);

        List<BaseSubcondEntity> baseSubcondEntities = BAPQueryParamsHelper.crateSubcondEntity(otherMap);
        singleFastQueryCond.subconds.addAll(baseSubcondEntities);
        return singleFastQueryCond;
    }

}
