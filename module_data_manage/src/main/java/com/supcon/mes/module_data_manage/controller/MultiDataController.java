package com.supcon.mes.module_data_manage.controller;

import android.text.TextUtils;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.middleware.constant.DataModule;
import com.supcon.mes.middleware.model.event.DataDownloadEvent;
import com.supcon.mes.middleware.model.event.DataParseEvent;
import com.supcon.mes.middleware.model.event.DataUploadEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_data_manage.model.bean.DataResultEntity;
import com.supcon.mes.module_data_manage.model.contract.DataManaerContract;
import com.supcon.mes.module_data_manage.presenter.DataManagerPresenter;
import com.supcon.mes.module_data_manage.service.DataManagerService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/8/13
 * Email:wangshizhan@supcom.com
 */
@Presenter(value = DataManagerPresenter.class)
public class MultiDataController extends BasePresenterController implements DataManaerContract.View{

    private OnSuccessListener<Boolean> onSuccessListener;

    private List<DataModule> downloadList = new ArrayList<>();

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
        downloadList.add(DataModule.getModule(module));
    }

    public void setModules(List<String> modules){
        if(modules!=null && modules.size() !=0) {
            for (String module : modules) {
                downloadList.add(DataModule.getModule(module));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadResult(DataDownloadEvent event){
        if(event.isSuccess()){
//            onLoading("下载完成， 正在解析数据...");
        }
        else{
            String msg = ErrorMsgHelper.msgParse(event.getMsg());
            if(TextUtils.isEmpty(msg)){//属于未登陆的情况
                return;
            }
            DataModule dataModule = DataModule.getModule(event.getModuleName());
            if(downloadList.contains(dataModule)){
                downloadList.remove(dataModule);
            }
            if(!continueDownload()){
                if(onSuccessListener!=null){
                    onSuccessListener.onSuccess(false);
                }
            }


        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event){
        continueDownload();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParseResult(DataParseEvent event){

        if(event.isSuccess()){

            updateViewForResultSuccess(event);

            if(!continueDownload()){
                if(onSuccessListener!=null){
                    onSuccessListener.onSuccess(true);
                }
            }

        }
        else{
            String msg = ErrorMsgHelper.msgParse(event.getMsg());
            DataModule dataModule = DataModule.getModule(event.getModuleName());
            updateViewForResultFailed(msg, dataModule);


            if(!continueDownload()){
                if(onSuccessListener!=null){
                    onSuccessListener.onSuccess(false);
                }
            }
        }

    }

    private void updateViewForResultFailed(String msg, DataModule dataModule) {
        LogUtil.e(dataModule.getModuelName()+"下载失败："+msg);
        if(downloadList.contains(dataModule)){
            downloadList.remove(dataModule);
        }
    }

    private void updateViewForResultSuccess(DataParseEvent event) {
        LogUtil.d(event.getModuleName()+"下载成功！");


        if(event.getModuleName().equals(DataModule.XJ_BASE.getModuelName())){
            if(downloadList.contains(DataModule.XJ_BASE)){
                downloadList.remove(DataModule.XJ_BASE);
            }
        }
        else if(event.getModuleName().equals(DataModule.EAM_BASE.getModuelName())){
            if(downloadList.contains(DataModule.EAM_BASE)){
                downloadList.remove(DataModule.EAM_BASE);
            }
        }
    }


    private boolean continueDownload() {

        if(downloadList.size()!=0){
            DataModule dataModule = downloadList.get(0);
            DataManagerService.download(MBapApp.getAppContext(), dataModule);
            LogUtil.d("正在下载："+ dataModule.getModuelName());
            return true;
        }

        return false;
    }

    public void startDownload(){
        continueDownload();
    }

    public <Boolean> void setOnSuccessListener(OnSuccessListener<java.lang.Boolean> onSuccessListener){
        this.onSuccessListener = onSuccessListener;
    }


    @Override
    public void downloadSuccess() {

    }

    @Override
    public void downloadFailed(String errorMsg) {

    }

    @Override
    public void uploadSuccess(DataUploadEvent entity) {

    }

    @Override
    public void uploadFailed(String errorMsg) {

    }

    @Override
    public void parseDataSuccess(DataParseEvent entity) {

    }

    @Override
    public void parseDataFailed(String errorMsg) {

    }
}
