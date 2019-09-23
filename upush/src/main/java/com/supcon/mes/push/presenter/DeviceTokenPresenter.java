package com.supcon.mes.push.presenter;

import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.push.model.contract.DeviceTokenContract;
import com.supcon.mes.push.model.network.PushHttpClient;

import java.util.Objects;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2019/4/29
 * Email:wangshizhan@supcom.com
 */
public class DeviceTokenPresenter extends DeviceTokenContract.Presenter {

    private void sendDeviceToken(String deviceToken, String loginStatus) {
        mCompositeSubscription.add(PushHttpClient.sendDeviceToken(deviceToken, loginStatus, "android")
                .onErrorReturn(new Function<Throwable, CommonEntity>() {
                    @Override
                    public CommonEntity apply(Throwable throwable) throws Exception {
                        CommonEntity commonEntity = new CommonEntity();
                        commonEntity.success = false;
                        commonEntity.errMsg = throwable.toString();
                        return commonEntity;
                    }
                })
                .subscribe(new Consumer<CommonEntity>() {
                    @Override
                    public void accept(CommonEntity commonEntity) throws Exception {
                        if(commonEntity.success){
                            if("login".equals(loginStatus)){
                                Objects.requireNonNull(getView()).sendLoginDeviceTokenSuccess();
                            }
                            else{
                                Objects.requireNonNull(getView()).sendLogoutDeviceTokenSuccess();
                            }
                        }
                        else{
                            if("login".equals(loginStatus)){
                                Objects.requireNonNull(getView()).sendLoginDeviceTokenFailed(commonEntity.errMsg);
                            }
                            else{
                                Objects.requireNonNull(getView()).sendLogoutDeviceTokenFailed(commonEntity.errMsg);
                            }
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if("login".equals(loginStatus)){
                            Objects.requireNonNull(getView()).sendLoginDeviceTokenFailed(throwable.toString());
                        }
                        else{
                            Objects.requireNonNull(getView()).sendLogoutDeviceTokenFailed(throwable.toString());
                        }
                    }
                }));
    }

    @Override
    public void sendLoginDeviceToken(String deviceToken) {
        sendDeviceToken(deviceToken,"login");
    }

    @Override
    public void sendLogoutDeviceToken(String deviceToken) {
        sendDeviceToken(deviceToken,"logout");
    }
}
