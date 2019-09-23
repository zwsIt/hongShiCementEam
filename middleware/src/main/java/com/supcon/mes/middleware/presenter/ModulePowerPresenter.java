package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.contract.ModulePowerContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/19
 * ------------- Description -------------
 */
public class ModulePowerPresenter extends ModulePowerContract.Presenter {
    @Override
    public void getStartActivePowerCode(long deploymentId) {
        mCompositeSubscription.add(MiddlewareHttpClient.getStartActivePowerCode(deploymentId)
                .onErrorReturn(new Function<Throwable, BapResultEntity>() {
                    @Override
                    public BapResultEntity apply(Throwable throwable) throws Exception {
                        BapResultEntity bapResultEntity = new BapResultEntity();
                        return bapResultEntity;
                    }
                }).subscribe(new Consumer<BapResultEntity>() {
                    @Override
                    public void accept(BapResultEntity bapResultEntity) throws Exception {
                        if (bapResultEntity.dealSuccessFlag) {
                            if (getView() != null)
                                getView().getStartActivePowerCodeSuccess(bapResultEntity);
                        } else {
                            if (getView() != null)
                                getView().getStartActivePowerCodeFailed(bapResultEntity.errMsg);
                        }
                    }
                }));
    }
}
