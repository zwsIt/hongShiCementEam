package com.supcon.mes.module_main.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_main.model.bean.AnomalyEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.module_main.model.bean.ProcessedEntity;
import com.supcon.mes.module_main.model.bean.ScoreEntity;
import com.supcon.mes.module_main.model.bean.WaitDealtEntity;
import com.supcon.mes.module_main.model.bean.WorkNumEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;

/**
 * Created by Administrator on 2016/3/23.
 */
@ApiFactory(name = "MainClient")
public interface MainService {

    /**
     * 获取所有待办
     *
     * @return
     */
    @GET("/BEAM2/personWork/personworkinfo/personWorkInfoList-query.action")
    Flowable<CommonBAPListEntity<WaitDealtEntity>> getWaitDealt(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * 工作提醒(新)
     * @param fastQueryCondEntity
     * @param pageQueryMap
     * @return
     */
    @GET("/BEAM2/personWork/allPersonWorkInfo/allPsWorkInfoList-query.action?1=1&permissionCode=BEAM2_1.0.0_personWork_allPsWorkInfoList")
    Flowable<CommonBAPListEntity<WaitDealtEntity>> getWaitDealtNew(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);


    /**
     * 委托
     *
     * @return
     */
    @GET("/ec/workflow/proxyPendingResult.action")
    Flowable<BapResultEntity> proxyPending(@QueryMap Map<String, Object> pageQueryMap);

    //档案查看:只查询当前登录人的设备，mobileFlag = 1 表示来自移动端
    @GET("/BEAM/baseInfo/baseInfo/baseInfoPartForview-query.action")
    Flowable<CommonBAPListEntity<EamEntity>> getEams(@Query("staffID") String staffID, @Query("mobileFlag") String mobileFlag, @QueryMap Map<String, Object> pageQueryMap);


    //个人评分
    @GET("/BEAM2/patrolWorkerScore/workerScoreHead/getPersonScoreInfo.action")
    Flowable<CommonEntity<ScoreEntity>> getPersonScore(@Query("staffID") String staffID);

    //设备评分
    @GET("/BEAM2/patrolWorkerScore/workerScoreHead/getEamScoreInfo.action")
    Flowable<CommonEntity<String>> getEamScore(@Query("deviceID ") long deviceID);

    //首页待办数量
    @GET("/BEAM2/patrolWorkerScore/workerScoreHead/getMainButtonWorkCount.action")
    Flowable<CommonBAPListEntity<WorkNumEntity>> getMainWorkCount(@Query("staffID") String staffID);

    //提示信息
    @GET("/BEAM2/patrolWorkerScore/workerScoreHead/getSloganInfo.action")
    Flowable<CommonEntity<String>> getSloganInfo();


    //已处理的
    @GET("/BEAM2/personWork/processFlowInfo/processFlowInfoList-query.action?1=1&permissionCode=BEAM2_1.0.0_personWork_processFlowInfoList")
    Flowable<CommonBAPListEntity<ProcessedEntity>> workflowHandleList(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * 获取我的流程，即已处理的(新)
     * @param fastQueryCondEntity
     * @return
     */
    @POST("/BEAM2/personWork/allProcessInfo/allProFlowInfoList-query.action?1=1&permissionCode=BEAM2_1.0.0_personWork_allProFlowInfoList")
    Flowable<CommonBAPListEntity<ProcessedEntity>> workflowHandleListNew(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);



    //批量派单
    @GET("/BEAM2/workList/workRecord/bulkSubmitWorkAndFault.action")
    Flowable<ResultEntity> bulkSubmitCustom(@QueryMap Map<String, Object> queryMap);

    /**
     * 异常记录
     * @param fastQueryCondEntity
     * @param pageQueryMap
     * @return
     */
    @POST("/BEAM2/personWork/allAbnormalInfoOfEam/allAbnormalInfoList-query.action?1=1&permissionCode=BEAM2_1.0.0_personWork_allAbnormalInfoList")
    Flowable<CommonBAPListEntity<AnomalyEntity>> getAnomalyList(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * 获取处理意见
     * @param url
     * @param tableInfoId 表单id
     */
    @GET("{url}/dealInfo-list.action")
    Flowable<List> getDealInfoList(@Path(value = "url",encoded = true) String url, @Query("tableInfoId") Long tableInfoId);

    @GET("/BEAM2/personWork/allAbnormalInfoOfEam/getPendingId.action")
    Flowable<CommonEntity> getPendingId(@Query("currentUserId") Long currentUserId, @Query("tableNo") String tableNo);
}
