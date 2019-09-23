package com.supcon.mes.module_data_manage.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_data_manage.model.bean.AlllDeviceResultEntity;
import com.supcon.mes.module_data_manage.model.bean.DataUploadResultEntity;
import com.supcon.mes.module_data_manage.model.bean.EamInfoEntity;
import com.supcon.mes.module_data_manage.model.bean.XJBasicInfoEntity;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/3/23.
 */
@ApiFactory(name = "DataManagerHttpClient")
public interface ApiService {

    /**
     * 下载隐患压缩包
     *
     * @return
     */
    @GET("/HSE/rectification/rectification/getRectificationInfo.action")
    Flowable<ResponseBody> downloadZipFile(@Query("module") String module, @Query("staffId") long staffId);

    /**
     * 上传隐患压缩包
     *
     * @param file    压缩包body
     * @param zipFile 压缩包名字
     */
    @Multipart
    @POST("/HSE/rectification/rectification/submitRectifitation.action")
    Flowable<DataUploadResultEntity> uploadZipFile(@Part List<MultipartBody.Part> file, @Query("zipFile") String zipFile);


    /**
     * 查询用户信息
     *
     * @return
     */
    @GET("/BEAM2/mobile/common/getBasicInfo.action")
    Flowable<EamInfoEntity> getBasicInfo();

    /**
     * 查询用户信息,巡检，安环
     *
     * @return
     */
    @GET("/mobileEAM/MobileInterfaceForAndroidAction/getBasicInfo.action")
    Flowable<XJBasicInfoEntity> getXJBasicInfo();


    /**
     * 查询用户信息, 设备
     *
     * @return
     */
    @GET("/BEAM2/mobile/common/getBasicInfo.action")
    Flowable<EamInfoEntity> getEamBasicInfo();

    /**
     * 获得所有设备列表
     *
     * @return
     */
    @GET("/BEAM2/mobile/common/listPowerBaseInfo.action")
    Flowable<AlllDeviceResultEntity> listPowerBaseInfo(@Query("id") String moduleId);

    /**
     * 获得所有设备列表
     *
     * @return
     */
    @GET("/BEAM2/mobile/common/listAllDevice.action")
    Flowable<AlllDeviceResultEntity> listAllDevice(@Query("id") String moduleId);

    /**
     * 下载巡检压缩包
     *
     * @param staffId 用户id
     * @return
     */
    @GET("/mobileEAM/MobileInterfaceForAndroidAction/dataDownloadZip.action")
    Flowable<ResponseBody> downloadXJZipFile(@Query("staffId") long staffId);


    /**
     * 更新巡检任务状态，下载解析完成时调用
     *
     * @param staffId   用户id
     * @param taskIdStr 下载完成的巡检任务id
     * @return
     */
    @GET("/public/potrolTaskNew/potrolTaskWF/updateStatus.action")
    Flowable<ResultEntity> updateStatus(@Query("staffId") long staffId, @Query("taskIdStr") String taskIdStr);

    /**
     * 上传巡检压缩包
     *
     * @param file    压缩包body
     * @param zipFile 压缩包名字
     */
    @Multipart
    @POST("/mobileEAM/MobileInterfaceForAndroidAction/submitPotrolTask.action")
    Flowable<CommonEntity> uploadXJZipFile(@Part List<MultipartBody.Part> file, @Query("zipFile") String zipFile);

    @Multipart
    @POST("/BEAM2/faultInfo/mobile/submitFaultRecord.action")
    Flowable<DataUploadResultEntity> uploadQXZipFile(@Part List<MultipartBody.Part> file, @Query("zipFile") String zipFile);

    /**
     * @description 隐患上传
     * @param 
     * @return  
     * @author zhangwenshuai1 2019/1/11
     *
     */
    @Multipart
    @POST("/submitRectifitation.action")
    Flowable<CommonEntity> uploadYHZipFile(@Part List<MultipartBody.Part> file, @Query("zipFile") String zipFile);

    /**
     * @description 巡检指导图片下载
     * @param
     * @return  
     * @author zhangwenshuai1 2019/1/9
     *
     */
    @POST("/mobileEAM/MobileInterfaceForAndroidAction/imageDownloadZip.action")
    Flowable<ResponseBody> downloadXJGuidePicZipFile(@Query("eamWorksIds") String areaIds);
}
