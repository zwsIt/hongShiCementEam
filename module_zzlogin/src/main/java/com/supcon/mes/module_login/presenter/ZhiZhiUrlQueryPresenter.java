package com.supcon.mes.module_login.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.module_login.model.contract.ZhiZhiUrlQueryContract;
import com.supcon.mes.module_login.model.network.LoginHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ZhiZhiUrlQueryPresenter extends ZhiZhiUrlQueryContract.Presenter {
    @Override
    public void getZhizhiUrl() {
        mCompositeSubscription.add(
                LoginHttpClient.getZhizhiUrl()
                        .onErrorReturn(new Function<Throwable, BapResultEntity>() {
                            @Override
                            public BapResultEntity apply(Throwable throwable) throws Exception {
                                BapResultEntity bapResultEntity = new BapResultEntity();
                                bapResultEntity.dealSuccessFlag = false;
                                bapResultEntity.errMsg = throwable.toString();
                                return bapResultEntity;
                            }
                        })
                        .subscribe(new Consumer<BapResultEntity>() {
                            @Override
                            public void accept(BapResultEntity bapResultEntity) throws Exception {
                                if (bapResultEntity.dealSuccessFlag) {
                                    getView().getZhizhiUrlSuccess(bapResultEntity);
                                } else {
                                    getView().getZhizhiUrlFailed(bapResultEntity.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                getView().getZhizhiUrlFailed(throwable.toString());
                            }
                        }));
    }
}
