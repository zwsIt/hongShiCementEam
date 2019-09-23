package com.supcon.mes.module_login.presenter;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.module_login.model.bean.LoginEntity;
import com.supcon.mes.module_login.model.contract.SilentLoginContract;
import com.supcon.mes.module_login.model.network.LoginHttpClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangshizhan on 2018/9/6
 * Email:wangshizhan@supcom.com
 */
public class SilentLoginPresenter extends SilentLoginContract.Presenter {
    @Override
    public void dologin(String username, String pwd) {
        LogUtil.i("dologin username:" + username + " pwd:" + pwd);

        Map<String, Object> defaultMap = new HashMap<>();
        defaultMap.put("machineId", 11111111);
        defaultMap.put("clientType", "android");
        defaultMap.put("clientVersion", "2.1");
        defaultMap.put("timestamp", new Date().getTime());

//        String token1 = SharedPreferencesUtils.getParam(EamApplication.getAppContext(), Constant.ZZ.ZZ_SUPOS_TOKEN, "");
//        if(!TextUtils.isEmpty(token1)) {
//            defaultMap.put("suposToken", token1);
//        }

        mCompositeSubscription.add(

                LoginHttpClient.login(username, pwd, defaultMap)
                        .onErrorReturn(throwable -> {
                            LoginEntity loginEntity = new LoginEntity();
                            loginEntity.success = false;
                            loginEntity.errMsg = throwable.toString();
                            return loginEntity;
                        })
                        .subscribe(loginEntity -> {
                            LogUtil.w("SilentLoginPresenter", String.valueOf(loginEntity.success));
                            LogUtil.w("SilentLoginPresenter", String.valueOf(getView()));
                            if (loginEntity.success){
                                if (getView() != null)
                                    getView().dologinSuccess(loginEntity);
                            } else if (loginEntity.errMsg != null && loginEntity.errMsg.contains("401")) {
                                if (getView() != null){
                                    getView().dologinFailed("用户名或密码错误!");
                                }
                            } else {
                                if (getView() != null){
                                    getView().dologinFailed(loginEntity.errMsg);
                                }
                            }
                        })

        );
    }

    @Override
    public void dologinWithSuposPW(String username, String supospwd) {

        Map<String, Object> defaultMap = new HashMap<>();
        defaultMap.put("machineId", 11111111);
        defaultMap.put("clientType", "android");
        defaultMap.put("clientVersion", "2.1");
        defaultMap.put("timestamp", new Date().getTime());
        defaultMap.put("username", username);
        defaultMap.put("suposPassword", supospwd);

        mCompositeSubscription.add(

                LoginHttpClient.login(defaultMap)
                        .onErrorReturn(throwable -> {
                            LoginEntity loginEntity = new LoginEntity();
                            loginEntity.success = false;
                            loginEntity.errMsg = throwable.toString();
                            return loginEntity;
                        })
                        .subscribe(loginEntity -> {
                            LogUtil.w("SilentLoginPresenter", String.valueOf(loginEntity.success));
                            LogUtil.w("SilentLoginPresenter", String.valueOf(getView()));
                            if (loginEntity.success){
                                if (getView() != null)
                                    getView().dologinSuccess(loginEntity);
                            } else if (loginEntity.errMsg != null && loginEntity.errMsg.contains("401")) {
                                if (getView() != null){
                                    getView().dologinFailed("用户名或密码错误!");
                                }
                            } else {
                                if (getView() != null){
                                    getView().dologinFailed(loginEntity.errMsg);
                                }
                            }
                        })

        );
    }
}
