package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.ContractListEntity;
import com.supcon.mes.middleware.model.contract.ContractQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
public class ContractPresenter extends ContractQueryContract.Presenter {

    @Override
    public void listStaff(String staffName, int pageNo) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.listContract(staffName, pageNo)
                        .onErrorReturn(new Function<Throwable, ContractListEntity>() {
                            @Override
                            public ContractListEntity apply(Throwable throwable) throws Exception {
                                ContractListEntity contractListEntity = new ContractListEntity();
                                contractListEntity.success = false;
                                contractListEntity.errMsg = throwable.toString();
                                return contractListEntity;
                            }
                        })
                        .subscribe(new Consumer<ContractListEntity>() {
                            @Override
                            public void accept(ContractListEntity contractListEntity) throws Exception {
                                if(contractListEntity.result != null && contractListEntity.result.size()!=0){
                                    getView().listStaffSuccess(contractListEntity);
                                }
                                else{
                                    getView().listStaffFailed(contractListEntity.errMsg);
                                }
                            }
                        }));
    }

}
