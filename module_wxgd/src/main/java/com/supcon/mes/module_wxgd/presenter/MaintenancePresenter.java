package com.supcon.mes.module_wxgd.presenter;


import com.supcon.mes.module_wxgd.model.bean.MaintenanceListEntity;
import com.supcon.mes.module_wxgd.model.contract.MaintenanceContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
public class MaintenancePresenter extends MaintenanceContract.Presenter {

    @Override
    public void listMaintenance(long id) {
        mCompositeSubscription.add(
                HttpClient.listMaintenance(id)
                        .onErrorReturn(throwable -> {
                            MaintenanceListEntity maintenanceListEntity = new MaintenanceListEntity();
                            maintenanceListEntity.errMsg = throwable.toString();
                            return maintenanceListEntity;
                        })
                        .subscribe(maintenanceListEntity -> {
                            if (maintenanceListEntity.result != null) {
                                getView().listMaintenanceSuccess(maintenanceListEntity);
                            } else {
                                getView().listMaintenanceFailed(maintenanceListEntity.errMsg);
                            }
                        }));
    }
}
