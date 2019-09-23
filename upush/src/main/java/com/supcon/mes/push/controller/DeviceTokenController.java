package com.supcon.mes.push.controller;

import android.content.Context;
import android.text.TextUtils;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseDataController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.push.event.DeviceTokenEvent;
import com.supcon.mes.push.model.api.DeviceTokenAPI;
import com.supcon.mes.push.model.contract.DeviceTokenContract;
import com.supcon.mes.push.presenter.DeviceTokenPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by wangshizhan on 2019/4/29
 * Email:wangshizhan@supcom.com
 */

@Presenter(DeviceTokenPresenter.class)
public class DeviceTokenController extends BaseDataController implements DeviceTokenContract.View {

    private String mDeviceToken = "";

    public DeviceTokenController(Context context) {
        super(context);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceToken(DeviceTokenEvent event) {

        LogUtil.e("DeviceTokenEvent:"+event.toString());

        if(event.isLogin()) {
            sendLoginDeviceToken(event.getDeviceToken());
        }
        else{
            sendLogoutDeviceToken(event.getDeviceToken());
        }
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public void initData() {
        super.initData();
        mDeviceToken = SharedPreferencesUtils.getParam(context, Constant.SPKey.DEVICE_TOKEN, "");

    }

    public String getDeviceToken() {
        return mDeviceToken;
    }

    public void cleanDeviceToken(){
        if(!TextUtils.isEmpty(mDeviceToken)){
            presenterRouter.create(DeviceTokenAPI.class).sendLogoutDeviceToken(mDeviceToken);
        }
    }

    public void sendLoginDeviceToken(String deviceToken){
        if(TextUtils.isEmpty(deviceToken)){
            return;
        }
        this.mDeviceToken = deviceToken;


        presenterRouter.create(DeviceTokenAPI.class).sendLoginDeviceToken(deviceToken);
    }

    public void sendLogoutDeviceToken(String deviceToken){
        if(TextUtils.isEmpty(deviceToken)){
            return;
        }
        presenterRouter.create(DeviceTokenAPI.class).sendLogoutDeviceToken(deviceToken);
    }


    @Override
    public void sendLoginDeviceTokenSuccess() {
        LogUtil.e("DEVICE_TOKEN发送成功:"+mDeviceToken);
//        ToastUtils.show(context, "DEVICE_TOKEN发送成功");
    }

    @Override
    public void sendLoginDeviceTokenFailed(String errorMsg) {
        LogUtil.e(""+errorMsg);
    }

    @Override
    public void sendLogoutDeviceTokenSuccess() {
        LogUtil.e("DEVICE_TOKEN清除成功:"+mDeviceToken);
        //do nothing
//        SharedPreferencesUtils.setParam(context, Constant.SPKey.DEVICE_TOKEN, "");
//        ToastUtils.show(context, "DEVICE_TOKEN清除成功");
    }

    @Override
    public void sendLogoutDeviceTokenFailed(String errorMsg) {
        LogUtil.e(""+errorMsg);
    }
}
