package com.supcon.mes.module_login.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.module_login.model.contract.PasswordContract;
import com.supcon.mes.module_login.model.network.LoginHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2019/8/6
 * Email:wangshizhan@supcom.com
 */
public class PasswordPresenter extends PasswordContract.Presenter {

    @Override
    public void checkPwd(String oldPassword) {
        mCompositeSubscription.add(
                LoginHttpClient.checkPwd(oldPassword)
                        .onErrorReturn(new Function<Throwable, BapResultEntity>() {
                            @Override
                            public BapResultEntity apply(Throwable throwable) throws Exception {
                                BapResultEntity bapResultEntity = new BapResultEntity();
                                bapResultEntity.dealSuccessFlag = false;
                                bapResultEntity.Message = throwable.toString();
                                return bapResultEntity;
                            }
                        })
                        .subscribe(new Consumer<BapResultEntity>() {
                            @Override
                            public void accept(BapResultEntity bapResultEntity) throws Exception {
                                if(bapResultEntity.dealSuccessFlag){
                                    getView().checkPwdSuccess(bapResultEntity);
                                }
                                else{
                                    getView().checkPwdFailed(bapResultEntity.Message);
                                }
                            }
                        }));
    }

    @Override
    public void saveNewPwd(String oldPassword, String newPassword) {

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("oldpassword", oldPassword);
        userInfo.put("newpassword", newPassword);

        mCompositeSubscription.add(
                LoginHttpClient.saveUsersrtInfo(userInfo)
                        .onErrorReturn(new Function<Throwable, BapResultEntity>() {
                            @Override
                            public BapResultEntity apply(Throwable throwable) throws Exception {
                                BapResultEntity bapResultEntity = new BapResultEntity();
                                bapResultEntity.dealSuccessFlag = false;
                                bapResultEntity.Message = throwable.toString();
                                return bapResultEntity;
                            }
                        })
                        .subscribe(new Consumer<BapResultEntity>() {
                            @Override
                            public void accept(BapResultEntity bapResultEntity) throws Exception {
                                if(bapResultEntity.dealSuccessFlag){
                                    getView().saveNewPwdSuccess(bapResultEntity);
                                }
                                else{
                                    getView().saveNewPwdFailed(bapResultEntity.Message);
                                }
                            }
                        }));
    }
}
