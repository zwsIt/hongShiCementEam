package com.supcon.mes.module_hs_tsd.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.module_hs_tsd.model.contract.ElectricitySubmitContract;
import com.supcon.mes.module_hs_tsd.model.network.HttpClient;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/27
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ElectricityOffSubmitPresenter extends ElectricitySubmitContract.Presenter {

    @Override
    public void submit(String view,Map<String, Object> queryParams,Map<String, Object> attachmentMap, String __pc__) {
        Map<String, RequestBody> params = FormDataHelper.createDataFormBody(queryParams);
        List<MultipartBody.Part> part = FormDataHelper.createFileForm(attachmentMap);
        mCompositeSubscription.add(
                HttpClient.submit(view,params,part,__pc__)
                .onErrorReturn(new Function<Throwable, BapResultEntity>() {
                    @Override
                    public BapResultEntity apply(Throwable throwable) throws Exception {
                        BapResultEntity bapResultEntity = new BapResultEntity();
                        bapResultEntity.errMsg = throwable.toString();
                        return bapResultEntity;
                    }
                })
                .subscribe(new Consumer<BapResultEntity>() {
                    @Override
                    public void accept(BapResultEntity bapResultEntity) throws Exception {
                        if (bapResultEntity.dealSuccessFlag){
                            getView().submitSuccess(bapResultEntity);
                        }else {
                            getView().submitFailed(bapResultEntity.errMsg);
                        }
                    }
                })
        );
    }
}
