package com.supcon.mes.module_olxj.presenter;

import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJStatisticsEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJStatisticsContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class OLXJStatisticsPresenter extends OLXJStatisticsContract.Presenter {
    @Override
    public void getInspectStaticsInfo(Map<String, Object> queryParam) {
        mCompositeSubscription.add(
                OLXJClient.getInspectStaticsInfo(queryParam)
                        .onErrorReturn(new Function<Throwable, CommonListEntity<OLXJStatisticsEntity>>() {
                            @Override
                            public CommonListEntity<OLXJStatisticsEntity> apply(Throwable throwable) throws Exception {
                                CommonListEntity<OLXJStatisticsEntity> olxjStatisticsEntity = new CommonListEntity<>();
                                olxjStatisticsEntity.errMsg = throwable.toString();
                                return olxjStatisticsEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonListEntity<OLXJStatisticsEntity>>() {
                            @Override
                            public void accept(CommonListEntity<OLXJStatisticsEntity> olxjStatisticsEntity) throws Exception {
                                if (olxjStatisticsEntity.success) {
                                    getView().getInspectStaticsInfoSuccess(olxjStatisticsEntity);
                                } else {
                                    getView().getInspectStaticsInfoFailed(olxjStatisticsEntity.errMsg);
                                }
                            }
                        }));
    }
}
