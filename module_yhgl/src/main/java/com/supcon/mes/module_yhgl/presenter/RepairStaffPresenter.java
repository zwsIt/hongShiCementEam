package com.supcon.mes.module_yhgl.presenter;


import com.supcon.mes.module_yhgl.model.bean.RepairStaffListEntity;
import com.supcon.mes.module_yhgl.model.contract.RepairStaffContract;
import com.supcon.mes.module_yhgl.model.network.YHGLHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
public class RepairStaffPresenter extends RepairStaffContract.Presenter {

    @Override
    public void listRepairStaffList(long id) {
        mCompositeSubscription.add(
                YHGLHttpClient.listRepairStaffs(id)
                        .onErrorReturn(throwable -> {
                            RepairStaffListEntity repairStaffListEntity = new RepairStaffListEntity();
                            repairStaffListEntity.errMsg = throwable.toString();
                            return repairStaffListEntity;
                        })
                        .subscribe(repairStaffListEntity -> {
                            if(repairStaffListEntity.result != null){
                                getView().listRepairStaffListSuccess(repairStaffListEntity);
                            }
                            else{
                                getView().listRepairStaffListFailed(repairStaffListEntity.errMsg);
                            }
                        }));
    }
}
