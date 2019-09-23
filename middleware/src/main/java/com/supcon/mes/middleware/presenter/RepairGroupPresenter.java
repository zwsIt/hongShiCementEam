package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.RepairGroupListEntity;
import com.supcon.mes.middleware.model.contract.RepairGroupQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */

public class RepairGroupPresenter extends RepairGroupQueryContract.Presenter {
    @Override
    public void queryRepairGroup() {
        mCompositeSubscription.add(
                MiddlewareHttpClient.listWXGroup()
                        .onErrorReturn(new Function<Throwable, RepairGroupListEntity>() {
                            @Override
                            public RepairGroupListEntity apply(Throwable throwable) throws Exception {
                                RepairGroupListEntity repairGroupListEntity = new RepairGroupListEntity();
                                repairGroupListEntity.success = false;
                                repairGroupListEntity.errMsg = throwable.toString();
                                return repairGroupListEntity;
                            }
                        })
                        .subscribe(new Consumer<RepairGroupListEntity>() {
                            @Override
                            public void accept(RepairGroupListEntity repairGroupListEntity) throws Exception {
                                if(repairGroupListEntity.result!=null && repairGroupListEntity.result.size()!=0){
                                    getView().queryRepairGroupSuccess(repairGroupListEntity);
                                }
                                else{
                                    getView().queryRepairGroupFailed(repairGroupListEntity.errMsg);
                                }
                            }
                        }));
    }
}
