package com.supcon.mes.module_overhaul_workticket.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.module_overhaul_workticket.model.contract.WorkTicketSubmitContract;
import com.supcon.mes.module_overhaul_workticket.model.network.HttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/24
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class WorkTicketSubmitPresenter extends WorkTicketSubmitContract.Presenter {

    @Override
    public void submit(String view, Map<String, Object> queryParams, String __pc__) {
        Map<String, RequestBody> params = FormDataHelper.createDataFormBody(queryParams);
        mCompositeSubscription.add(
                HttpClient.submit(view,params,__pc__)
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