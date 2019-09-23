package com.supcon.mes.module_score.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.module_score.model.contract.ScoreStaffSubmitContract;
import com.supcon.mes.module_score.model.contract.ScoreSubmitContract;
import com.supcon.mes.module_score.model.network.ScoreHttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;

public class ScoreInspectorStaffSubmitPresenter extends ScoreStaffSubmitContract.Presenter {
    @Override
    public void doStaffSubmit(String url, Map<String, Object> map) {
        Map<String, RequestBody> formBody = FormDataHelper.createDataFormBody(map);
        mCompositeSubscription.add(
                ScoreHttpClient.doStaffSubmit(url, formBody)
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
                                    getView().doStaffSubmitSuccess(bapResultEntity);
                                } else {
                                    getView().doStaffSubmitFailed(bapResultEntity.errMsg);
                                }
                            }
                        })
        );
    }
}
