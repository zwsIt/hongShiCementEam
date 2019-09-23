package com.supcon.mes.module_sbda_online.presenter;

import android.text.TextUtils;

import com.supcon.mes.module_sbda_online.model.bean.SparePartListEntity;
import com.supcon.mes.module_sbda_online.model.contract.SpareContract;
import com.supcon.mes.module_sbda_online.model.network.SBDAOnlineHttpClient;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class SparePresenter extends SpareContract.Presenter {
    @Override
    public void spareRecord(Long eamID) {
        mCompositeSubscription.add(SBDAOnlineHttpClient.spareRecord(eamID)
                .onErrorReturn(throwable -> {
                    SparePartListEntity sparePartListEntity = new SparePartListEntity();
                    sparePartListEntity.errMsg = throwable.toString();
                    return sparePartListEntity;
                }).subscribe(sparePartListEntity -> {
                    if (TextUtils.isEmpty(sparePartListEntity.errMsg)) {
                        getView().spareRecordSuccess(sparePartListEntity);
                    } else {
                        getView().spareRecordFailed(sparePartListEntity.errMsg);
                    }
                }));
    }
}
