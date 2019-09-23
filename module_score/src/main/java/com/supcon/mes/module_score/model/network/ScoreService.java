package com.supcon.mes.module_score.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_score.model.bean.ScoreDeviceCheckResultEntity;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamListEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceListEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffDailyPerformanceEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceListEntity;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

@ApiFactory(name = "ScoreHttpClient")
public interface ScoreService {

    //评分列表
    @GET("/BEAM/scorePerformance/scoreHead/beamScoreList-query.action")
    Flowable<ScoreEamListEntity> getScoreList(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    //评分
    @GET
    Flowable<ScoreEamPerformanceListEntity> getScore(@Url String url, @Query("scoreHead.id") int scoreId);


    //提交
    @POST("/BEAM/scorePerformance/scoreHead/beamPerformanceEdit/submit.action?__pc__=YmVhbVNjb3JlTGlzdF9hZGRfYWRkX0JFQU1fMS4wLjBfc2NvcmVQZXJmb3JtYW5jZV9iZWFtU2NvcmVMaXN0fA__&_bapFieldPermissonModelCode_=BEAM_1.0.0_scorePerformance_ScoreHead&_bapFieldPermissonModelName_=ScoreHead")
    @Multipart
    Flowable<BapResultEntity> doSubmit(@PartMap Map<String, RequestBody> map);

    //人员评分列表
    @GET
    Flowable<CommonBAPListEntity<ScoreStaffEntity>> patrolScore(@Url String url, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    //巡检工人员评分
    @GET
    Flowable<ScoreStaffPerformanceListEntity> getInspectorStaffScore(@Url String url, @Query("workerScoreHead.id") int scoreId);

    //巡检工每日人员评分
    @GET
    Flowable<CommonBAPListEntity<ScoreStaffDailyPerformanceEntity>> getInspectorStaffDailyScore(@Url String url, @Query("workerScoreHead.id") int scoreId);

    //机修工人员评分
    @GET
    Flowable<ScoreStaffPerformanceListEntity> getMechanicStaffScore(@Url String url, @Query("workerScoreHead.id") int scoreId);

    //人员设备评分
    @GET("/BEAM/patrolWorkerScore/workerScoreHead/getResponsityBeam.action")
    Flowable<CommonListEntity<ScoreDutyEamEntity>> getDutyEam(@Query("staffId") long staffId, @Query("scoreType") String scoreType);

    //提交
    @POST
    @Multipart
    Flowable<BapResultEntity> doStaffSubmit(@Url String url, @PartMap Map<String, RequestBody> map);


    @GET("/BEAM/scorePerformance/scoreHead/checkIsDeal.action")
    Flowable<ScoreDeviceCheckResultEntity> doCheckDevice(@Query("eamId") long eamId, @Query("dateString") String date);
}
