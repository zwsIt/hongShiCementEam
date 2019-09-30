package com.supcon.mes.module_main.presenter;

import com.supcon.mes.module_main.model.contract.DealInfoContract;
import com.supcon.mes.module_main.model.network.MainClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class DealInfoPresenter extends DealInfoContract.Presenter {
    @Override
    public void listDealInfo(String moduleName, String tableName, Long tableInfoId) {
        mCompositeSubscription.add(
                MainClient.getDealInfoList(moduleName,tableName,tableInfoId)
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
