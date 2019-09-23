package com.supcon.mes.module_login.presenter;


import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.module_login.model.bean.WorkEntity;
import com.supcon.mes.module_login.model.bean.WorkNumEntity;
import com.supcon.mes.module_login.model.contract.WorkContract;
import com.supcon.mes.module_login.model.network.LoginHttpClient;

import org.reactivestreams.Publisher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2017/8/15.
 */
public class WorkPresenter extends WorkContract.Presenter {

    private String url;

    @Override
    public void getAllPendings(String staffName) {
//        getView().getPendingsFailed("not work");

        mCompositeSubscription.add(
                LoginHttpClient.getAllPendings()
                        .onErrorReturn(new Function<Throwable, String>() {
                            @Override
                            public String apply(Throwable throwable) throws Exception {
                                return "http error";
                            }
                        })
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                LogUtil.d("s:" + s);

                            }
                        })
        );


        getView().getAllPendingsSuccess(new WorkEntity());
    }

    @SuppressLint("CheckResult")
    @Override
    public void getPendingsByModule(List<String> pendingParams) {
        for (String url : pendingParams) {
            Map<String, Integer> map = new HashMap();
            String[] split = url.split(",");
            mCompositeSubscription.add(Flowable.fromIterable(Arrays.asList(split))
                    .flatMap((Function<String, Publisher<WorkNumEntity>>) s -> LoginHttpClient.getPendingsByModule(s))
                    .onErrorReturn(throwable -> {
                        WorkNumEntity entity = new WorkNumEntity();
                        entity.success = false;
                        entity.errMsg = throwable.toString();
                        return entity;
                    })
                    .subscribe(workNumEntity -> {
                        if (TextUtils.isEmpty(workNumEntity.errMsg)) {
                            int count;
                            if (map.containsKey(url)) {
                                count = map.get(url) + workNumEntity.totalCount;
                            } else {
                                count = workNumEntity.totalCount;
                            }
                            map.put(url, count);
                        }
                    }, throwable -> {
                    }, () -> {
                        getView().getPendingsByModuleSuccess(map);
                    }));
        }

    }

}
