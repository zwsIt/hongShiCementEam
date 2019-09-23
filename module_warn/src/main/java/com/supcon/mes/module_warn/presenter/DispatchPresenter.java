package com.supcon.mes.module_warn.presenter;

import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.contract.DispatchContract;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2019/5/28
 * Email:wangshizhan@supcom.com
 */
public class DispatchPresenter extends DispatchContract.Presenter {
    @Override
    public void generateWork(Map<String, Object> param) {
        mCompositeSubscription.add(
                EarlyWarnHttpClient.generateWork(param)
                        .onErrorReturn(new Function<Throwable, DelayEntity>() {
                            @Override
                            public DelayEntity apply(Throwable throwable) throws Exception {
                                DelayEntity delayEntity = new DelayEntity();
                                delayEntity.success = false;
                                delayEntity.errMsg = throwable.toString();
                                return delayEntity;
                            }
                        })
                        .subscribe(new Consumer<DelayEntity>() {
                            @Override
                            public void accept(DelayEntity delayEntity) throws Exception {
                                if(delayEntity.success){
                                    getView().generateWorkSuccess(delayEntity);
                                }
                                else{
                                    getView().generateWorkFailed(delayEntity.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                getView().generateWorkFailed(throwable.toString());
                            }
                        }));
    }
}
