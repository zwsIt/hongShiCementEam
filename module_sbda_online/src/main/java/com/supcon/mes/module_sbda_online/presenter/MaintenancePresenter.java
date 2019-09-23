package com.supcon.mes.module_sbda_online.presenter;

import android.text.TextUtils;

import com.supcon.mes.module_sbda_online.model.bean.MaintenanceListEntity;
import com.supcon.mes.module_sbda_online.model.contract.MaintenanceContract;
import com.supcon.mes.module_sbda_online.model.network.SBDAOnlineHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class MaintenancePresenter extends MaintenanceContract.Presenter {
    @Override
    public void maintenanceRecord(Long beamID) {
        mCompositeSubscription.add(SBDAOnlineHttpClient.maintenanceRecord(beamID)
                .onErrorReturn(throwable -> {
                    MaintenanceListEntity maintenanceListEntity = new MaintenanceListEntity();
                    maintenanceListEntity.errMsg = throwable.toString();
                    return maintenanceListEntity;
                }).subscribe(maintenanceListEntity -> {
                    if (TextUtils.isEmpty(maintenanceListEntity.errMsg)) {
                        getView().maintenanceRecordSuccess(maintenanceListEntity);
                    } else {
                        getView().maintenanceRecordFailed(maintenanceListEntity.errMsg);
                    }
                }));
    }
}
