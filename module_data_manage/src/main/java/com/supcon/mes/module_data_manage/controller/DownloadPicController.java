package com.supcon.mes.module_data_manage.controller;

import android.annotation.SuppressLint;

import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_data_manage.model.network.DataManagerHttpClient;
import com.supcon.mes.module_data_manage.util.Util;
import com.supcon.mes.middleware.util.ZipUtils;

public class DownloadPicController extends BasePresenterController {

    /**
     * @description 下载巡检指导图片
     * @param
     * @param areaIds
     * @return
     * @author zhangwenshuai1 2019/1/4
     *
     */
    @SuppressLint("CheckResult")
    public void downloadXJGuidePic(String areaIds){
        Api.getInstance().setTimeOut(900);
        DataManagerHttpClient.downloadXJGuidePicZipFile(areaIds)
                .onErrorReturn(throwable -> {
                    Api.getInstance().setTimeOut(30);
                    return null;
                })
                .filter(responseBody -> responseBody != null)
                .map(responseBody -> {
                    Api.getInstance().setTimeOut(30);
                    return Util.writeZipResponseBodyToDisk(Constant.FILE_PATH + "xjGuideImages.zip",responseBody);
                })
                .subscribe(file -> {
                    try {
                        if (file.exists()){
                            ZipUtils.upZipFile(file.getAbsolutePath(),Constant.FILE_PATH);
                        }
                    } catch (Exception e) {
                        LogUtil.w("downloadXJGuidePic：",e.getMessage());
                        e.printStackTrace();
                    }
                });
    }
}
