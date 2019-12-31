package com.supcon.mes.module_sbda_online.controller;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.view.custom.OnResultListener;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.module_sbda_online.model.bean.EamFileViewUrlEntity;
import com.supcon.mes.module_sbda_online.model.network.ApiService;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 在线预览设备文档：获取预览url
 */
public class EamFileUrlController extends BasePresenterController {

    private OnAPIResultListener<EamFileViewUrlEntity> onAPIResultListener;

    public void getEamFileViewUrl(Long id, OnAPIResultListener<EamFileViewUrlEntity> listener){
        onAPIResultListener = listener;
        queryEamFileViewUrl(id);
    }

    @SuppressLint("CheckResult")
    private void queryEamFileViewUrl(Long id) {
        Api.getInstance().retrofit.create(ApiService.class).getEamFileViewUrl(id)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(throwable -> {
                    EamFileViewUrlEntity entity = new EamFileViewUrlEntity();
                    entity.errMsg = throwable.toString();
                    return entity;
                })
                .subscribeOn(Schedulers.newThread())
                .subscribe(eamFileViewUrlEntity -> {
                    if (eamFileViewUrlEntity.dealSuccess){
                        onAPIResultListener.onSuccess(eamFileViewUrlEntity);
                    }else {
                        onAPIResultListener.onFail(TextUtils.isEmpty(eamFileViewUrlEntity.errMsg)? "文件不存在或异常" : eamFileViewUrlEntity.errMsg);
                    }
                });
    }

}
