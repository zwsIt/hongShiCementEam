package com.supcon.mes.module_sbda_online.presenter;

import android.text.TextUtils;

import com.supcon.mes.module_sbda_online.model.bean.ParamListEntity;
import com.supcon.mes.module_sbda_online.model.contract.ParamContract;
import com.supcon.mes.module_sbda_online.model.network.SBDAOnlineHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class ParamPresenter extends ParamContract.Presenter {
    @Override
    public void getEamParam(Long beamID) {
        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("baseInfo.id", beamID);
        mCompositeSubscription.add(SBDAOnlineHttpClient.getEamParam(pageQueryParams)
                .onErrorReturn(throwable -> {
                    ParamListEntity paramListEntity = new ParamListEntity();
                    paramListEntity.errMsg = throwable.toString();
                    return paramListEntity;
                }).subscribe(paramListEntity -> {
                    if (TextUtils.isEmpty(paramListEntity.errMsg)) {
                        getView().getEamParamSuccess(paramListEntity);
                    } else {
                        getView().getEamParamFailed(paramListEntity.errMsg);
                    }
                }));
    }
}
