package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.module_wxgd.model.bean.RepairStaffListEntity;
import com.supcon.mes.module_wxgd.model.contract.RepairStaffContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

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
                HttpClient.listRepairStaffs(id)
                        .onErrorReturn(new Function<Throwable, RepairStaffListEntity>() {
                            @Override
                            public RepairStaffListEntity apply(Throwable throwable) throws Exception {
                                RepairStaffListEntity repairStaffListEntity = new RepairStaffListEntity();
                                repairStaffListEntity.errMsg = throwable.toString();
                                return repairStaffListEntity;
                            }
                        })
                        .subscribe(new Consumer<RepairStaffListEntity>() {
                            @Override
                            public void accept(RepairStaffListEntity repairStaffListEntity) throws Exception {
                                if(repairStaffListEntity.result != null){
                                    getView().listRepairStaffListSuccess(repairStaffListEntity);
                                }
                                else{
                                    getView().listRepairStaffListFailed(repairStaffListEntity.errMsg);
                                }
                            }
                        }));
    }
}
