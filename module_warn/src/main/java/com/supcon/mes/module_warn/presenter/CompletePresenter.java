package com.supcon.mes.module_warn.presenter;

import android.text.TextUtils;

import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.contract.CompleteContract;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import java.util.Map;

public class CompletePresenter extends CompleteContract.Presenter {
    @Override
    public void dailyComplete(Map<String, Object> param) {
        mCompositeSubscription.add(EarlyWarnHttpClient.dailyComplete(param)
                .onErrorReturn(throwable -> {
                    DelayEntity delayEntity = new DelayEntity();
                    delayEntity.errMsg = throwable.toString();
                    return delayEntity;
                }).subscribe(delayEntity -> {
                    if (delayEntity.success) {
                        getView().dailyCompleteSuccess(delayEntity);
                    } else {
                        getView().dailyCompleteFailed(TextUtils.isEmpty(delayEntity.errMsg) ? "任务未完成" : delayEntity.errMsg);
                    }
                }));
    }
}
