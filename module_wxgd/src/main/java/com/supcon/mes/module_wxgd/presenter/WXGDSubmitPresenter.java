package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.module_wxgd.model.contract.WXGDSubmitContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
public class WXGDSubmitPresenter extends WXGDSubmitContract.Presenter {

    //维修工单接单提交
    @Override
    public void doReceiveSubmit(Map<String, Object> map) {

        Map<String, RequestBody> formBody = FormDataHelper.createDataFormBody(map);

        mCompositeSubscription.add(
                HttpClient.receiveSubmit(formBody)
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
                                    getView().doReceiveSubmitSuccess(bapResultEntity);
                                } else {
                                    getView().doReceiveSubmitFailed(bapResultEntity.errMsg);
                                }
                            }
                        }));
    }

    //维修工单执行提交
    @Override
    public void doExecuteSubmit(Map<String, Object> map) {
        Map<String, RequestBody> formBody = FormDataHelper.createDataFormBody(map);
        mCompositeSubscription.add(
                HttpClient.executeSubmit(formBody)
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
                                    getView().doReceiveSubmitSuccess(bapResultEntity);
                                } else {
                                    getView().doReceiveSubmitFailed(bapResultEntity.errMsg);
                                }
                            }
                        }));
    }

    //维修工单派工提交
    @Override
    public void doDispatcherSubmit(Map<String, Object> map) {
        Map<String, RequestBody> formBody = FormDataHelper.createDataFormBody(map);
        mCompositeSubscription.add(
                HttpClient.doSubmitDispatch(formBody)
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
                            getView().doDispatcherSubmitSuccess(bapResultEntity);
                        } else {
                            getView().doDispatcherSubmitFailed(bapResultEntity.errMsg);
                        }
                    }
                })
        );
    }

    @Override
    public void doDispatcherWarnSubmit(Map<String, Object> map) {
        Map<String, RequestBody> formBody = FormDataHelper.createDataFormBody(map);
        mCompositeSubscription.add(
                HttpClient.doSubmitDispatchWarn(formBody)
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
                            getView().doDispatcherWarnSubmitSuccess(bapResultEntity);
                        } else {
                            getView().doDispatcherWarnSubmitFailed(bapResultEntity.errMsg);
                        }
                    }
                })
        );
    }

    //维修工单验收提交
    @Override
    public void doAcceptChkSubmit(Map<String, Object> map, Map<String, Object> attachmentMap) {
        Map<String, RequestBody> formBody = FormDataHelper.createDataFormBody(map);
        List<MultipartBody.Part> parts = FormDataHelper.createFileForm(attachmentMap);

        mCompositeSubscription.add(
                HttpClient.doAcceptChk(formBody, parts)
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
                            getView().doAcceptChkSubmitSuccess(bapResultEntity);
                        } else {
                            getView().doAcceptChkSubmitFailed(bapResultEntity.errMsg);
                        }
                    }
                })
        );
    }


}
