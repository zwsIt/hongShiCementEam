package com.supcon.mes.module_login.presenter;

import android.util.Log;

import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.module_login.model.bean.HeartBeatEntity;
import com.supcon.mes.module_login.model.contract.HeartBeatContract;
import com.supcon.mes.module_login.model.network.LoginHttpClient;


/**
 * Created by wangshizhan on 2017/8/7.
 */

public class AutoLoginPresenter extends HeartBeatContract.Presenter {


    @Override
    public void heartBeat() {
        Log.i("AutoLoginPresenter","heartBeat:"+ DateUtil.dateFormat(System.currentTimeMillis()));
        mCompositeSubscription.add(
                LoginHttpClient.heartbeat()
                        .onErrorReturn(throwable -> {
                            HeartBeatEntity heartBeatEntity = new HeartBeatEntity();
                            heartBeatEntity.mobileAction = false;
                            heartBeatEntity.errMsg = throwable.toString();
                            return heartBeatEntity;
                        })
                        .subscribe(heartBeatEntity -> {
                            if(heartBeatEntity != null && heartBeatEntity.mobileAction)
                                getView().heartBeatSuccess();
                            else{
                                getView().heartBeatFailed("心跳失败!"+heartBeatEntity.errMsg);
                            }
                        }, throwable -> {
                            if(getView()==null){
                                return;
                            }
                            getView().heartBeatFailed(throwable.getMessage());
                        })

        );

    }

}
