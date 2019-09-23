package com.supcon.mes.module_login.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.view.App;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.bean.ModuleAuthorizationListEntity;
import com.supcon.mes.middleware.model.bean.MyInfo;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.module_login.model.bean.LoginEntity;
import com.supcon.mes.module_login.model.contract.LoginContract;
import com.supcon.mes.module_login.model.network.LoginHttpClient;
import com.supcon.mes.push.event.DeviceTokenEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangshizhan on 2017/8/7.
 */

public class LoginPresenter extends LoginContract.Presenter {


    @Override
    public void dologin(String username, String pwd) {
        Map<String, Object> defaultMap = new HashMap<>();
        defaultMap.put("machineId",11111111);
        defaultMap.put("clientType","android");
        defaultMap.put("clientVersion","2.1");
        defaultMap.put("timestamp",new Date().getTime());

        mCompositeSubscription.add(
                LoginHttpClient.login(username, pwd, defaultMap)
                        .onErrorReturn(throwable -> {
                            LoginEntity loginEntity = new LoginEntity();
                            loginEntity.success = false;
                            loginEntity.errMsg = throwable.toString();
                            return loginEntity;
                        })
                        .subscribe(loginEntity -> {
                            LogUtil.i( "onNext userEntity:"+loginEntity);
                            if(loginEntity.success) {
                                getView().dologinSuccess(loginEntity);
                            }
                            else if(loginEntity.errMsg != null && loginEntity.errMsg.contains("401")){
                                getView().dologinFailed("用户名或密码错误!");
                            } else {
                                getView().dologinFailed(loginEntity.errMsg);
                            }
                        })

        );
    }

    @Override
    public void dologinWithToken(String username, String pwd, String token1) {
        Map<String, Object> defaultMap = new HashMap<>();
        defaultMap.put("machineId",11111111);
        defaultMap.put("clientType","android");
        defaultMap.put("clientVersion","2.1");
        defaultMap.put("suposToken",token1);
        defaultMap.put("timestamp",new Date().getTime());

        mCompositeSubscription.add(
                LoginHttpClient.login(username, pwd, defaultMap)
                        .onErrorReturn(throwable -> {
                            LoginEntity loginEntity = new LoginEntity();
                            loginEntity.success = false;
                            loginEntity.errMsg = throwable.toString();
                            return loginEntity;
                        })
                        .subscribe(loginEntity -> {
                            LogUtil.i( "onNext userEntity:"+loginEntity);
                            if(loginEntity.success) {
                                getView().dologinSuccess(loginEntity);
                            }
                            else if(loginEntity.errMsg != null && loginEntity.errMsg.contains("401")){
                                getView().dologinFailed("用户名或密码错误!");
                            } else {
                                getView().dologinFailed(loginEntity.errMsg);
                            }
                        })

        );
    }

    @Override
    public void dologinWithSuposPW(String username, String supospwd) {

        Map<String, Object> defaultMap = new HashMap<>();
        defaultMap.put("machineId",11111111);
        defaultMap.put("clientType","android");
        defaultMap.put("clientVersion","2.1");
        defaultMap.put("timestamp",new Date().getTime());

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
                            LogUtil.i( "onNext userEntity:"+loginEntity);
                            if(loginEntity.success) {
                                getView().dologinSuccess(loginEntity);
                            }
                            else if(loginEntity.errMsg != null && loginEntity.errMsg.contains("401")){
                                getView().dologinFailed("用户名或密码错误!");
                            } else {
                                getView().dologinFailed(loginEntity.errMsg);
                            }
                        })

        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getLicenseInfo(String moduleCodes) {

//        Flowable.timer(500, TimeUnit.MILLISECONDS)
//                .compose(RxSchedulers.io_main())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        getView().getLicenseInfoSuccess(new LicenseEntity());
//                    }
//                });

        mCompositeSubscription.add(LoginHttpClient.getLicenseInfos(moduleCodes)
                .onErrorReturn(throwable -> {
                    ModuleAuthorizationListEntity moduleAuthorizationListEntity = new ModuleAuthorizationListEntity();
                    moduleAuthorizationListEntity.errMsg = throwable.toString();
                    moduleAuthorizationListEntity.success = false;
                    moduleAuthorizationListEntity.result = null;
                    return moduleAuthorizationListEntity;

                })
                .subscribe(moduleAuthorizationListEntity -> {
                    if(moduleAuthorizationListEntity.success){
                        getView().getLicenseInfoSuccess(moduleAuthorizationListEntity);
                    }
                    else{
                        getView().getLicenseInfoFailed(moduleAuthorizationListEntity.errMsg);
                    }
                })
        );
        /*mCompositeSubscription.add(LoginHttpClient.getLicenseInfo()
                .onErrorReturn(throwable -> {
                    LicenseEntity licenseEntity = new LicenseEntity();
                    licenseEntity.errMsg = throwable.toString();
                    licenseEntity.success = false;
                    licenseEntity.result = false;
                    return licenseEntity;
                })
                .subscribe(licenseEntity -> {
                    if(licenseEntity.result){
                        getView().getLicenseInfoSuccess(licenseEntity);
                    }
                    else{
                        getView().getLicenseInfoFailed(licenseEntity.errMsg);
                    }
                })
        );*/
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAccountInfo(String username) {
        mCompositeSubscription.add(MiddlewareHttpClient.getCurrentUserinfo()
                .onErrorReturn(throwable -> {
                    MyInfo myInfo = new MyInfo();
                    myInfo.errMsg = throwable.toString();
                    myInfo.success = false;
                    return myInfo;
                })
                .subscribe(myInfo -> {
                    if (myInfo.success) {
                        AccountInfo accountInfo = myInfo.result;
                        Staff staff = new Staff();
                        staff.id = accountInfo.staffId;
                        staff.code = accountInfo.staffCode;
                        staff.name = accountInfo.staffName;
                        EamApplication.setStaff(staff);
                        accountInfo.ip = EamApplication.getIp();
                        accountInfo.setPassword(SharedPreferencesUtils.getParam(MBapApp.getAppContext(), MBapConstant.SPKey.PWD, ""));
                        accountInfo.setIp(SharedPreferencesUtils.getParam(MBapApp.getAppContext(), MBapConstant.SPKey.IP, ""));
                        accountInfo.setDate(DateUtil.dateTimeFormat(System.currentTimeMillis()));
                        EamApplication.dao().getAccountInfoDao().insertOrReplace(accountInfo);
                        EamApplication.setAccountInfo(accountInfo);

                        //初始化DeviceManager
                        DeviceManager.getInstance().init();

//                        doZhiZhiLogin();

                        getView().getAccountInfoSuccess();
                        EventBus.getDefault().post(new DeviceTokenEvent(SharedPreferencesUtils.getParam(App.getAppContext(), Constant.SPKey.DEVICE_TOKEN, ""), true));

                    } else {
                        getView().getLicenseInfoFailed(myInfo.errMsg);

                    }

                })
        );

    }

/*    private void doZhiZhiLogin() {
        String ip = SharedPreferencesUtils.getParam(ZZApp.getAppContext(), Constant.ZZ.IP, "");
        String port = SharedPreferencesUtils.getParam(ZZApp.getAppContext(), Constant.ZZ.PORT, "");
        String userName = ZZApp.getUserName();
        String pwd = ZZApp.getPassword();
        LogUtil.d("zhizhi login ip:"+ip+" port:"+port+" userName:"+userName+" pwd:"+pwd);
        LoginUserSDK.getInstance().userLogin(userName, pwd, "Http://"+ip+":"+port+"/");
    }*/


/*    @SuppressLint("CheckResult")
    @Override
    public void getAccountInfo(String username) {
        mCompositeSubscription.add(MiddlewareHttpClient.queryUserInfo(username)
                .onErrorReturn(throwable -> {
                    UserInfoListEntity userInfoListEntity = new UserInfoListEntity();
                    userInfoListEntity.errMsg = throwable.toString();
                    userInfoListEntity.success = false;
                    return userInfoListEntity;
                })
                .subscribe(userInfoListEntity -> {
                    if (userInfoListEntity.result != null && userInfoListEntity.result.size() != 0) {

                        Flowable.just(AccountInfoHelper.convertStaffInfo(userInfoListEntity.result.get(0)))
                                .compose(RxSchedulers.io_main())
                                .subscribe(accountInfo -> {
                                    accountInfo.ip = EamApplication.getIp();
                                    accountInfo.setPassword(SharedPreferencesUtils.getParam(MBapApp.getAppContext(), MBapConstant.SPKey.PWD, ""));
                                    accountInfo.setIp(SharedPreferencesUtils.getParam(MBapApp.getAppContext(), MBapConstant.SPKey.IP, ""));
                                    accountInfo.setDate(DateUtil.dateTimeFormat(System.currentTimeMillis()));
                                    EamApplication.dao().getAccountInfoDao().insertOrReplace(accountInfo);
                                    EamApplication.setAccountInfo(accountInfo);
                                    //初始化DeviceManager
                                    DeviceManager.getInstance().init();
                                    getView().getAccountInfoSuccess();
                                });

                    } else {
                        getView().getLicenseInfoFailed(userInfoListEntity.errMsg);

                    }

                })
        );

    }*/
}
