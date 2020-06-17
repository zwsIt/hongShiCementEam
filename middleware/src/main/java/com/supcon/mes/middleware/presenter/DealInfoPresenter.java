package com.supcon.mes.middleware.presenter;


import com.supcon.mes.middleware.model.contract.DealInfoContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class DealInfoPresenter extends DealInfoContract.Presenter {
    @Override
    public void listDealInfo(String url, Long tableInfoId) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getDealInfoList(url,tableInfoId)
                .onErrorReturn(new Function<Throwable, List>() {
                    @Override
                    public List apply(Throwable throwable) throws Exception {
                        return new ArrayList();
                    }
                })
                .subscribe(new Consumer<List>() {
                    @Override
                    public void accept(List list) throws Exception {
                        if (list != null){
                            getView().listDealInfoSuccess(list);
                        }else {
                            getView().listDealInfoFailed("获取单据处理意见失败");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().listDealInfoFailed(throwable.toString());
                    }
                })
        );
    }
}
