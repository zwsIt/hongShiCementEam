package com.supcon.mes.module_warn.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_warn.model.bean.LubricationWarnListEntity;
import com.supcon.mes.module_warn.model.contract.LubricationWarnContract;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/23
 * ------------- Description -------------
 * 润滑
 */
public class LubricationWarnPresenter extends LubricationWarnContract.Presenter {

    @Override
    public void getLubrication(String url, Map<String, Object> params, int page, long id) {
        if (TextUtils.isEmpty(url)) {
            url = "/BEAM/baseInfo/jWXItem/data-dg1530747504994.action";
        }

        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(params, "EAM_BaseInfo,EAM_ID,BEAM_JWXITEMS,EAMID");
        fastQuery.subconds.add(joinSubcondEntity);
        fastQuery.modelAlias = "jWXItem";

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.maxPageSize", 500);
        if (id != -1) {
            pageQueryParams.put("jwxID", id);
            pageQueryParams.put("mobileFlag", 1);
        }
        mCompositeSubscription.add(EarlyWarnHttpClient.getLubrication(url, fastQuery, pageQueryParams)
                .onErrorReturn(throwable -> {
                    LubricationWarnListEntity lubricationWarnListEntity = new LubricationWarnListEntity();
                    lubricationWarnListEntity.errMsg = throwable.toString();
                    return lubricationWarnListEntity;
                }).subscribe(lubricationWarnListEntity -> {
                    if (TextUtils.isEmpty(lubricationWarnListEntity.errMsg)) {
                        getView().getLubricationSuccess(lubricationWarnListEntity);
                    } else {
                        getView().getLubricationFailed(lubricationWarnListEntity.errMsg);
                    }
                }));
    }
}
