package com.supcon.mes.module_yhgl.presenter;

import com.supcon.mes.module_yhgl.model.bean.LubricateOilsListEntity;
import com.supcon.mes.module_yhgl.model.bean.MaintenanceListEntity;
import com.supcon.mes.module_yhgl.model.contract.LubricateOilsContract;
import com.supcon.mes.module_yhgl.model.contract.MaintenanceContract;
import com.supcon.mes.module_yhgl.model.network.YHGLHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
public class MaintenancePresenter extends MaintenanceContract.Presenter {

    @Override
    public void listMaintenance(long id) {
        mCompositeSubscription.add(
                YHGLHttpClient.listMaintenance(id)
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
