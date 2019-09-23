package com.supcon.mes.module_acceptance.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.module_acceptance.model.contract.AcceptanceSubmitContract;
import com.supcon.mes.module_acceptance.model.network.AcceptanceHttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
public class AcceptanceSubmitPresenter extends AcceptanceSubmitContract.Presenter {
    @Override
    public void doSubmit(Map<String, Object> map, String powerCode) {
        Map<String, RequestBody> formBody = FormDataHelper.createDataFormBody(map);
        mCompositeSubscription.add(
                AcceptanceHttpClient.doSubmit(formBody, powerCode)
                        .onErrorReturn(new Function<Throwable, BapResultEntity>() {
                            @Override
                            public BapResultEntity apply(Throwable throwable) throws Exception {
                                BapResultEntity bapResultEntity = new BapResultEntity();
                                bapResultEntity.dealSuccessFlag = false;
                                bapResultEntity.errMsg = throwable.toString();
                                return bapResultEntity;
                            }
                        })
                        .subscribe(new Consumer<BapResultEntity>() {
                            @Override
                            public void accept(BapResultEntity bapResultEntity) throws Exception {
                                if (bapResultEntity.dealSuccessFlag) {
                                    getView().doSubmitSuccess(bapResultEntity);
                                } else {
                                    getView().doSubmitFailed(bapResultEntity.errMsg);
                                }
                            }
                        })
        );
    }
}
