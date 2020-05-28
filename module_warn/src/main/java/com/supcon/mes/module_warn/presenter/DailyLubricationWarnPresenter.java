package com.supcon.mes.module_warn.presenter;

import android.text.TextUtils;

import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskListEntity;
import com.supcon.mes.module_warn.model.bean.LubricationWarnListEntity;
import com.supcon.mes.module_warn.model.contract.DailyLubricationWarnContract;
import com.supcon.mes.module_warn.model.contract.LubricationWarnContract;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/23
 * ------------- Description -------------
 * 润滑
 */
public class DailyLubricationWarnPresenter extends DailyLubricationWarnContract.Presenter {

    @Override
    public void getLubrications(Map<String, Object> params, Map<String, Object> pageQueryParams) {
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());

        Map<String, Object> paramsEam = new HashMap<>();
        if (params.containsKey(Constant.BAPQuery.EAM_CODE)) {
            paramsEam.put(Constant.BAPQuery.EAM_CODE, Objects.requireNonNull(params.get(Constant.BAPQuery.EAM_CODE)));
        }
        if (params.containsKey(Constant.BAPQuery.EAM_ID)) {
            paramsEam.put(Constant.BAPQuery.EAM_ID, Objects.requireNonNull(params.get(Constant.BAPQuery.EAM_ID)));
        }
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(paramsEam, "EAM_BaseInfo,EAM_ID,BEAM_JWXITEMS,EAMID");
//        if (params.containsKey(Constant.BAPQuery.NAME)) {  // 通过设备权限控制，不需要查询设备巡检责任人筛选
//            Map<String, Object> paramsName = new HashMap<>();
//            paramsName.put(Constant.BAPQuery.NAME, params.get(Constant.BAPQuery.NAME));
//            paramsName.put(Constant.BAPQuery.ID, EamApplication.getAccountInfo().staffId);
//            JoinSubcondEntity joinSubcondEntityStaff = BAPQueryParamsHelper.createJoinSubcondEntity(paramsName, "base_staff,ID,EAM_BaseInfo,INSPECTION_STAFF");
//            joinSubcondEntity.subconds.add(joinSubcondEntityStaff);
//        }
        fastQuery.subconds.add(joinSubcondEntity);
        fastQuery.modelAlias = "jWXItem";
        pageQueryParams.put("page.pageSize", 5000);
        pageQueryParams.put("page.maxPageSize", 500);
        Api.getInstance().setTimeOut(600000);
        mCompositeSubscription.add(EarlyWarnHttpClient.getLubrications(fastQuery, pageQueryParams)
                .onErrorReturn(throwable -> {
                    DailyLubricateTaskListEntity lubricationWarnListEntity = new DailyLubricateTaskListEntity();
                    lubricationWarnListEntity.errMsg = throwable.toString();
                    return lubricationWarnListEntity;
                }).subscribe(lubricationWarnListEntity -> {
                    Api.getInstance().setTimeOut(300000);
                    if (TextUtils.isEmpty(lubricationWarnListEntity.errMsg)) {
                        getView().getLubricationsSuccess(lubricationWarnListEntity);
                    } else {
                        getView().getLubricationsFailed(lubricationWarnListEntity.errMsg);
                    }
                }));
    }
}
