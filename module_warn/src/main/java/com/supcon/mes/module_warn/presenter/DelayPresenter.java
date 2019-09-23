package com.supcon.mes.module_warn.presenter;

import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.contract.DelayContract;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import java.util.Map;

public class DelayPresenter extends DelayContract.Presenter {
    @Override
    public void delayDate(Map<String, Object> param) {
        mCompositeSubscription.add(EarlyWarnHttpClient.delayDate(param)
                .onErrorReturn(throwable -> {
                    DelayEntity delayEntity = new DelayEntity();
                    delayEntity.errMsg = throwable.toString();
                    return delayEntity;
                }).subscribe(delayEntity -> {
                    if (delayEntity.success) {
                        getView().delayDateSuccess(delayEntity);
                    } else {
                        getView().delayDateFailed(delayEntity.errMsg);
                    }
                }));
    }
}
