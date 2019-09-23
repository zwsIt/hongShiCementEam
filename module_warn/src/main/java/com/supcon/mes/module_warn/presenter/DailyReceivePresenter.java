package com.supcon.mes.module_warn.presenter;

import android.text.TextUtils;

import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.contract.DailyReceiveContract;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import java.util.Map;

public class DailyReceivePresenter extends DailyReceiveContract.Presenter {
    @Override
    public void receiveTask(Map<String, Object> params) {
        mCompositeSubscription.add(EarlyWarnHttpClient.receiveTask(params)
                .onErrorReturn(throwable -> {
                    DelayEntity delayEntity = new DelayEntity();
                    delayEntity.errMsg = throwable.toString();
                    return delayEntity;
                }).subscribe(delayEntity -> {
                    if (delayEntity.success) {
                        getView().receiveTaskSuccess(delayEntity);
                    } else {
                        getView().receiveTaskFailed(TextUtils.isEmpty(delayEntity.errMsg) ? "领取失败" : delayEntity.errMsg);
                    }
                }));
    }
}
