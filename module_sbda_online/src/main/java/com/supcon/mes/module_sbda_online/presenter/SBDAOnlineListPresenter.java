package com.supcon.mes.module_sbda_online.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_sbda_online.model.bean.SBDAOnlineListEntity;
import com.supcon.mes.module_sbda_online.model.contract.SBDAOnlineListContract;
import com.supcon.mes.module_sbda_online.model.network.SBDAOnlineHttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/3/30.
 */

public class SBDAOnlineListPresenter extends SBDAOnlineListContract.Presenter {
    @SuppressLint("CheckResult")
    @Override
    public void getSearchSBDA(Map<String, Object> params, int page) {
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());

        if (params.containsKey(Constant.BAPQuery.IS_MAIN_EQUIP)) {
            Map<String, Object> mainParam = new HashMap();
            mainParam.put(Constant.BAPQuery.IS_MAIN_EQUIP, params.get(Constant.BAPQuery.IS_MAIN_EQUIP));
            List<BaseSubcondEntity> subcondEntities = BAPQueryParamsHelper.crateSubcondEntity(mainParam);
            fastQuery.subconds.addAll(subcondEntities);
        }
        if (params.containsKey(Constant.BAPQuery.EAM_CODE)) {
            Map<String, Object> codeParam = new HashMap();
            codeParam.put(Constant.BAPQuery.EAM_CODE, params.get(Constant.BAPQuery.EAM_CODE));
            List<BaseSubcondEntity> baseSubcondEntities = BAPQueryParamsHelper.crateSubcondEntity(codeParam);
            fastQuery.subconds.addAll(baseSubcondEntities);
        }
        if (params.containsKey(Constant.BAPQuery.EAM_STATE)) {
            Map<String, Object> stateParam = new HashMap();
            stateParam.put(Constant.BAPQuery.EAM_STATE, params.get(Constant.BAPQuery.EAM_STATE));
            List<BaseSubcondEntity> baseSubcondEntities = BAPQueryParamsHelper.crateSubcondEntity(stateParam);
            fastQuery.subconds.addAll(baseSubcondEntities);
        }
        if (params.containsKey(Constant.BAPQuery.EAM_TYPE)) {
            Map<String, Object> typeParam = new HashMap();
            typeParam.put(Constant.BAPQuery.EAM_TYPE, params.get(Constant.BAPQuery.EAM_TYPE));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(typeParam, "EAM_EAMTYPE,EAMTYPE_CODE,EAM_BaseInfo,EAM_TYPE");
            fastQuery.subconds.add(joinSubcondEntity);
        }
        if (params.containsKey(Constant.BAPQuery.EAM_AREA)) {
            Map<String, Object> areaParam = new HashMap();
            areaParam.put(Constant.BAPQuery.EAM_AREA, params.get(Constant.BAPQuery.EAM_AREA));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(areaParam, "BEAM_AREAS,ID,EAM_BaseInfo,INSTALL_PLACE");
            fastQuery.subconds.add(joinSubcondEntity);
        }

        fastQuery.modelAlias = "baseInfo";
        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", 20);
        pageQueryParams.put("page.maxPageSize", 500);
        mCompositeSubscription.add(SBDAOnlineHttpClient.getPartForview(fastQuery, pageQueryParams)
                .onErrorReturn(throwable -> {
                    SBDAOnlineListEntity sbdaOnlineListEntity = new SBDAOnlineListEntity();
                    sbdaOnlineListEntity.errMsg = throwable.toString();
                    return sbdaOnlineListEntity;
                }).subscribe(sbdaOnlineListEntity -> {
                    if (TextUtils.isEmpty(sbdaOnlineListEntity.errMsg)) {
                        getView().getSearchSBDASuccess(sbdaOnlineListEntity);
                    } else {
                        getView().getSearchSBDAFailed(sbdaOnlineListEntity.errMsg);
                    }
                }));
    }
}
