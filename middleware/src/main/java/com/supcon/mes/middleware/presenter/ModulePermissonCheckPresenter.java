package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.LongResultEntity;
import com.supcon.mes.middleware.model.contract.ModulePermissonCheckContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/9/21
 * Email:wangshizhan@supcom.com
 */
public class ModulePermissonCheckPresenter extends ModulePermissonCheckContract.Presenter {
    @Override
    public void checkModulePermission(String userName, String proccessKey) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.checkModulePermission(userName, proccessKey)
                        .onErrorReturn(new Function<Throwable, LongResultEntity>() {
                            @Override
                            public LongResultEntity apply(Throwable throwable) throws Exception {
                                LongResultEntity resultEntity = new LongResultEntity();
                                resultEntity.errMsg = throwable.toString();
                                resultEntity.success = false;
                                return resultEntity;
                            }
                        })
                        .subscribe(new Consumer<LongResultEntity>() {
                            @Override
                            public void accept(LongResultEntity longResultEntity) throws Exception {
                                if(longResultEntity.success){
                                    if(getView() != null)
                                    getView().checkModulePermissionSuccess(longResultEntity);
                                }
                                else{
                                    if(getView() != null)
                                    getView().checkModulePermissionFailed(longResultEntity.errMsg);
                                }
                            }
                        }));
    }
}
