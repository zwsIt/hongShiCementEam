package com.supcon.mes.module_login.presenter;

import android.text.TextUtils;

import com.supcon.mes.module_login.model.bean.PendingNumEntity;
import com.supcon.mes.module_login.model.contract.PendingNumContract;
import com.supcon.mes.module_login.model.network.LoginHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/12/7
 * Email:wangshizhan@supcom.com
 */

public class PendingNumPresenter extends PendingNumContract.Presenter {
    @Override
    public void queryPendingNum(long userId) {
        mCompositeSubscription.add(
                LoginHttpClient.getPendingNum(userId)
                        .onErrorReturn(new Function<Throwable, PendingNumEntity>() {
                            @Override
                            public PendingNumEntity apply(Throwable throwable) throws Exception {
                                PendingNumEntity pendingNumEntity = new PendingNumEntity();
                                pendingNumEntity.errMsg = throwable.toString();
                                return pendingNumEntity;
                            }
                        })
                        .subscribe(new Consumer<PendingNumEntity>() {
                            @Override
                            public void accept(PendingNumEntity pendingNumEntity) throws Exception {
                                if(TextUtils.isEmpty(pendingNumEntity.errMsg)){
                                    getView().queryPendingNumSuccess(pendingNumEntity);
                                }
                                else{
                                    getView().queryPendingNumFailed(pendingNumEntity.errMsg);
                                }
                            }
                        }));
    }
}
