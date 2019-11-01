package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.contract.PcQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/6/24
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class PcQueryPresenter extends PcQueryContract.Presenter {
    @Override
    public void queryPc(String operateCode, String flowKey) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getPc(operateCode,flowKey)
                .onErrorReturn(new Function<Throwable, CommonEntity>() {
                    @Override
                    public CommonEntity apply(Throwable throwable) throws Exception {
                        CommonEntity commonEntity = new CommonEntity();
                        commonEntity.errMsg = throwable.toString();
                        return commonEntity;
                    }
                })
                .subscribe(new Consumer<CommonEntity>() {
                    @Override
                    public void accept(CommonEntity commonEntity) throws Exception {
                        if (commonEntity.success){
                            getView().queryPcSuccess(commonEntity);
                        }else {
                            getView().queryPcFailed(commonEntity.errMsg);
                        }
                    }
                })
        );
    }
}
