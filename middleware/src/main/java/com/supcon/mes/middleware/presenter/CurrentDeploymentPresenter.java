package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.CurrentDeploymentEntity;
import com.supcon.mes.middleware.model.contract.CurrentDeploymentContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/9
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class CurrentDeploymentPresenter extends CurrentDeploymentContract.Presenter {
    @Override
    public void getCurrentDeployment(String processKey) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getCurrentDeployment(processKey)
                .onErrorReturn(throwable -> {
                    CurrentDeploymentEntity currentDeploymentEntity = new CurrentDeploymentEntity();
                    currentDeploymentEntity.errMsg = throwable.toString();
                    return currentDeploymentEntity;
                })
                .subscribe(currentDeploymentEntity -> {
                    if (currentDeploymentEntity.dealSuccessFlag){
                        getView().getCurrentDeploymentSuccess(currentDeploymentEntity);
                    }else {
                        getView().getCurrentDeploymentFailed(currentDeploymentEntity.errMsg);
                    }
                })
        );
    }
}
