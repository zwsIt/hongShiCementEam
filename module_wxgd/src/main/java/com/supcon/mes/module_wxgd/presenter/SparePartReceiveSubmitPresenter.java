package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.module_wxgd.model.contract.SparePartReceiveSubmitContract;
import com.supcon.mes.module_wxgd.model.contract.WXGDSubmitContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;

/**
 * 备件领用
 */
public class SparePartReceiveSubmitPresenter extends SparePartReceiveSubmitContract.Presenter {

    @Override
    public void doSubmitSparePart(Map<String, Object> map) {
        Map<String, RequestBody> formBody = FormDataHelper.createDataFormBody(map);
        mCompositeSubscription.add(
                HttpClient.doSubmitSparePart(formBody)
                        .onErrorReturn(new Function<Throwable, BapResultEntity>() {
                            @Override
                            public BapResultEntity apply(Throwable throwable) throws Exception {
                                BapResultEntity bapResultEntity = new BapResultEntity();
                                bapResultEntity.dealSuccessFlag = false;
                                bapResultEntity.errMsg = throwable.toString();
                                return bapResultEntity;
                            }
                        }).subscribe(new Consumer<BapResultEntity>() {
                    @Override
                    public void accept(BapResultEntity bapResultEntity) throws Exception {
                        if (bapResultEntity.dealSuccessFlag) {
                            getView().doSubmitSparePartSuccess(bapResultEntity);
                        } else {
                            getView().doSubmitSparePartFailed(bapResultEntity.errMsg);
                        }
                    }
                })
        );
    }
}
