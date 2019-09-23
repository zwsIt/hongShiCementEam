package com.supcon.mes.middleware.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.model.bean.RefProductListEntity;
import com.supcon.mes.middleware.model.bean.SparePartRefListEntity;
import com.supcon.mes.middleware.model.contract.RefProductContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/5
 * ------------- Description -------------
 */
public class RefProductPresenter extends RefProductContract.Presenter {
    @Override
    public void listRefProduct(int pageNum, Map<String, Object> queryParam) {
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryParam);
        fastQueryCondEntity.modelAlias = "product";
        Map<String, Object> pageQueryParam = new HashMap<>();
        pageQueryParam.put("page.pageSize", 20);
        pageQueryParam.put("page.maxPageSize", 500);
        pageQueryParam.put("page.pageNo", pageNum);

        mCompositeSubscription.add(MiddlewareHttpClient.listRefProduct(fastQueryCondEntity, pageQueryParam)
                .onErrorReturn(throwable -> {
                    RefProductListEntity refProductListEntity = new RefProductListEntity();
                    refProductListEntity.errMsg = throwable.toString();
                    return refProductListEntity;
                }).subscribe(refProductListEntity -> {
                    if (refProductListEntity.errMsg == null) {
                        getView().listRefProductSuccess(refProductListEntity);
                    } else {
                        getView().listRefProductFailed(refProductListEntity.errMsg);
                    }
                }));
    }

    @Override
    public void listRefSparePart(int pageNum, Long eamID, Map<String, Object> queryParam) {
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryParam);
        fastQueryCondEntity.modelAlias = "sparePart";
        if (fastQueryCondEntity.subconds.size() > 0) {
            JoinSubcondEntity joinSubcondEntity = new JoinSubcondEntity();
            joinSubcondEntity.type = Constant.BAPQuery.TYPE_JOIN;
            joinSubcondEntity.joinInfo = "S2BASE_PRODUCT,PRODUCT_ID,BEAM_SPARE_PARTS,PRODUCTID";
            joinSubcondEntity.subconds = new ArrayList<>();  //必须创建新对象
            joinSubcondEntity.subconds.addAll(fastQueryCondEntity.subconds);

            fastQueryCondEntity.subconds.clear();
            fastQueryCondEntity.subconds.add(joinSubcondEntity);
        }


        Map<String, Object> pageQueryParam = new HashMap<>();
        pageQueryParam.put("page.pageSize", 20);
        pageQueryParam.put("page.maxPageSize", 500);
        pageQueryParam.put("page.pageNo", pageNum);
        pageQueryParam.put("eamID", eamID);

        mCompositeSubscription.add(
                MiddlewareHttpClient.listSparePartsRef(pageQueryParam, fastQueryCondEntity)
                        .onErrorReturn(new Function<Throwable, SparePartRefListEntity>() {
                            @Override
                            public SparePartRefListEntity apply(Throwable throwable) throws Exception {
                                SparePartRefListEntity sparePartRefListEntity = new SparePartRefListEntity();
                                sparePartRefListEntity.success = false;
                                sparePartRefListEntity.errMsg = throwable.toString();
                                return sparePartRefListEntity;
                            }
                        })
                        .subscribe(new Consumer<SparePartRefListEntity>() {
                            @Override
                            public void accept(SparePartRefListEntity sparePartRefListEntity) throws Exception {
                                if (TextUtils.isEmpty(sparePartRefListEntity.errMsg)) {
                                    getView().listRefSparePartSuccess(sparePartRefListEntity);
                                } else {
                                    getView().listRefSparePartFailed(sparePartRefListEntity.errMsg);
                                }
                            }
                        })
        );

    }
}
