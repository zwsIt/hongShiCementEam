package com.supcon.mes.module_wxgd.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_wxgd.model.bean.SparePartsConsumeEntity;
import com.supcon.mes.module_wxgd.model.contract.SparePartReceiveRecordContract;
import com.supcon.mes.module_wxgd.model.contract.SparePartsConsumeLedgerContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/3
 * ------------- Description -------------
 */
public class SparePartReceiveRecordPresenter extends SparePartReceiveRecordContract.Presenter {
    @Override
    public void sparePartList(Map<String, Object> params, int page) {

        FastQueryCondEntity fastQueryCond = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());

        if (params.containsKey(Constant.BAPQuery.PRODUCT_NAME)||params.containsKey(Constant.BAPQuery.PRODUCT_CODE)) {
            Map<String, Object> productParam = new HashMap<>();
            productParam.put(Constant.BAPQuery.PRODUCT_NAME, params.get(Constant.BAPQuery.PRODUCT_NAME));
            productParam.put(Constant.BAPQuery.PRODUCT_CODE, params.get(Constant.BAPQuery.PRODUCT_CODE));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(productParam, "S2BASE_PRODUCT,PRODUCT_ID,BEAM2_SPARE_PARTS,PRODUCTID");
            fastQueryCond.subconds.add(joinSubcondEntity);
        }
        fastQueryCond.modelAlias = "sparePart";

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", 500);
        pageQueryParams.put("page.maxPageSize", 500);
        mCompositeSubscription.add(HttpClient.sparePartList(fastQueryCond, pageQueryParams)
                .onErrorReturn(throwable -> {
                    CommonBAPListEntity<SparePartsConsumeEntity> bapListEntity = new CommonBAPListEntity<>();
                    bapListEntity.errMsg = throwable.toString();
                    return bapListEntity;
                }).subscribe(new Consumer<CommonBAPListEntity<SparePartsConsumeEntity>>() {
                    @Override
                    public void accept(CommonBAPListEntity<SparePartsConsumeEntity> sparePartsEntity) throws Exception {
                        if (TextUtils.isEmpty(sparePartsEntity.errMsg)) {
                            getView().sparePartListSuccess(sparePartsEntity);
                        } else {
                            getView().sparePartListFailed(sparePartsEntity.errMsg);
                        }
                    }
                }));
    }

}
