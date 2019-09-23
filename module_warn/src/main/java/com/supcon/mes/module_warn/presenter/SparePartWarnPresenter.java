package com.supcon.mes.module_warn.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_warn.model.bean.SparePartWarnListEntity;
import com.supcon.mes.module_warn.model.contract.SparePartWarnContract;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/23
 * ------------- Description -------------
 * 备件
 */
public class SparePartWarnPresenter extends SparePartWarnContract.Presenter {

    @Override
    public void getSparePart(String url, Map<String, Object> params, int page, long id) {

        if (TextUtils.isEmpty(url)) {
            url = "/BEAM/baseInfo/sparePart/data-dg1535424823416.action";
        }
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(params, "EAM_BaseInfo,EAM_ID,BEAM_SPARE_PARTS,EAMID");
        fastQuery.subconds.add(joinSubcondEntity);
        fastQuery.modelAlias = "sparePart";

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.maxPageSize", 500);
        if (id != -1) {
            pageQueryParams.put("sparePartID", id);
            pageQueryParams.put("mobileFlag", 1);
        }
        mCompositeSubscription.add(EarlyWarnHttpClient.getSparePart(url, fastQuery, pageQueryParams)
                .onErrorReturn(throwable -> {
                    SparePartWarnListEntity sparePartWarnListEntity = new SparePartWarnListEntity();
                    sparePartWarnListEntity.errMsg = throwable.toString();
                    return sparePartWarnListEntity;
                }).subscribe(sparePartWarnListEntity -> {
                    if (TextUtils.isEmpty(sparePartWarnListEntity.errMsg)) {
                        getView().getSparePartSuccess(sparePartWarnListEntity);
                    } else {
                        getView().getSparePartFailed(sparePartWarnListEntity.errMsg);
                    }
                }));
    }
}
