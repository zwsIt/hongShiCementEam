package com.supcon.mes.module_warn.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_warn.model.bean.DailyLubricateRecordEntity;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskListEntity;
import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.bean.DelayRecordListEntity;
import com.supcon.mes.module_warn.model.bean.LubricationWarnListEntity;
import com.supcon.mes.module_warn.model.bean.MaintenanceWarnListEntity;
import com.supcon.mes.module_warn.model.bean.SparePartWarnListEntity;
import com.supcon.mes.module_warn.model.bean.TemLubricateTaskEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * Created by Administrator on 2016/3/23.
 */
@ApiFactory(name = "EarlyWarnHttpClient")
public interface ApiService {

    // 维保
    @GET
    Flowable<MaintenanceWarnListEntity> getMaintenance(@Url String url, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    //润滑
    @GET
    Flowable<LubricationWarnListEntity> getLubrication(@Url String url, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    //备件
    @GET
    Flowable<SparePartWarnListEntity> getSparePart(@Url String url, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    //完成
    @GET("/BEAM/baseInfo/jWXItem/jwxDailyLubRecord.action")
    Flowable<DelayEntity> dailyComplete(@QueryMap Map<String, Object> pageQueryMap);


    //延期
    @GET("/BEAM/baseInfo/jWXItem/jwxDelaySetting.action")
    Flowable<DelayEntity> delayDate(@QueryMap Map<String, Object> pageQueryMap);

    //延期记录
    @GET
    Flowable<DelayRecordListEntity> delayRecords(@Url String url, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * 派单
     */
    @POST("/BEAM2/workList/workRecord/generateWork.action")
    Flowable<DelayEntity> generateWork(@QueryMap Map<String, Object> pageQueryMap);


    //日常润滑（红狮定制视图）
    @GET("/BEAM/baseInfo/jWXItem/data-dg1558678704208.action") // 设备档案模块--日常润滑到期(移动端)，菜单显示：预防性维护（时间频率页签），条件：日常润滑=true
    Flowable<DailyLubricateTaskListEntity> getLubrications(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    //领取润滑任务
    @GET("/BEAM/baseInfo/jWXItem/ReceiveLubTask.action")
    Flowable<DelayEntity> receiveTask(@QueryMap Map<String, Object> pageQueryMap);

    //临时润滑
    @GET("/BEAM/baseInfo/jWXItem/data-dg1560391643861.action") // 设备档案模块--临时润滑任务(移动端)，菜单显示：预防性维护（时间频率页签），条件：日常润滑=true
    Flowable<CommonBAPListEntity<TemLubricateTaskEntity>> getTempLubrications(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    /**
     *  已完成日常润滑记录
     * @param fastQueryCondEntity
     * @param pageQueryMap
     * @return
     */
    @POST("/BEAM/baseInfo/dailyLubricateRecor/dailyLubricateTask-query.action?1=1&permissionCode=BEAM_1.0.0_baseInfo_dailyLubricateTask")
    Flowable<CommonBAPListEntity<DailyLubricateRecordEntity>> dailyLubricateTaskQuery(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);
}
