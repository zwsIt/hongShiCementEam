package com.supcon.mes.module_yhgl.presenter;

import com.supcon.mes.middleware.model.bean.AcceptanceCheckListEntity;
import com.supcon.mes.module_yhgl.model.contract.AcceptanceCheckContract;
import com.supcon.mes.module_yhgl.model.network.YHGLHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
public class AcceptanceCheckPresenter extends AcceptanceCheckContract.Presenter {
    @Override
    public void listAcceptanceCheckList(long id) {
        mCompositeSubscription.add(
                YHGLHttpClient.listAcceptances(id)
                        .onErrorReturn(new Function<Throwable, AcceptanceCheckListEntity>() {
                            @Override
                            public AcceptanceCheckListEntity apply(Throwable throwable) throws Exception {
                                AcceptanceCheckListEntity acceptanceCheckListEntity = new AcceptanceCheckListEntity();
                                acceptanceCheckListEntity.errMsg = throwable.toString();
                                return acceptanceCheckListEntity;
                            }
                        })
                        .subscribe(new Consumer<AcceptanceCheckListEntity>() {
                            @Override
                            public void accept(AcceptanceCheckListEntity acceptanceCheckListEntity) throws Exception {
                                if(acceptanceCheckListEntity.result != null){
                                    getView().listAcceptanceCheckListSuccess(acceptanceCheckListEntity);
                                }
                                else{
                                    getView().listAcceptanceCheckListFailed(acceptanceCheckListEntity.errMsg);
                                }
                            }
                        }));
    }
}
