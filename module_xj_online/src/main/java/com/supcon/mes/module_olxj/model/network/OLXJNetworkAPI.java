package com.supcon.mes.module_olxj.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_olxj.model.bean.AbnormalEntity;
import com.supcon.mes.module_olxj.model.bean.EamXJEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJExemptionEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJGroupEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJStatisticsEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by wangshizhan on 2019/3/29
 * Email:wangshizhan@supcom.com
 */
@ApiFactory(name = "OLXJClient")
public interface OLXJNetworkAPI {

    /**
     * 获取路线列表
     *
     * @return
     */
    @GET("/mobileEAM/workGroup/workGroup/workGroupList-query.action?page.pageSize=500&page.maxPageSize=500&page.pageNo=1")
    Flowable<CommonBAPListEntity<OLXJGroupEntity>> queryWorkGroupList(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity);


    /**
     * 获取已下发任务列表
     *
     * @param queryParam          页数相关key
     * @param fastQueryCondEntity 已下发快速查询条件
     * @return
     */
    @GET("/mobileEAM/potrolTaskNew/potrolTaskWF/potrolTaskList-query.action?1=1&permissionCode=mobileEAM_1.0.0_potrolTaskNew_potrolTaskList")
    Flowable<CommonBAPListEntity<OLXJTaskEntity>> queryPotrolTaskList(@QueryMap Map<String, Object> queryParam, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity);

    /**
     * 获取已下发临时任务列表
     *
     * @param queryParam          页数相关key
     * @param fastQueryCondEntity
     * @return
     */
    @GET("/mobileEAM/potrolTaskNew/potrolTaskWF/tempList-query.action?1=1&permissionCode=mobileEAM_1.0.0_potrolTaskNew_tempList&page.pageSize=500&page.maxPageSize=500&page.pageNo=1")
    Flowable<CommonBAPListEntity<OLXJTaskEntity>> queryPotrolTempTaskList(@QueryMap Map<String, Object> queryParam, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity);


    /**
     * 获取任务包含的作业项
     *
     * @param queryParam 页数相关key
     * @return
     */
    @GET("/mobileEAM/potrolTaskNew/potrolTaskWF/data-dg1488776891029.action?datagridCode=mobileEAM_1.0.0_potrolTaskNew_tempViewdg1489026162123&rt=json")
    Flowable<CommonBAPListEntity<OLXJWorkItemEntity>> queryWorkList(@QueryMap Map<String, Object> queryParam);


    /**
     * 1、添加临时巡检任务作业项参照列表
     * 2、路线查询区域和作业项
     */
    @GET("/mobileEAM/work/workItem/addworkItem-query.action?&permissionCode=mobileEAM_1.0.0_work_addworkItem&crossCompanyFlag=")
//    @GET("/mobileEAM/work/workItem/addworkItem-query.action?&permissionCode=mobileEAM_1.0.0_work_addworkItem&crossCompanyFlag=&page.fileName=%E5%B7%A1%E6%A3%80%E9%A1%B9_%E5%B7%A1%E6%A3%80%E9%A1%B9.xls&cookie_Last_query_type=LAST_QUERY_ec_mobileEAM_work_workItem_addworkItem_queryForm&cookie_expand_type=EXPAND_TYPE_ec_mobileEAM_work_workItem_addworkItem_queryForm&cookie_expand_type_value=all&old_expand_type_value=all&queryParam=MESBasic_1_equipManage_Equipment_code_eamID_code")
    Flowable<CommonBAPListEntity<OLXJWorkItemEntity>> queryWorkListRef(@QueryMap Map<String, Object> queryParam);

    /**
     * 创建临时任务
     *
     * @return
     */
    @Multipart
    @POST("/mobileEAM/potrolTaskNew/potrolTaskWF/tempEdit/submit.action?__pc__=c3RhcnQzMTF0ZW1wV0Z8dGVtcFdG&_bapFieldPermissonModelCode_=mobileEAM_1.0.0_potrolTaskNew_PotrolTaskWF&_bapFieldPermissonModelName_=PotrolTaskWF&superEdit=false")
    Flowable<BapResultEntity> createTempTask(@PartMap Map<String, RequestBody> requestBodyMap);

    /**
     * 创建临时任务
     *
     * @return
     */
    @GET("/mobileEAM/MobileInterfaceForAndroidAction/createTempPotrolTask.action")
    Flowable<CommonEntity<String>> createTempTaskNew(@QueryMap Map<String, Object> map);

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
     * 批量取消任务
     *
     * @param taskIDs
     * @param changeState 1、未下发 LinkState/01 2、已下发 LinkState/02 3、已完成 LinkState/03 4、已取消 LinkState/04
     * @return
     */
    @GET("/mobileEAM/potrolTaskNew/potrolTaskWF/cancelTask.action")
    Flowable<ResultEntity> cancelTask(@Query("taskIDs") String taskIDs, @Query("changeState") String changeState);

    /**
     * 结束/终止任务
     *
     * @param taskIDs
     * @param endReason
     * @param isFinish
     * @return
     */
    @GET("/mobileEAM/MobileInterfaceForAndroidAction/endTask.action")
    Flowable<ResultEntity> endTask(@Query("taskIDs") String taskIDs, @Query("endReason") String endReason, @Query("type") boolean isFinish);

    /**
     * 获取区域列表
     */
    @GET("/mobileEAM/work/work/workPart-query.action?valueType=mobileEAM001/02&&permissionCode=mobileEAM_1.0.0_work_workLayout&page.pageSize=20&page.maxPageSize=500")
    Flowable<CommonBAPListEntity<OLXJAreaEntity>> queryOLXJArea(@Query("workGroupID") long workGroupID, @Query("page.pageNo") int pageNo);

    /**
     * 获取区域隐患信息
     */
    @GET("/BEAM2/patrolWorkerScore/workerScoreHead/getAbnormalInspectTaskPart.action")
    Flowable<CommonListEntity<AbnormalEntity>> getAbnormalInspectTaskPart(@Query("workGroupId") long workGroupID, @Query("isTemp") int isTemp);

    /**
     * 区域上传数据
     *
     * @param file    压缩包body
     * @param zipFile 压缩包名字
     */
    @Multipart
    @POST("/mobileEAM/MobileInterfaceForAndroidAction/submitPotrolTaskByWork.action")
    Flowable<ResultEntity> submitPotrolTaskByWork(@Part List<MultipartBody.Part> file, @Query("zipFile") String zipFile);

    /**
     * 免检项
     */
    @GET("/mobileEAM/work/workItem/data-dg1491444225573.action?dgPage.pageSize=65536")
    Flowable<CommonBAPListEntity<OLXJExemptionEntity>> getExemptionEam(@Query("workItem.id") long workItemId);

    /**
     * 巡检统计
     */
    @GET("/BEAM2/patrolWorkerScore/workerScoreHead/getInspectStaticsInfo.action")
    Flowable<CommonListEntity<OLXJStatisticsEntity>> getInspectStaticsInfo(@QueryMap Map<String, Object> queryParam);


    //设备创建巡检任务
    @GET("/mobileEAM/MobileInterfaceForAndroidAction/createTempPotrolTaskByEam.action")
    Flowable<CommonEntity<EamXJEntity>> createTempPotrolTaskByEam(@QueryMap Map<String, Object> paramMap);


    //生成任务
    @GET("/mobileEAM/potrolTaskNew/potrolTaskWF/updateTaskById.action")
    Flowable<ResultEntity> updateTaskById(@Query("taskID") long taskID);
}
