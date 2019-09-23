package com.supcon.mes.module_warn.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_warn.model.bean.DelayRecordListEntity;
import com.supcon.mes.module_warn.model.contract.DelayRecordContract;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import java.util.HashMap;
import java.util.Map;

public class DelayRecordPresenter extends DelayRecordContract.Presenter {
    @Override
    public void delayRecords(String url, Map<String, Object> params) {
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(params, "EAM_BaseInfo,EAM_ID,BEAM_DELAY_RECORDS,DELAY_EAM_ID");
        fastQuery.subconds.add(joinSubcondEntity);
        fastQuery.modelAlias = "delayRecords";
        mCompositeSubscription.add(EarlyWarnHttpClient.delayRecords(url, fastQuery, params)
                .onErrorReturn(throwable -> {
                    DelayRecordListEntity delayRecordListEntity = new DelayRecordListEntity();
                    delayRecordListEntity.errMsg = throwable.toString();
                    return delayRecordListEntity;
                }).subscribe(delayRecordListEntity -> {
                    if (TextUtils.isEmpty(delayRecordListEntity.errMsg)) {
                        getView().delayRecordsSuccess(delayRecordListEntity);
                    } else {
                        getView().delayRecordsFailed(delayRecordListEntity.errMsg);
                    }
                }));
    }
}
