package com.supcon.mes.module_data_manage.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.DataModule;
import com.supcon.mes.module_data_manage.controller.SingleDataController;


/**
 * Created by wangshizhan on 2017/12/18.
 * Email:wangshizhan@supcon.com
 */

public class DataManagerService extends IntentService{

    private static final String DATA_MANAGE_DOWNLOAD_DATA = "DATA_MANAGE_DOWNLOAD_DATA";
    private static final String DATA_MANAGE_UPLOAD_DATA = "DATA_MANAGE_UPLOAD_DATA";
    private static final String DATA_MANAGE_UPLOAD_DATA2 = "DATA_MANAGE_UPLOAD_DATA2";
    private static final String DATA_MANAGE_UPLOAD_DATA3 = "DATA_MANAGE_UPLOAD_DATA3";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */

    private static SingleDataController singleDataController;

    public DataManagerService() {
        super("DataManagerService");
        singleDataController = new SingleDataController();
    }

    public static void download(Context context, DataModule dataModule){
        Intent intent = new Intent(context, DataManagerService.class);
        intent.putExtra(Constant.IntentKey.MODULE, dataModule);
        intent.putExtra(Constant.IntentKey.URL, Constant.FILE_PATH+ dataModule.getDownloadFileName());
        intent.setAction(DATA_MANAGE_DOWNLOAD_DATA);
        context.startService(intent);
    }

    public static void upload(Context context, DataModule dataModule){
        Intent intent = new Intent(context, DataManagerService.class);
        intent.putExtra(Constant.IntentKey.MODULE, dataModule);
        intent.setAction(DATA_MANAGE_UPLOAD_DATA);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        DataModule module = (DataModule) intent.getSerializableExtra(Constant.IntentKey.MODULE);
        String url = intent.getStringExtra(Constant.IntentKey.URL);
        handleAction(action, module, url);
    }

    private void handleAction(String action, DataModule module, String url) {

        switch (action){

            case DATA_MANAGE_DOWNLOAD_DATA:
                download(module, url);
                break;

            case DATA_MANAGE_UPLOAD_DATA:
                upload(module);
                break;
        }

    }


    private void download(DataModule module, String filtPath) {
        LogUtil.i("DataManagerService dowload module "+module.getModuelName()+" , save in path "+filtPath);
        singleDataController.startDownload(module, filtPath);
    }

    private void upload(DataModule module) {
        LogUtil.i("DataManagerService upload module "+module);
        singleDataController.upload(module);
    }

}
