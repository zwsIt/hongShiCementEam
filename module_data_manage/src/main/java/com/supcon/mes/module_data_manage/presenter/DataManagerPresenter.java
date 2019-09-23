package com.supcon.mes.module_data_manage.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.App;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.cache.CacheUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.DataModule;
import com.supcon.mes.middleware.constant.EamPermission;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.DaoSession;
import com.supcon.mes.middleware.model.bean.QXDealTypeEntity;
import com.supcon.mes.middleware.model.bean.QXGLEntity;
import com.supcon.mes.middleware.model.bean.QXTypeEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.RunStateEntity;
import com.supcon.mes.middleware.model.bean.XJAreaEntity;
import com.supcon.mes.middleware.model.bean.XJAreaEntityDao;
import com.supcon.mes.middleware.model.bean.XJPathEntity;
import com.supcon.mes.middleware.model.bean.XJPathEntityDao;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntityDao;
import com.supcon.mes.middleware.model.bean.YHEntityDto;
import com.supcon.mes.middleware.model.event.DataParseEvent;
import com.supcon.mes.middleware.model.event.DataUploadEvent;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.module_data_manage.controller.DownloadPicController;
import com.supcon.mes.module_data_manage.model.bean.AlllDeviceResultEntity;
import com.supcon.mes.module_data_manage.model.bean.DataUploadResultEntity;
import com.supcon.mes.module_data_manage.model.bean.EamInfo;
import com.supcon.mes.module_data_manage.model.bean.EamInfoEntity;
import com.supcon.mes.module_data_manage.model.bean.QXGLUploadListEntity;
import com.supcon.mes.module_data_manage.model.bean.UploadResultEntity;
import com.supcon.mes.module_data_manage.model.bean.XJBasicInfoEntity;
import com.supcon.mes.module_data_manage.model.bean.XJDataEntity;
import com.supcon.mes.module_data_manage.model.bean.XJDownloadFailEntity;
import com.supcon.mes.module_data_manage.model.bean.XJDownloadListEntity;
import com.supcon.mes.module_data_manage.model.bean.YHGLUploadListEntity;
import com.supcon.mes.module_data_manage.model.contract.DataManaerContract;
import com.supcon.mes.module_data_manage.model.network.DataManagerHttpClient;
import com.supcon.mes.middleware.util.FileUtil;
import com.supcon.mes.module_data_manage.util.Util;
import com.supcon.mes.middleware.util.ZipUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.supcon.mes.module_data_manage.util.Util.createZipFileForm;


/**
 * @author wangshizhan
 * @date 2018/3/22
 * Email:wangshizhan@supcon.com
 */

public class DataManagerPresenter extends DataManaerContract.Presenter {

    private static final String YH_UPLOAD_JSON_FILE_NAME = "yh_upload.json";
    private static final String YH_UPLOAD_ZIP_FILE_NAME = "yh_upload.zip";

    private static final String QX_UPLOAD_JSON_FILE_NAME = "qx_upload.json";
    private static final String QX_UPLOAD_ZIP_FILE_NAME = "qx_upload.zip";

    private static final String XJ_UPLOAD_JSON_FILE_NAME = "xj_upload.json";
    private static final String XJ_UPLOAD_ZIP_FILE_NAME = "xj_upload.zip";

    @Override
    public void download(DataModule module, String localPath) {
        FileUtil.createDir(Constant.FILE_PATH);
        switch (module.name()) {
            case "EAM_BASE":
                downloadEamBase();
                break;
            case "XJ_BASE":
                downloadXjBase();
                break;
            case "EAM_DEVICE":
                downloadEamDevice();
                break;
            case "XJ":
                downloadXj(module, localPath);
                break;
            case "YH":
                break;
            default:
        }
    }

    private void downloadXj(DataModule module, String localPath) {
        Api.getInstance().setTimeOut(300);
        mCompositeSubscription.add(DataManagerHttpClient.downloadXJZipFile(EamApplication.getAccountInfo().staffId)
                .onErrorReturn(throwable -> {
                    Api.getInstance().setTimeOut(30);
                    getView().downloadFailed(throwable.toString());
                    return null;
                })
                .filter(responseBody -> responseBody != null)
                .map(responseBody -> {
                    Api.getInstance().setTimeOut(30);
                    return Util.writeZipResponseBodyToDisk(localPath, responseBody);
                })
                .subscribe(file -> {
                    if (file != null) {
                        downloadResult(file, module, localPath);
                    } else {
                        getView().downloadFailed("下载文件为空，服务器数据出错！");
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        DataManagerPresenter.this.getView().downloadFailed(throwable.toString());
                    }
                }));
    }

    private void downloadEamDevice() {
        Api.getInstance().setTimeOut(300);
        mCompositeSubscription.add(
                DataManagerHttpClient.listPowerBaseInfo("")
                        .onErrorReturn(throwable -> {
                            Api.getInstance().setTimeOut(60);
                            AlllDeviceResultEntity alllDeviceResultEntity = new AlllDeviceResultEntity();
                            alllDeviceResultEntity.success = false;
                            alllDeviceResultEntity.errMsg = throwable.toString();
                            return alllDeviceResultEntity;
                        })
                        .subscribe(alllDeviceResultEntity -> {
                            Api.getInstance().setTimeOut(60);
                            if (alllDeviceResultEntity.success) {
                                parseEamInfo(alllDeviceResultEntity);
                                DataParseEvent dataParseEvent = new DataParseEvent(true);
                                Objects.requireNonNull(getView()).parseDataSuccess(dataParseEvent);
                            } else {
                                Objects.requireNonNull(getView()).downloadFailed(alllDeviceResultEntity.errMsg);
                            }
                        })

        );
    }

    private void downloadXjBase() {
        mCompositeSubscription.add(
                DataManagerHttpClient.getXJBasicInfo()
                        .onErrorReturn(throwable -> {
                            XJBasicInfoEntity basicInfoEntity = new XJBasicInfoEntity();
                            basicInfoEntity.success = false;
                            basicInfoEntity.errMsg = throwable.toString();
                            return basicInfoEntity;
                        })
                        .subscribe(basicInfoEntity -> {
                            if (basicInfoEntity.success) {
                                parseXJBase(basicInfoEntity);
                                DataParseEvent dataParseEvent = new DataParseEvent(true);
                                getView().parseDataSuccess(dataParseEvent);
                            } else {
                                getView().downloadFailed(basicInfoEntity.errMsg);
                            }
                        })
        );
    }

    private void downloadEamBase() {
        LogUtil.i(this.getClass().getSimpleName(), "getBasicInfo");
        mCompositeSubscription.add(
                DataManagerHttpClient.getEamBasicInfo()
                        .onErrorReturn(throwable -> {
                            EamInfoEntity eamInfoEntity = new EamInfoEntity();
                            eamInfoEntity.success = false;
                            eamInfoEntity.errMsg = throwable.toString();
                            return eamInfoEntity;
                        })
                        .subscribe(eamInfoEntity -> {
                            if (eamInfoEntity.success) {
                                parseBaseInfo(eamInfoEntity);
                                DataParseEvent dataParseEvent = new DataParseEvent(true);
                                getView().parseDataSuccess(dataParseEvent);
                            } else {
                                getView().downloadFailed(eamInfoEntity.errMsg);
                            }
                        })
        );
    }

    @SuppressLint("CheckResult")
    private void parseEamInfo(AlllDeviceResultEntity entity) {
        Flowable.just(entity)
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(alllDeviceResultEntity -> {
                    LogUtil.i("insertAllDevice thread:" + Thread.currentThread().getName());
                    insertAllDevice(entity.result);
                });
    }

    private void insertAllDevice(AlllDeviceResultEntity.AlllDeviceResult result) {
        DataParseEvent dataParseEvent = new DataParseEvent(true);
        if (result.BEAM060_01 != null && result.BEAM060_01.size() != 0) {
            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_01, EamApplication.getAccountInfo().userId, EamPermission.DeviceModify.name());
        }
        if (result.BEAM060_02 != null && result.BEAM060_02.size() != 0) {
            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_02, EamApplication.getAccountInfo().userId, EamPermission.DeviceCheck.name());
        }
//        if (result.BEAM060_03 != null && result.BEAM060_03.size() != 0) {
//            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_03, EamApplication.getAccountInfo().userId, EamPermission.Point.name());
//        }
//        if (result.BEAM060_04 != null && result.BEAM060_04.size() != 0) {
//            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_04, EamApplication.getAccountInfo().userId, EamPermission.Maintain.name());
//        }
//        if (result.BEAM060_05 != null && result.BEAM060_05.size() != 0) {
//            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_05, EamApplication.getAccountInfo().userId, EamPermission.Lubricate.name());
//        }
        if (result.BEAM060_06 != null && result.BEAM060_06.size() != 0) {
            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_06, EamApplication.getAccountInfo().userId, EamPermission.Repair.name());
        }
        if (result.BEAM060_07 != null && result.BEAM060_07.size() != 0) {
            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_07, EamApplication.getAccountInfo().userId, EamPermission.Insection.name());
        }
        if (result.BEAM060_08 != null && result.BEAM060_08.size() != 0) {
            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_08, EamApplication.getAccountInfo().userId, EamPermission.Fault.name());
        }
//        if (result.BEAM060_09 != null && result.BEAM060_09.size() != 0) {
//            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_09, EamApplication.getAccountInfo().userId, EamPermission.RunningState.name());
//        }
//        if (result.BEAM060_10 != null && result.BEAM060_10.size() != 0) {
//            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_10, EamApplication.getAccountInfo().userId, EamPermission.Predict.name());
//        }
//        if (result.BEAM060_11 != null && result.BEAM060_11.size() != 0) {
//            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_11, EamApplication.getAccountInfo().userId, EamPermission.MeaDevice.name());
//        }
//        if (result.BEAM060_12 != null && result.BEAM060_12.size() != 0) {
//            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_12, EamApplication.getAccountInfo().userId, EamPermission.SpecialDevice.name());
//        }
//        if (result.BEAM060_13 != null && result.BEAM060_13.size() != 0) {
//            DeviceManager.getInstance().insertOrUpdateDevices(result.BEAM060_13, EamApplication.getAccountInfo().userId, EamPermission.Other.name());
//        }
        DeviceManager.getInstance().updateDatabase();
        getView().parseDataSuccess(dataParseEvent);
    }

    private void parseXJBase(XJBasicInfoEntity entity) {

    }

    private void parseBaseInfo(EamInfoEntity eamInfoEntity) {

        if (null != eamInfoEntity.result) {

            EamInfo eamInfo = eamInfoEntity.result;
            List<QXTypeEntity> faultType = eamInfo.faultType;
            List<QXDealTypeEntity> dealType = eamInfo.dealType;
            List<RunStateEntity> runStateParams = eamInfo.runStateParams;

            //将公共信息保存到缓存中
            CacheUtil.putString(Constant.SPKey.FAULT_TYPE, GsonUtil.gsonString(faultType));
            CacheUtil.putString(Constant.SPKey.DEAL_TYPE, GsonUtil.gsonString(dealType));
            CacheUtil.putString(Constant.SPKey.RUNNING_STATE_PARAM, GsonUtil.gsonString(runStateParams));
            //设备状态，设备档案用
            CacheUtil.putString(Constant.SPKey.STATE_TYPE, GsonUtil.gsonString(eamInfo.stateType));

            //是否自动生成维修工单
            SharedPreferencesUtils.setParam(App.getAppContext(), Constant.SPKey.IS_AUTO_REPAIR, eamInfo.isAutoRepair);

        }
    }

    private void downloadResult(File file, DataModule module, String localPath) {
        try {
            ZipUtils.upZipFile(localPath, Constant.FILE_PATH);//将下载的xj_download.zip文件解压
        } catch (Exception e) {
            e.printStackTrace();
            getView().downloadFailed("解压出错！");
        }
        if (file.exists()) {
            parseData(module, localPath);
        } else {
            getView().downloadFailed("未找到下载的文件！");
        }

    }


    @SuppressLint("CheckResult")
    @Override
    public void upload(DataModule module) {

        switch (module.name()) {

            case "XJ":
                uploadXjFile(module);
                break;

            case "YH":
                uploadYhFile(module);
                break;

            case "QX":
                uploadQxFile(module);
                break;

            default:
        }
    }

    @SuppressLint("CheckResult")
    private void uploadYhFile(DataModule module) {
        FileUtil.createDir(Constant.YH_PATH);

        List<YHEntityDto> yhEntityDtos = YHEntityDto.genUploadEntities();

        if (checkUploadFailed(yhEntityDtos, "没有可以上传的隐患数据")) {
            return;
        }

        YHGLUploadListEntity yhglUploadListEntity = new YHGLUploadListEntity();
        yhglUploadListEntity.faultInfoRecord = yhEntityDtos;

        List<String> includeFileNames = new ArrayList<>();
        includeFileNames.add(YH_UPLOAD_JSON_FILE_NAME);
        int size = yhEntityDtos.size();

        for (YHEntityDto yhEntityDto : yhEntityDtos) {
            String imgUrl = yhEntityDto.getFaultPicPaths();
            if (!TextUtils.isEmpty(imgUrl)) {
                List<String> imgs = new ArrayList<>();
                if (imgUrl.contains(",")) {
                    imgs.addAll(Arrays.asList(imgUrl.split(",")));
                } else {
                    imgs.add(imgUrl);
                }
                Flowable.fromIterable(imgs)
                        .filter(s -> !TextUtils.isEmpty(s) && !s.contains("createRecord"))
                        .subscribe(includeFileNames::add);
            }
        }
        File jsonFile = new File(Constant.YH_PATH, YH_UPLOAD_JSON_FILE_NAME);
        FileUtil.write2File(jsonFile.getAbsolutePath(), yhglUploadListEntity.toString());
        File uploadZip = new File(Constant.FILE_PATH + YH_UPLOAD_ZIP_FILE_NAME);

        try {
            ZipUtils.zipFolderEx(Constant.YH_PATH, Constant.FILE_PATH + YH_UPLOAD_ZIP_FILE_NAME, includeFileNames);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        uploadZipFile(uploadZip, module.name(), size);
    }

    private boolean checkUploadFailed(List list, String errMes) {
        if (list == null || list.size() == 0) {
            getView().uploadFailed(errMes);
            return true;
        }
        return false;
    }

    @SuppressLint("CheckResult")
    private void uploadQxFile(DataModule module) {
        FileUtil.createDir(Constant.QX_PATH);

        List<QXGLEntity> qxglEntities = QXGLEntity.genLocalFinishedEntities();

        if (checkUploadFailed(qxglEntities, "没有可以上传的缺陷数据！")) {
            return;
        }

        QXGLUploadListEntity qxglUploadListEntity = new QXGLUploadListEntity();
        qxglUploadListEntity.faultInfos = qxglEntities;

        List<String> includeFileNames = new ArrayList<>();
        includeFileNames.add(QX_UPLOAD_JSON_FILE_NAME);
        int size = qxglEntities.size();

        for (QXGLEntity qxglEntity : qxglEntities) {
            String imgUrl = qxglEntity.faultPicPaths;
            if (!TextUtils.isEmpty(imgUrl)) {

                List<String> imgs = new ArrayList<>();

                if (imgUrl.contains(",")) {
                    imgs.addAll(Arrays.asList(imgUrl.split(",")));

                } else {
                    imgs.add(imgUrl);
                }

                Flowable.fromIterable(imgs)
                        .filter(s -> !TextUtils.isEmpty(s) && !s.contains("createRecord"))
                        .subscribe(includeFileNames::add);
            }
        }

        LogUtil.i("qx upload:" + qxglUploadListEntity.toString());
        File jsonFile = new File(Constant.QX_PATH, QX_UPLOAD_JSON_FILE_NAME);
        FileUtil.write2File(jsonFile.getAbsolutePath(), qxglUploadListEntity.toString());
        File uploadZip = new File(Constant.FILE_PATH + QX_UPLOAD_ZIP_FILE_NAME);

        try {
            ZipUtils.zipFolderEx(Constant.QX_PATH, Constant.FILE_PATH + QX_UPLOAD_ZIP_FILE_NAME, includeFileNames);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        uploadZipFile(uploadZip, module.name(), size);
    }

    private void uploadXjFile(DataModule module) {
        FileUtil.createDir(Constant.FILE_PATH + "xj");

        XJDownloadListEntity xjDownloadListEntity = new XJDownloadListEntity();
        XJDataEntity xjDataEntity = new XJDataEntity();

        List<XJWorkItemEntity> xjWorkItemEntities = new ArrayList<>();
        List<XJAreaEntity> xjAreaEntities = new ArrayList<>();
        xjDataEntity.mobileWorkGroups = EamApplication.dao().getXJPathEntityDao().queryBuilder()
                .where(XJPathEntityDao.Properties.State.eq("已检")).limit(10).list();

        if (checkUploadFailed(xjDataEntity.mobileWorkGroups, "没有已经完成的巡检任务数据！")) {
            return;
        }

        int size = xjDataEntity.mobileWorkGroups.size();


        for (XJPathEntity xjPathEntity : xjDataEntity.mobileWorkGroups) {
            xjAreaEntities.addAll(EamApplication.dao().getXJAreaEntityDao().queryBuilder()
                    .where(XJAreaEntityDao.Properties.TaskId.eq(xjPathEntity.id)).list());
        }
        for (XJPathEntity xjPathEntity : xjDataEntity.mobileWorkGroups) {
            xjWorkItemEntities.addAll(EamApplication.dao().getXJWorkItemEntityDao().queryBuilder()
                    .where(XJWorkItemEntityDao.Properties.TaskId.eq(xjPathEntity.id)).list());
        }

        if (checkUploadFailed(xjWorkItemEntities, "没有已经完成的巡检作业项！")) {
            return;
        }
        xjDataEntity.mobileEamWorkItems = xjWorkItemEntities;

        if (checkUploadFailed(xjAreaEntities, "没有已经完成的巡检作业！")) {
            return;
        }
        xjDataEntity.mobileEamWorks = xjAreaEntities;
        xjDownloadListEntity.protrolTasks = xjDataEntity;
        //TODO...处理图片、json文件的压缩
        List<String> includeFiles = new ArrayList<>();
        includeFiles.add(XJ_UPLOAD_JSON_FILE_NAME);

        for (XJWorkItemEntity xjWorkItemEntity : xjWorkItemEntities) {
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
        File xjJsonFile = new File(Constant.XJ_PATH, XJ_UPLOAD_JSON_FILE_NAME);
        String result =  xjDownloadListEntity.toString().replaceAll("/storage/emulated/0/eam/xj/pics/", "");
        FileUtil.write2File(xjJsonFile.getAbsolutePath(),result);
        LogUtil.i("xj upload:" + result);
        try {
            ZipUtils.zipFolderEx(Constant.XJ_PATH, Constant.FILE_PATH + XJ_UPLOAD_ZIP_FILE_NAME, includeFiles);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        uploadZipFile(new File(Constant.FILE_PATH + XJ_UPLOAD_ZIP_FILE_NAME), module.name(), size);
    }

    private void uploadZipFile(File uploadZip, String moduleName, int size) {
        if ("XJ".equals(moduleName)) {
            uploadXJZipFile(uploadZip, size);
        } else if ("QX".equals(moduleName)) {
            uploadQXZipFile(uploadZip, size);
        } else if ("YH".equals(moduleName)) {
            uploadYHZipFile(uploadZip, size);
        }
    }

    private void uploadYHZipFile(File uploadZip, int size) {
        mCompositeSubscription.add(
                DataManagerHttpClient.uploadYHZipFile(createZipFileForm(uploadZip), uploadZip.getName())
                        .compose(RxSchedulers.io_main())
                        .onErrorReturn(throwable -> {
//                            UploadResultEntity resultEntity = new UploadResultEntity();
//                            resultEntity.success = false;
//                            resultEntity.errMsg = throwable.toString();
//                            resultEntity.module = DataModule.YH.getModuelName();
//                            DataUploadResultEntity dataUploadResultEntity = new DataUploadResultEntity();
//                            dataUploadResultEntity.mobileMsg = resultEntity;
//                            return dataUploadResultEntity;
                            CommonEntity commonEntity = new CommonEntity();
                            commonEntity.result = null;
                            commonEntity.success = false;
                            commonEntity.errMsg = throwable.toString();
                            return commonEntity;
                        })
                        .subscribe(new Consumer<CommonEntity>() {
                            @Override
                            public void accept(CommonEntity commonEntity) throws Exception {
                                if (commonEntity.success){
                                    DataUploadEvent dataUploadEvent = new DataUploadEvent(true);
                                    dataUploadEvent.setSize(size);
                                    getView().uploadSuccess(dataUploadEvent);
                                    deleteYHDataAndZipFile();
                                }else {
                                    getView().uploadFailed(commonEntity.errMsg);
                                }
                            }
                        }, throwable -> getView().uploadFailed(throwable.toString()))
//                        .subscribe(resultEntity -> {
//                            if (resultEntity.mobileMsg == null) {
//                                return;
//                            }
//                            if (resultEntity.mobileMsg.success) {
//                                DataUploadEvent dataUploadEvent = new DataUploadEvent(true);
//                                dataUploadEvent.setSize(size);
//                                getView().uploadSuccess(dataUploadEvent);
//                                deleteYHDataAndZipFile();
//                            } else {
//                                getView().uploadFailed(resultEntity.mobileMsg.errMsg);
//                            }
//                        }, throwable -> getView().uploadFailed(throwable.toString()))
        );
    }

    private void uploadQXZipFile(File uploadZip, int size) {
        mCompositeSubscription.add(
                DataManagerHttpClient.uploadQXZipFile(createZipFileForm(uploadZip), uploadZip.getName())
                        .compose(RxSchedulers.io_main())
                        .onErrorReturn(throwable -> {
                            UploadResultEntity resultEntity = new UploadResultEntity();
                            resultEntity.success = false;
                            resultEntity.errMsg = throwable.toString();
                            resultEntity.module = DataModule.XJ.getModuelName();
                            DataUploadResultEntity dataUploadResultEntity = new DataUploadResultEntity();
                            dataUploadResultEntity.mobileMsg = resultEntity;
                            return dataUploadResultEntity;
                        })
                        .subscribe(resultEntity -> {
                            if (resultEntity.mobileMsg == null) {
                                return;
                            }
                            if (resultEntity.mobileMsg.success) {
                                DataUploadEvent dataUploadEvent = new DataUploadEvent(true);
                                dataUploadEvent.setSize(size);
                                getView().uploadSuccess(dataUploadEvent);
                                deleteQXDataAndZipFile();
                            } else {
                                getView().uploadFailed(resultEntity.mobileMsg.errMsg);
                            }
                        }, throwable -> getView().uploadFailed(throwable.toString()))
        );


    }

    private void uploadXJZipFile(File uploadZip, int size) {
        Api.getInstance().setTimeOut(300);
        mCompositeSubscription.add(
                DataManagerHttpClient.uploadXJZipFile(createZipFileForm(uploadZip), uploadZip.getName())
                        .compose(RxSchedulers.io_main())
                        .onErrorReturn(throwable -> {
                            Api.getInstance().setTimeOut(30);
//                            UploadResultEntity resultEntity = new UploadResultEntity();
//                            resultEntity.success = false;
//                            resultEntity.errMsg = throwable.toString();
//                            resultEntity.module = DataModule.XJ.getModuelName();
//                            DataUploadResultEntity dataUploadResultEntity = new DataUploadResultEntity();
//                            dataUploadResultEntity.mobileMsg = resultEntity;
//                            return dataUploadResultEntity;
                            CommonEntity commonEntity = new CommonEntity();
                            commonEntity.result = null;
                            commonEntity.success = false;
                            commonEntity.errMsg = throwable.toString();
                            return commonEntity;
                        })
                        .subscribe(commonEntity -> {
                            Api.getInstance().setTimeOut(30);
                            if (commonEntity.success) {
                                DataUploadEvent dataUploadEvent = new DataUploadEvent(true);
                                dataUploadEvent.setSize(size);
                                final int finishedSize = EamApplication.dao().getXJPathEntityDao().queryBuilder()
                                        .where(XJPathEntityDao.Properties.State.eq("已检")).list().size();
                                if (finishedSize > 10)
                                    dataUploadEvent.setMsg("仍有" + (finishedSize - 10) + "条巡检数据待上传");
                                getView().uploadSuccess(dataUploadEvent);
                                deleteXJDataAndZipFile();
                            } else {
                                getView().uploadFailed(commonEntity.errMsg);
                            }
                        }, throwable -> getView().uploadFailed(throwable.toString()))
//                        .subscribe(resultEntity -> {
//                            Api.getInstance().setTimeOut(30);
//                            if (resultEntity.mobileMsg == null) {
//                                getView().uploadFailed("巡检数据上传失败！");
//                                return;
//                            }
//                            if (resultEntity.mobileMsg.success) {
//                                DataUploadEvent dataUploadEvent = new DataUploadEvent(true);
//                                dataUploadEvent.setSize(size);
//                                final int finishedSize = EamApplication.dao().getXJPathEntityDao().queryBuilder()
//                                        .where(XJPathEntityDao.Properties.State.eq("已检")).list().size();
//                                if (finishedSize > 10)
//                                    dataUploadEvent.setMsg("仍有" + (finishedSize - 10) + "条巡检数据待上传");
//                                getView().uploadSuccess(dataUploadEvent);
//                                deleteXJDataAndZipFile();
//                            } else {
//                                getView().uploadFailed(resultEntity.mobileMsg.errMsg);
//
//                            }
//                        }, throwable -> getView().uploadFailed(throwable.toString()))
        );

    }

    private void deleteXJDataAndZipFile() {
        String json = Util.getFileFromSD(Constant.XJ_PATH + XJ_UPLOAD_JSON_FILE_NAME);

        if (TextUtils.isEmpty(json)) {
            return;
        }
        XJDownloadListEntity xjDownloadListEntity = GsonUtil.gsonToBean(json, XJDownloadListEntity.class);

        if (xjDownloadListEntity.protrolTasks != null) {

            if (xjDownloadListEntity.protrolTasks.mobileWorkGroups != null && xjDownloadListEntity.protrolTasks.mobileWorkGroups.size() != 0) {
                EamApplication.dao().getXJPathEntityDao().deleteInTx(xjDownloadListEntity.protrolTasks.mobileWorkGroups);
            }

            if (xjDownloadListEntity.protrolTasks.mobileEamWorks != null && xjDownloadListEntity.protrolTasks.mobileEamWorks.size() != 0) {
                EamApplication.dao().getXJAreaEntityDao().deleteInTx(xjDownloadListEntity.protrolTasks.mobileEamWorks);
            }

            if (xjDownloadListEntity.protrolTasks.mobileEamWorkItems != null && xjDownloadListEntity.protrolTasks.mobileEamWorkItems.size() != 0) {
                EamApplication.dao().getXJWorkItemEntityDao().deleteInTx(xjDownloadListEntity.protrolTasks.mobileEamWorkItems);
            }
        }

        FileUtil.deleteFile(Constant.FILE_PATH + XJ_UPLOAD_JSON_FILE_NAME);
        FileUtil.deleteFile(Constant.FILE_PATH + XJ_UPLOAD_ZIP_FILE_NAME);
    }

    private void deleteQXDataAndZipFile() {
        String json = Util.getFileFromSD(Constant.QX_PATH + QX_UPLOAD_JSON_FILE_NAME);

        if (TextUtils.isEmpty(json)) {
            return;
        }
        QXGLUploadListEntity qxDownloadListEntity = GsonUtil.gsonToBean(json, QXGLUploadListEntity.class);

        if (qxDownloadListEntity.faultInfos != null) {

            if (qxDownloadListEntity.faultInfos.size() != 0) {
                EamApplication.dao().getQXGLEntityDao().deleteInTx(qxDownloadListEntity.faultInfos);
            }
        }

        FileUtil.deleteFile(Constant.FILE_PATH + QX_UPLOAD_ZIP_FILE_NAME);
    }

    @SuppressLint("CheckResult")
    private void deleteYHDataAndZipFile() {
        String json = Util.getFileFromSD(Constant.YH_PATH + YH_UPLOAD_JSON_FILE_NAME);
        if (TextUtils.isEmpty(json)) {
            return;
        }
        YHGLUploadListEntity yhglUploadListEntity = GsonUtil.gsonToBean(json, YHGLUploadListEntity.class);
        if (yhglUploadListEntity.faultInfoRecord != null) {
            if (yhglUploadListEntity.faultInfoRecord.size() != 0) {
                Flowable.fromIterable(yhglUploadListEntity.faultInfoRecord)
                        .subscribe(yhEntityDto -> EamApplication.dao().getYHEntityVoDao().deleteByKey(yhEntityDto.getTableId()));
            }
        }
        FileUtil.deleteFile(Constant.FILE_PATH + YH_UPLOAD_ZIP_FILE_NAME);
    }

    @SuppressLint("CheckResult")
    @Override
    public void parseData(DataModule module, String url) {
        Flowable.just(module.name())
                .subscribeOn(Schedulers.newThread())
                .subscribe(this::parse);
    }

    @SuppressLint("CheckResult")
    private void parse(String moduleName) {

        boolean isSuccess = true;
        StringBuilder errMsg = new StringBuilder("解析数据错误\n");
        String json;
        String filePath = null;
        DaoSession daoSession = EamApplication.dao();
        //下载任务数量
        int taskCount = 0;
        StringBuilder taskIdStr = new StringBuilder("");

        switch (moduleName) {

            case "XJ":
                boolean needUpdate = true;
                filePath = Constant.FILE_PATH + "potrolTask.json";
                json = Util.getFileFromSD(filePath);  //上述步骤中将下载的xj_download.zip文件解压后，读取解压出来的文件potrolTask.json
                if (TextUtils.isEmpty(json)) {

                    return;
                }
                LogUtil.d("parse XJ json:" + json);
                XJDownloadListEntity xjDownloadListEntity = null;
                try {
                    xjDownloadListEntity = GsonUtil.gsonToBean(json, XJDownloadListEntity.class); // 若此处PC返回{"protrolTasks": null}  依然会解析成功，需要通过后面的xjDownloadListEntity.protrolTasks判断null的提示失败：巡检数据为null！
                } catch (Exception e) {
                    //这里报错，一般是PC返回的错误信息：{"protrolTasks":"could not execute query"}
                    try {
                        XJDownloadFailEntity xjDownloadFailEntity = GsonUtil.gsonToBean(json, XJDownloadFailEntity.class);
                        FileUtil.deleteFile(filePath);
                        getView().downloadFailed("解析json文件报错：" + xjDownloadFailEntity.protrolTasks);
                        e.printStackTrace();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    return;
                }
                XJDataEntity xjDataEntity = xjDownloadListEntity.protrolTasks;
                LogUtil.d("parse XJ sucess");

                if (xjDataEntity == null) {
                    getView().downloadFailed("巡检数据为null！,请查看服务器日志错误信息");
                    return;
                }

                //TODO... 查询本地数据库判断是否重复insert
                List<XJPathEntity> xjPathEntities = daoSession.getXJPathEntityDao().loadAll();
                //重复任务id
                List<String> repeatIds = new ArrayList<>();
                List<String> localIdList = new ArrayList<>();

                for (XJPathEntity xjPathEntity : xjPathEntities) {
                    localIdList.add(xjPathEntity.id + xjPathEntity.ip);
                }

                if (xjDataEntity.mobileWorkGroups != null && xjDataEntity.mobileWorkGroups.size() != 0) {

                    //任务数量
                    taskCount = xjDataEntity.mobileWorkGroups.size();
                    //任务ID串
                    for (XJPathEntity xjPathEntity : xjDataEntity.mobileWorkGroups) {
                        taskIdStr.append(xjPathEntity.id).append(",");
                    }


                    for (int i = taskCount - 1; i >= 0; i--) {
                        if (localIdList.contains(xjDataEntity.mobileWorkGroups.get(i).id + MBapApp.getIp())) {
                            repeatIds.add(xjDataEntity.mobileWorkGroups.get(i).id + MBapApp.getIp());
                            //删除重复任务
                            xjDataEntity.mobileWorkGroups.remove(i);
                        }
                    }

                    daoSession.getXJPathEntityDao().insertOrReplaceInTx(xjDataEntity.mobileWorkGroups);

                    LogUtil.d("save XJ path data");
                } else {
                    needUpdate = false;
                }


                if (xjDataEntity.mobileEamWorks != null && xjDataEntity.mobileEamWorks.size() != 0) {
                    if (repeatIds.size() > 0) {
                        for (int i = xjDataEntity.mobileEamWorks.size() - 1; i >= 0; i--) {
                            if (localIdList.contains(xjDataEntity.mobileEamWorks.get(i).taskId + MBapApp.getIp())) {
                                xjDataEntity.mobileEamWorks.remove(i);
                            }
                        }
                    }

                    daoSession.getXJAreaEntityDao().insertOrReplaceInTx(xjDataEntity.mobileEamWorks);
                    LogUtil.d("save XJ area data");


                    //巡检指导图片处理
                    downloadInspectionGuidePic(xjDataEntity.mobileEamWorks);


                } else {
                    needUpdate = false;
                }


                if (xjDataEntity.mobileEamWorkItems != null && xjDataEntity.mobileEamWorkItems.size() != 0) {
                    if (repeatIds.size() > 0) {
                        for (int i = xjDataEntity.mobileEamWorkItems.size() - 1; i >= 0; i--) {
                            if (localIdList.contains(xjDataEntity.mobileEamWorkItems.get(i).taskId + MBapApp.getIp())) {
                                xjDataEntity.mobileEamWorkItems.remove(i);
                            }
                        }
                    }

                    //处理设备名称、部位 拼音排序

                    daoSession.getXJWorkItemEntityDao().insertOrReplaceInTx(xjDataEntity.mobileEamWorkItems);
                    LogUtil.d("save XJ xjItem data");
                } else {
                    needUpdate = false;
                }

                if (xjDataEntity.mobileEamItemConc != null && xjDataEntity.mobileEamItemConc.size() > 0) {
                    daoSession.getXJExemptionEntityDao().deleteAll();//删除本地数据，否则PC删除了部分免检后，本地仍会保留
                    daoSession.getXJExemptionEntityDao().insertOrReplaceInTx(xjDataEntity.mobileEamItemConc);
                    LogUtil.d("save XJ xjExemption data");
                }

                if (xjDataEntity.mobileEamHistory != null && xjDataEntity.mobileEamHistory.size() != 0) {
                    daoSession.getXJHistoryEntityDao().insertOrReplaceInTx(xjDataEntity.mobileEamHistory);
                    LogUtil.d("save XJ history data");
                }

                if (needUpdate) {
                    updateTaskStatus(filePath, taskIdStr, taskCount);
                } else {
                    downloadXJSuccess(filePath, 0);
                }

                break;

            case "YH":

                break;
            default:
        }
    }

    @SuppressLint("CheckResult")
    private void downloadInspectionGuidePic(List<XJAreaEntity> xjAreaEntityList) {
        Flowable.just(xjAreaEntityList)
                .subscribeOn(Schedulers.newThread())
                .subscribe(xjAreaEntities -> {
//                                FileUtil.createDir(Constant.XJ_GUIDE_IMGPATH); //创建路径
                    Set<Long> areaIdSet = new HashSet<>();  //为了去重id
                    for (XJAreaEntity xjAreaEntity : xjAreaEntities) {  //判断图片是否存在
                        File file = new File(Constant.XJ_GUIDE_IMGPATH + xjAreaEntity.guideImageName);
                        if (!file.exists()) {
                            //将区域id记录
                            areaIdSet.add(xjAreaEntity.areaId);
                        }
                    }

                    if (areaIdSet.size() > 0) {
                        StringBuilder sb = new StringBuilder("");
                        for (Long id : areaIdSet) {
                            sb.append(id).append(",");
                        }
                        String ids = sb.substring(0, sb.length() - 1);
                        //请求照片 controller 去做
                        new DownloadPicController().downloadXJGuidePic(ids);
                    }

                });
    }

    private void downloadXJSuccess(String filePath, int size) {
        FileUtil.deleteFile(filePath);
        DataParseEvent dataParseEvent = new DataParseEvent(true);
        dataParseEvent.setSize(size);
        getView().parseDataSuccess(dataParseEvent);
    }

    @SuppressLint("CheckResult")
    private void updateTaskStatus(String filePath) {

        StringBuilder taskIdStr = new StringBuilder();
        long staffId = EamApplication.getAccountInfo().staffId;

        List<XJPathEntity> xjPathEntities = EamApplication.dao().getXJPathEntityDao().queryBuilder()
                .where(XJPathEntityDao.Properties.State.eq("待检")).list();
        if (xjPathEntities.size() == 0) {
            downloadXJSuccess(filePath, 0);
            return;
        }

        Flowable.fromIterable(xjPathEntities)
                .subscribe(xjPathEntity -> {
                    if (taskIdStr.length() == 0) {
                        taskIdStr.append(xjPathEntity.id);
                    } else {
                        taskIdStr.append(",");
                        taskIdStr.append(xjPathEntity.id);
                    }
                }, throwable -> {

                }, () -> mCompositeSubscription.add(
                        DataManagerHttpClient.updateStatus(staffId, taskIdStr.toString())
                                .onErrorReturn(throwable -> {
                                    ResultEntity resultEntity = new ResultEntity();
                                    resultEntity.success = false;
                                    resultEntity.errMsg = throwable.toString();
                                    return resultEntity;
                                })
                                .subscribe(resultEntity -> {
                                    if (resultEntity.success) {
                                        downloadXJSuccess(filePath, xjPathEntities.size());
                                    } else {
                                        getView().parseDataFailed(resultEntity.errMsg);
                                    }
                                }, throwable -> {
                                })
                ));


    }

    /**
     * @author zhangwenshuai1
     * @date 2018/4/13
     * @description 更改任务状态
     */
    private void updateTaskStatus(String filePath, StringBuilder taskIdStr, int taskCount) {

        long staffId = EamApplication.getAccountInfo().staffId;

        if (taskCount == 0) {
            downloadXJSuccess(filePath, 0);
            return;
        }
        if (taskIdStr.length() <= 0) {
            return;
        }

        mCompositeSubscription.add(
                DataManagerHttpClient.updateStatus(staffId, taskIdStr.substring(0, taskIdStr.length() - 1))
                        .onErrorReturn(throwable -> {
                            ResultEntity resultEntity = new ResultEntity();
                            resultEntity.success = false;
                            resultEntity.errMsg = throwable.toString();
                            return resultEntity;
                        })
                        .subscribe(resultEntity -> {
                            if (resultEntity.success) {
                                downloadXJSuccess(filePath, taskCount);
                            } else {
                                getView().parseDataFailed(resultEntity.errMsg);
                            }
                        }, throwable -> {
                        })
        );
    }
}
