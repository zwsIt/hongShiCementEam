package com.supcon.mes.middleware.presenter;


import android.text.TextUtils;

import com.supcon.mes.middleware.model.contract.TableInfoContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class TableInfoPresenter extends TableInfoContract.Presenter {
    @Override
    public void getTableInfo(String url, Long id, String includes) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("includes", includes);
        mCompositeSubscription.add(
                MiddlewareHttpClient.get(url, id, queryMap)
                        .onErrorReturn(throwable -> throwable.toString())
                        .subscribe(result -> {
                            if (result != null && !TextUtils.isEmpty(result.toString()) && !result.toString().contains("Exception")){
                                getView().getTableInfoSuccess(result);
                            } else {
                                getView().getTableInfoFailed(String.valueOf(result));
                            }
                        })
        );
    }
}
