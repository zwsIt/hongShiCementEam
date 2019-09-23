package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.AreaListEntity;
import com.supcon.mes.middleware.model.contract.AreaQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
public class AreaPresenter extends AreaQueryContract.Presenter {

    @Override
    public void listArea() {
        mCompositeSubscription.add(
                MiddlewareHttpClient.listArea()
                        .onErrorReturn(new Function<Throwable, AreaListEntity>() {
                            @Override
                            public AreaListEntity apply(Throwable throwable) throws Exception {
                                AreaListEntity areaListEntity = new AreaListEntity();
                                areaListEntity.success = false;
                                areaListEntity.errMsg = throwable.toString();
                                return areaListEntity;
                            }
                        })
                        .subscribe(new Consumer<AreaListEntity>() {
                            @Override
                            public void accept(AreaListEntity areaListEntity) throws Exception {
                                if(areaListEntity.result != null && areaListEntity.result.size()!=0){
                                    getView().listAreaSuccess(areaListEntity);
                                }
                                else{
                                    getView().listAreaFailed(areaListEntity.errMsg);
                                }
                            }
                        }));
    }

}
