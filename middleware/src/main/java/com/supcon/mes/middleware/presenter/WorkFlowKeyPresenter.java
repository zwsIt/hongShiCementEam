package com.supcon.mes.middleware.presenter;

import android.annotation.SuppressLint;

import com.google.common.base.Throwables;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.LongResultEntity;
import com.supcon.mes.middleware.model.contract.PcQueryContract;
import com.supcon.mes.middleware.model.contract.WorkFlowKeyContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/6/24
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class WorkFlowKeyPresenter extends WorkFlowKeyContract.Presenter {

    @SuppressLint("CheckResult")
    @Override
    public void queryWorkFlowKeyOnly(String entityCode, String type) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getProcessKey(entityCode, type)
                        .onErrorReturn(throwable -> {
                            CommonEntity commonEntity = new CommonEntity();
                            commonEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return commonEntity;
                        })
                        .subscribe(new Consumer<CommonEntity>() {
                            @Override
                            public void accept(CommonEntity commonEntity) throws Exception {
                                if(commonEntity.success){
                                    if(getView() != null)
                                        getView().queryWorkFlowKeyOnlySuccess(commonEntity);
                                } else{
                                    if(getView() != null)
                                        getView().queryWorkFlowKeyOnlyFailed(commonEntity.errMsg);
                                }
                            }
                        })
        );
    }

    @Override
    public void queryWorkFlowKeyAndPermission(String entityCode, String type) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getProcessKey(entityCode, type)
                        .subscribeOn(Schedulers.io()) // 子线程请求
                        .onErrorReturn(throwable -> {
                            CommonEntity commonEntity = new CommonEntity();
                            commonEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return commonEntity;
                        })
                        .observeOn(AndroidSchedulers.mainThread())//主线程中处理结果
                        .doOnNext(commonEntity -> {
                            if (!commonEntity.success) {
                                getView().queryWorkFlowKeyAndPermissionFailed(commonEntity.errMsg);
                            }
                        })
                        .observeOn(Schedulers.io()) // 切换到子线程再次请求
                        .filter(new Predicate<CommonEntity>() {
                            @Override
                            public boolean test(CommonEntity commonEntity) throws Exception {
                                return commonEntity.success;
                            }
                        })
                        .flatMap((Function<CommonEntity, Publisher<LongResultEntity>>) entity -> MiddlewareHttpClient.checkModulePermission(EamApplication.getUserName(), String.valueOf(entity.result)))
                        .onErrorReturn(throwable -> {
                            LongResultEntity longResultEntity = new LongResultEntity();
                            longResultEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return longResultEntity;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(longResultEntity -> {
                            if(longResultEntity.success){
                                if(getView() != null)
                                    getView().queryWorkFlowKeyAndPermissionSuccess(longResultEntity);
                            }
                            else{
                                if(getView() != null)
                                    getView().queryWorkFlowKeyAndPermissionFailed(longResultEntity.errMsg);
                            }
                        })
        );
    }

    @Override
    public void queryWorkFlowKeyToPc(String operateCode, String entityCode, String type) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getProcessKey(entityCode,type)
                .onErrorReturn(throwable -> {
                    CommonEntity commonEntity = new CommonEntity();
                    commonEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    return commonEntity;
                })
                .doOnNext(commonEntity -> {
                    if (!commonEntity.success){
                        getView().queryWorkFlowKeyToPcFailed(commonEntity.errMsg);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap((Function<CommonEntity, Publisher<CommonEntity>>) commonEntity -> MiddlewareHttpClient.getPc(operateCode, String.valueOf(commonEntity.result)))
                .onErrorReturn(throwable -> {
                    CommonEntity commonEntity = new CommonEntity();
                    commonEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    return commonEntity;
                })
                .subscribe(new Consumer<CommonEntity>() {
                    @Override
                    public void accept(CommonEntity commonEntity) throws Exception {
                        if(commonEntity.success){
                            if(getView() != null)
                                getView().queryWorkFlowKeyToPcSuccess(commonEntity);
                        } else{
                            if(getView() != null)
                                getView().queryWorkFlowKeyToPcFailed(commonEntity.errMsg);
                        }
                    }
                })
        );
    }
}
