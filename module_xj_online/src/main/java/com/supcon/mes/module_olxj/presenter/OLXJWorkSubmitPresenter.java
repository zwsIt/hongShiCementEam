package com.supcon.mes.module_olxj.presenter;

import android.text.TextUtils;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.XJPathEntityDao;
import com.supcon.mes.middleware.model.event.DataUploadEvent;
import com.supcon.mes.middleware.util.FileUtil;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.middleware.util.ZipUtils;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJWorkSubmitContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by wangshizhan on 2019/4/4
 * Email:wangshizhan@supcom.com
 */
public class OLXJWorkSubmitPresenter extends OLXJWorkSubmitContract.Presenter{
    @Override
    public void uploadOLXJAreaData(OLXJAreaEntity areaEntity) {
        createXjFile(areaEntity);
    }

    private void createXjFile(OLXJAreaEntity areaEntity) {
        FileUtil.createDir(Constant.FILE_PATH + "xj");

        List<String> includeFiles = new ArrayList<>();
        includeFiles.add("xj_upload.json");

        for (OLXJWorkItemEntity xjWorkItemEntity : areaEntity.workItemEntities) {
            String imgUrl = xjWorkItemEntity.xjImgUrl;

            if (!TextUtils.isEmpty(imgUrl)) {
                imgUrl = imgUrl.replaceAll("/storage/emulated/0/eam/xj/pics/", "");
                if (imgUrl.contains(",")) {
                    includeFiles.addAll(Arrays.asList(imgUrl.split(",")));
                } else {
                    includeFiles.add(imgUrl);
                }
            }
        }
        File xjJsonFile = new File(Constant.XJ_PATH, "xj_upload.json");
        String result =  areaEntity.toString().replaceAll("/storage/emulated/0/eam/xj/pics/", "");
        FileUtil.write2File(xjJsonFile.getAbsolutePath(), result);
        LogUtil.i("xj upload:" + result);
        try {
            ZipUtils.zipFolderEx(Constant.XJ_PATH, Constant.FILE_PATH + "xj_upload.json", includeFiles);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        uploadXJZipFile(new File(Constant.FILE_PATH + "xj_upload.json"));
    }

    private void uploadXJZipFile(File uploadZip) {
        Api.getInstance().setTimeOut(300);
        mCompositeSubscription.add(
                OLXJClient.submitPotrolTaskByWork(FormDataHelper.createZipFileForm(uploadZip), uploadZip.getName())
                        .compose(RxSchedulers.io_main())
                        .onErrorReturn(throwable -> {
                            Api.getInstance().setTimeOut(30);
                            ResultEntity resultEntity = new ResultEntity();
                            resultEntity.success = false;
                            resultEntity.errMsg = throwable.toString();
                            return resultEntity;
                        })
                        .subscribe(commonEntity -> {
                            Api.getInstance().setTimeOut(30);
                            if (commonEntity.success) {
                                DataUploadEvent dataUploadEvent = new DataUploadEvent(true);
                                dataUploadEvent.setSize(1);
                                final int finishedSize = EamApplication.dao().getXJPathEntityDao().queryBuilder()
                                        .where(XJPathEntityDao.Properties.State.eq("已检")).list().size();
                                if (finishedSize > 10)
                                    dataUploadEvent.setMsg("仍有" + (finishedSize - 10) + "条巡检数据待上传");
                                Objects.requireNonNull(getView()).uploadOLXJAreaDataSuccess();
                            } else {
                                Objects.requireNonNull(getView()).uploadOLXJAreaDataFailed(commonEntity.errMsg);
                            }
                        }, throwable -> Objects.requireNonNull(getView()).uploadOLXJAreaDataFailed(throwable.toString()))

        );

    }
}
