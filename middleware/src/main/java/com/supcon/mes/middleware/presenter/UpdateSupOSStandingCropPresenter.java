package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.StandingCropEntity;
import com.supcon.mes.middleware.model.contract.UpdateSupOSStandingCropContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class UpdateSupOSStandingCropPresenter extends UpdateSupOSStandingCropContract.Presenter {
    @Override
    public void updateStandingCrop(String sparePartCodes) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.updateStandingCrop(sparePartCodes)
                        .onErrorReturn(new Function<Throwable, CommonListEntity<StandingCropEntity>>() {
                            @Override
                            public CommonListEntity<StandingCropEntity> apply(Throwable throwable) throws Exception {
                                CommonListEntity resultEntity = new CommonListEntity();
                                resultEntity.success = false;
                                resultEntity.errMsg = throwable.toString();
                                return resultEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonListEntity<StandingCropEntity>>() {
                            @Override
                            public void accept(CommonListEntity<StandingCropEntity> resultEntity) throws Exception {
                                if (resultEntity.success) {
                                    getView().updateStandingCropSuccess(resultEntity);
                                } else {
                                    getView().updateStandingCropFailed(resultEntity.errMsg);
                                }
                            }
                        })
        );
    }
}
