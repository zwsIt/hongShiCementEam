package com.supcon.mes.module_warn.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskListEntity;
import com.supcon.mes.module_warn.model.bean.TemLubricateTaskEntity;
import com.supcon.mes.module_warn.model.contract.TemporaryContract;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class TemporaryPresenter extends TemporaryContract.Presenter {
    @Override
    public void getTempLubrications(Map<String, Object> params, Map<String, Object> pageQueryParams) {
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        if (params.containsKey(Constant.BAPQuery.EAM_CODE)) {
            Map<String, Object> paramsEam = new HashMap<>();
            paramsEam.put(Constant.BAPQuery.EAM_CODE, params.get(Constant.BAPQuery.EAM_CODE));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(paramsEam, "EAM_BaseInfo,EAM_ID,BEAM_JWXITEMS,EAMID");
            fastQuery.subconds.add(joinSubcondEntity);
        }
        fastQuery.modelAlias = "jWXItem";

        pageQueryParams.put("page.pageSize", 500);
        pageQueryParams.put("page.maxPageSize", 500);

        mCompositeSubscription.add(EarlyWarnHttpClient.getTempLubrications(fastQuery, pageQueryParams)
                .onErrorReturn(throwable -> {
                    CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                    commonBAPListEntity.errMsg = throwable.toString();
                    return commonBAPListEntity;
                }).subscribe(new Consumer<CommonBAPListEntity<TemLubricateTaskEntity>>() {
                    @Override
                    public void accept(CommonBAPListEntity<TemLubricateTaskEntity> temLubricateTaskEntity) throws Exception {
                        if (TextUtils.isEmpty(temLubricateTaskEntity.errMsg)) {
                            getView().getTempLubricationsSuccess(temLubricateTaskEntity);
                        } else {
                            getView().getTempLubricationsFailed(temLubricateTaskEntity.errMsg);
                        }
                    }
                }));
    }

}
