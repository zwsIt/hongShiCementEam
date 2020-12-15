package com.supcon.mes.module_login.presenter;

import android.util.Log;

import com.supcon.common.com_http.NullEntity;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_login.model.bean.HeartBeatEntity;
import com.supcon.mes.module_login.model.contract.HeartBeatContract;
import com.supcon.mes.module_login.model.network.LoginHttpClient;

import org.json.JSONObject;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * Created by wangshizhan on 2017/8/7.
 */

public class AutoLoginPresenter extends HeartBeatContract.Presenter {


    @Override
    public void heartBeat() {
        Log.d("-AutoLoginPresenter-","heartBeat:"+ DateUtil.dateFormat(System.currentTimeMillis()));
        mCompositeSubscription.add(
                LoginHttpClient.heartbeat()
                        .onErrorReturn(new Function<Throwable, JSONObject>() {
                            @Override
                            public JSONObject apply(Throwable throwable) throws Exception {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("msg",throwable.toString());
                                return jsonObject;
                            }
                        })
                        .subscribe(new Consumer<JSONObject>() {
                            @Override
                            public void accept(JSONObject jsonObject) throws Exception {
                                if (jsonObject.length() == 0){
                                    getView().heartBeatSuccess();
                                }else {
                                    getView().heartBeatFailed("心跳失败!"+jsonObject.get("msg"));
                                }
                            }
                        }, throwable -> {
                            if(getView()==null){
                                return;
                            }
                            getView().heartBeatFailed(throwable.getMessage());
                        }));

    }

}
