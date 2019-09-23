package com.supcon.mes.module_main.presenter;

import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_main.model.bean.AnomalyEntity;
import com.supcon.mes.module_main.model.contract.AnomalyContract;
import com.supcon.mes.module_main.model.network.MainClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/9/17
 * ------------- Description -------------
 */
public class AnomalyPresenter extends AnomalyContract.Presenter {
    @Override
    public void getAnomalyList(int page, int pageSize, Map<String, Object> params) {
        FastQueryCondEntity fastQueryCond = BAPQueryParamsHelper.createSingleFastQueryCond(params);

        fastQueryCond.modelAlias = "abnormalinfoofeam";

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", pageSize);
        pageQueryParams.put("page.maxPageSize", 500);

        mCompositeSubscription.add(MainClient.getAnomalyList(fastQueryCond, pageQueryParams)
                .onErrorReturn(new Function<Throwable, CommonBAPListEntity<AnomalyEntity>>() {
                    @Override
                    public CommonBAPListEntity<AnomalyEntity> apply(Throwable throwable) throws Exception {
                        CommonBAPListEntity<AnomalyEntity> anomalyEntity = new CommonBAPListEntity<>();
                        anomalyEntity.errMsg = throwable.toString();
                        return anomalyEntity;
                    }
                }).subscribe(new Consumer<CommonBAPListEntity<AnomalyEntity>>() {
                    @Override
                    public void accept(CommonBAPListEntity<AnomalyEntity> anomalyEntity) throws Exception {
                        if (anomalyEntity.result != null) {
                            getView().getAnomalyListSuccess(anomalyEntity);
                        } else {
                            getView().getAnomalyListFailed(anomalyEntity.errMsg);
                        }
                    }
                }));
    }
}
