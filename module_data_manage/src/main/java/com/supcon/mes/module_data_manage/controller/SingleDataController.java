package com.supcon.mes.module_data_manage.controller;

import android.text.TextUtils;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.constant.DataModule;
import com.supcon.mes.middleware.model.event.DataDownloadEvent;
import com.supcon.mes.middleware.model.event.DataParseEvent;
import com.supcon.mes.middleware.model.event.DataUploadEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.module_data_manage.model.api.DataManaerAPI;
import com.supcon.mes.module_data_manage.model.contract.DataManaerContract;
import com.supcon.mes.module_data_manage.presenter.DataManagerPresenter;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wangshizhan on 2018/8/13
 * Email:wangshizhan@supcom.com
 */
@Presenter(value = DataManagerPresenter.class)
public class SingleDataController extends BasePresenterController implements DataManaerContract.View{

    private OnSuccessListener<Boolean> onSuccessListener;

    private DataModule dataModule;

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void setModule(String module){

        if(!TextUtils.isEmpty(module))
            dataModule = DataModule.getModule(module);
    }


    public void startDownload(DataModule module, String url){
        if(module == null){
            LogUtil.e("download module is null");
            return;
        }
        dataModule = module;

        presenterRouter.create(DataManaerAPI.class).download(dataModule, url);
        LogUtil.d("正在下载："+ dataModule.getModuelName());
    }


    public void upload(DataModule module){
        if(module == null){
            LogUtil.e("upload module is null");
            return;
        }
        dataModule = module;
        presenterRouter.create(DataManaerAPI.class).upload(dataModule);
        LogUtil.d("正在上传："+ dataModule.getModuelName());
    }

    public <Boolean> void setOnSuccessListener(OnSuccessListener<java.lang.Boolean> onSuccessListener){
        this.onSuccessListener = onSuccessListener;
    }

    @Override
    public void downloadSuccess() {
        if(onSuccessListener!=null){
            onSuccessListener.onSuccess(true);
        }
        DataDownloadEvent dataDownloadEvent = new DataDownloadEvent(true);
        dataDownloadEvent.setModuleName(dataModule.getModuelName());
        EventBus.getDefault().post(dataDownloadEvent);
    }

    @Override
    public void downloadFailed(String errorMsg) {
        if(onSuccessListener!=null){
            onSuccessListener.onSuccess(false);
        }

        DataDownloadEvent dataDownloadEvent = new DataDownloadEvent(false, errorMsg);
        dataDownloadEvent.setModuleName(dataModule.getModuelName());
        EventBus.getDefault().post(dataDownloadEvent);
    }

    @Override
    public void uploadSuccess(DataUploadEvent entity) {
        entity.setModuleName(dataModule.getModuelName());
        EventBus.getDefault().post(entity);
    }

    @Override
    public void uploadFailed(String errorMsg) {
        DataUploadEvent dataUploadEvent = new DataUploadEvent(false, errorMsg);
        dataUploadEvent.setModuleName(dataModule.getModuelName());
        EventBus.getDefault().post(dataUploadEvent);
    }

    @Override
    public void parseDataSuccess(DataParseEvent entity) {
        entity.setModuleName(dataModule.getModuelName());
        EventBus.getDefault().post(entity);
    }

    @Override
    public void parseDataFailed(String errorMsg) {
        DataParseEvent dataParseEvent = new DataParseEvent(false, errorMsg);
        dataParseEvent.setModuleName(DataModule.getModuleName(dataModule.getModuelName()));
        EventBus.getDefault().post(dataParseEvent);
    }
}
