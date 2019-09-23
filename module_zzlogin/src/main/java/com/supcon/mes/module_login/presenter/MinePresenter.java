package com.supcon.mes.module_login.presenter;

import com.supcon.common.view.App;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.module_login.model.contract.MineContract;
import com.supcon.mes.module_login.model.network.LoginHttpClient;
import com.supcon.mes.module_login.service.HeartBeatService;
import com.supcon.mes.push.event.DeviceTokenEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wangshizhan on 2017/8/7.
 */

public class MinePresenter extends MineContract.Presenter {


    @Override
    public void logout() {
        LogUtil.i("dologout");

        EventBus.getDefault().post(new DeviceTokenEvent(SharedPreferencesUtils.getParam(EamApplication.getAppContext(), Constant.SPKey.DEVICE_TOKEN, ""), false));
        mCompositeSubscription.add(
                LoginHttpClient.logout()
                        .onErrorReturn(throwable -> {
                            ResultEntity entity = new ResultEntity();
                            entity.success = false;
                            entity.errMsg = throwable.toString();
                            return entity;
                        })
                        .subscribe(resultEntity -> {
                        LogUtil.i("onNext resultEntity:" + resultEntity);

                        if(resultEntity.success){
    //                        EventBus.getDefault().post(new DeviceTokenEvent(SharedPreferencesUtils.getParam(EamApplication.getAppContext(), Constant.SPKey.DEVICE_TOKEN, ""), false));

                            getView().logoutSuccess();
                            SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.LOGIN, false);
                            SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.JSESSIONID, "");
                            SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.CASTGC, "");
                        }
                        else{


                            getView().logoutSuccess();
                            SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.LOGIN, false);
                            SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.JSESSIONID, "");
                            SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.CASTGC, "");
    //                        getView().logoutFailed(resultEntity.errMsg);
                        }

                        MBapApp.setIsLogin(false);
                        MBapApp.setPassword("");
                        //用户信息清空
                        EamApplication.setAccountInfo(null);
                        HeartBeatService.stopLoginLoop(App.getAppContext());
                        DeviceManager.getInstance().release();
                })

        );


    }

}
