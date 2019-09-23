package com.supcon.mes.module_acceptance.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceListEntity;
import com.supcon.mes.module_acceptance.model.contract.AcceptanceListContract;
import com.supcon.mes.module_acceptance.model.network.AcceptanceHttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
public class AcceptanceListPresenter extends AcceptanceListContract.Presenter {
    @Override
    public void getAcceptanceList(Map<String, Object> param, int page) {
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        fastQuery.modelAlias = "checkApply";
        if (param.containsKey(Constant.BAPQuery.EAM_CODE) || param.containsKey(Constant.BAPQuery.EAM_NAME)) {
            Map<String, Object> eamParam = new HashMap<>();
            eamParam.put(Constant.BAPQuery.EAM_CODE, param.get(Constant.BAPQuery.EAM_CODE));
            eamParam.put(Constant.BAPQuery.EAM_NAME, param.get(Constant.BAPQuery.EAM_NAME));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(eamParam, "EAM_BaseInfo,EAM_ID,BEAM2_CHECK_APPLIES,BEAMID");
            fastQuery.subconds.add(joinSubcondEntity);
        }

        if (param.containsKey(Constant.BAPQuery.TABLE_NO)) {
            Map<String, Object> tabParam = new HashMap<>();
            tabParam.put(Constant.BAPQuery.TABLE_NO, param.get(Constant.BAPQuery.TABLE_NO));
            List<BaseSubcondEntity> baseSubcondEntities = BAPQueryParamsHelper.crateSubcondEntity(tabParam);
            fastQuery.subconds.addAll(baseSubcondEntities);
        }

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", 20);
        pageQueryParams.put("page.maxPageSize", 500);
        mCompositeSubscription.add(AcceptanceHttpClient.getAcceptanceList(fastQuery, pageQueryParams)
                .onErrorReturn(new Function<Throwable, AcceptanceListEntity>() {
                    @Override
                    public AcceptanceListEntity apply(Throwable throwable) throws Exception {
                        AcceptanceListEntity acceptanceListEntity = new AcceptanceListEntity();
                        acceptanceListEntity.errMsg = throwable.toString();
                        return acceptanceListEntity;
                    }
                })
                .subscribe(new Consumer<AcceptanceListEntity>() {
                    @Override
                    public void accept(AcceptanceListEntity acceptanceListEntity) throws Exception {
                        if (TextUtils.isEmpty(acceptanceListEntity.errMsg)) {
                            getView().getAcceptanceListSuccess(acceptanceListEntity);
                        } else {
                            getView().getAcceptanceListFailed(acceptanceListEntity.errMsg);
                        }
                    }
                }));
    }
}
