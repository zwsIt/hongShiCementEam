package com.supcon.mes.module_sbda_online.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_sbda_online.model.bean.SparePartsLedgerListEntity;
import com.supcon.mes.module_sbda_online.model.contract.SparePartsLedgerContract;
import com.supcon.mes.module_sbda_online.model.network.SBDAOnlineHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/3
 * ------------- Description -------------
 */
public class SparePartsLedgerPresenter extends SparePartsLedgerContract.Presenter {
    @Override
    public void baseInfoProduct(Map<String, Object> params, Long productID, int page) {

        FastQueryCondEntity fastQueryCond = BAPQueryParamsHelper.createSingleFastQueryCond(params);
        fastQueryCond.modelAlias = "baseInfo";

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", 500);
        pageQueryParams.put("page.maxPageSize", 500);
        mCompositeSubscription.add(SBDAOnlineHttpClient.baseInfoProduct(fastQueryCond,productID, pageQueryParams)
                .onErrorReturn(throwable -> {
                    SparePartsLedgerListEntity sparePartsLedgerListEntity = new SparePartsLedgerListEntity();
                    sparePartsLedgerListEntity.errMsg = throwable.toString();
                    return sparePartsLedgerListEntity;
                }).subscribe(sparePartsLedgerListEntity -> {
                    if (TextUtils.isEmpty(sparePartsLedgerListEntity.errMsg)) {
                        getView().baseInfoProductSuccess(sparePartsLedgerListEntity);
                    } else {
                        getView().baseInfoProductFailed(sparePartsLedgerListEntity.errMsg);
                    }
                }));
    }
}
