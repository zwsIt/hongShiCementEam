package com.supcon.mes.module_olxj.presenter;

import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskCreateContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import java.util.Map;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
public class OLXJTaskCreatePresenter extends OLXJTaskCreateContract.Presenter {

    @Override
    public void  createTempTask(Map<String, Object> map) {

        Map<String, RequestBody> formBody = FormDataHelper.createDataFormBody(map);
        Api.getInstance().setTimeOut(300);
        mCompositeSubscription.add(
                OLXJClient.createTempTask(formBody)
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
                                Api.getInstance().setTimeOut(30);
                                if (bapResultEntity.dealSuccessFlag) {
                                    Objects.requireNonNull(getView()).createTempTaskSuccess(bapResultEntity);
                                } else {
                                    Objects.requireNonNull(getView()).createTempTaskFailed(bapResultEntity.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Api.getInstance().setTimeOut(30);
                                Objects.requireNonNull(getView()).createTempTaskFailed(throwable.toString());
                            }
                        }));
    }

    @Override
    public void createTempTaskNew(Map<String, Object> map) {
        mCompositeSubscription.add(
                OLXJClient.createTempTaskNew(map)
                        .onErrorReturn(new Function<Throwable, CommonEntity<String>>() {
                            @Override
                            public CommonEntity apply(Throwable throwable) throws Exception {
                                CommonEntity commonEntity = new CommonEntity();
                                commonEntity.success = false;
                                commonEntity.errMsg = throwable.toString();
                                return commonEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonEntity<String>>() {
                            @Override
                            public void accept(CommonEntity<String> resultEntity) throws Exception {
                                if (resultEntity.success) {
                                    Objects.requireNonNull(getView()).createTempTaskNewSuccess(resultEntity);
                                } else {
                                    Objects.requireNonNull(getView()).createTempTaskNewFailed(resultEntity.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Objects.requireNonNull(getView()).createTempTaskNewFailed(throwable.toString());
                            }
                        }));
    }


}
