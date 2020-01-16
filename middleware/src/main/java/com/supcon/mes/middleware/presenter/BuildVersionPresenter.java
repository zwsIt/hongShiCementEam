package com.supcon.mes.middleware.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BuildVersionEntity;
import com.supcon.mes.middleware.model.contract.BuildVersionContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2019/12/26
 * Email:wangshizhan@supcom.com
 */
public class BuildVersionPresenter extends BuildVersionContract.Presenter {
    @Override
    public void getLatestBuildVersion(String appId) {

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("api_token", Constant.Fir.API_TOKEN);
        queryMap.put("type", "android");

        mCompositeSubscription.add(
                MiddlewareHttpClient.findLatestVersion(appId, queryMap)
                        .onErrorReturn(new Function<Throwable, BuildVersionEntity>() {
                            @Override
                            public BuildVersionEntity apply(Throwable throwable) throws Exception {
                                BuildVersionEntity buildVersionEntity = new BuildVersionEntity();
                                buildVersionEntity.msg = throwable.toString();
                                return buildVersionEntity;
                            }
                        })
                        .subscribe(new Consumer<BuildVersionEntity>() {
                            @Override
                            public void accept(BuildVersionEntity buildVersionEntity) throws Exception {
                                if(TextUtils.isEmpty(buildVersionEntity.msg)){
                                    getView().getLatestBuildVersionSuccess(buildVersionEntity);
                                }
                                else{
                                    getView().getLatestBuildVersionFailed(buildVersionEntity.msg);
                                }
                            }
                        }));
    }
}
