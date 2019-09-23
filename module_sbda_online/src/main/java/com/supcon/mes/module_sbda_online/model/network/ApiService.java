package com.supcon.mes.module_sbda_online.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_sbda_online.model.bean.LubriListEntity;
import com.supcon.mes.module_sbda_online.model.bean.MaintenanceListEntity;
import com.supcon.mes.module_sbda_online.model.bean.ParamListEntity;
import com.supcon.mes.module_sbda_online.model.bean.RepairListEntity;
import com.supcon.mes.module_sbda_online.model.bean.RoutineCommonEntity;
import com.supcon.mes.module_sbda_online.model.bean.SBDAOnlineListEntity;
import com.supcon.mes.module_sbda_online.model.bean.ScreenListEntity;
import com.supcon.mes.module_sbda_online.model.bean.SparePartListEntity;
import com.supcon.mes.module_sbda_online.model.bean.SparePartsLedgerListEntity;
import com.supcon.mes.module_sbda_online.model.bean.StopPoliceListEntity;
import com.supcon.mes.module_sbda_online.model.bean.SubsidiaryListEntity;

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

/**
 * Created by Administrator on 2016/3/23.
 */
@ApiFactory(name = "SBDAOnlineHttpClient")
public interface ApiService {


    //档案查看
    @GET("/BEAM/baseInfo/baseInfo/baseInfoPartForview-query.action")
    Flowable<SBDAOnlineListEntity> getPartForview(@Query("advQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    //附属
    @GET("/BEAM/baseInfo/baseInfo/data-dg1461551623906.action")
    Flowable<SubsidiaryListEntity> attachPart(@Query("baseInfo.id") Long beamID);

    //备件
    @GET("/BEAM/baseInfo/baseInfo/data-dg1461551624000.action")
    Flowable<SparePartListEntity> spareRecord(@Query("baseInfo.id") Long beamID);

    //润滑
    @GET("/BEAM/baseInfo/baseInfo/data-dg1461553165559.action")
    Flowable<LubriListEntity> lubriRecord(@Query("baseInfo.id") Long beamID);

    //维保
    @GET("/BEAM/baseInfo/baseInfo/data-dg1461561957963.action")
    Flowable<MaintenanceListEntity> maintenanceRecord(@Query("baseInfo.id") Long beamID);

    //维修
    @GET("/BEAM2/workList/workRecord/data-dg1543885029358.action")
    Flowable<RepairListEntity> workRecord(@Query("beamID") Long beamID, @QueryMap Map<String, Object> pageQueryMap);

    //设备类型
    @GET
    Flowable<ScreenListEntity> screenPart(@Url String url);

    //设备类型
    @GET("/BEAM/baseInfo/baseInfo/getEamOtherInfo.action")
    Flowable<RoutineCommonEntity> getEamOtherInfo(@Query("eamID") Long eamID);

    //备件台账
    @GET("/BEAM/baseInfo/baseInfo/baseInfoProduct-query.action")
    Flowable<SparePartsLedgerListEntity> baseInfoProduct(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @Query("productID") Long productID, @QueryMap Map<String, Object> pageQueryMap);

    //查看设备
    @POST("/BEAM2/runningGather/runningGathers/runningGatherList-query.action")
    @Multipart
    Flowable<StopPoliceListEntity> runningGatherList(
            @PartMap Map<String, RequestBody> map,
            @QueryMap Map<String, Object> pageQueryMap);

    //技术参数
    @GET("/BEAM/baseInfo/baseInfo/data-dg1461551857214.action")
    Flowable<ParamListEntity> getEamParam(@QueryMap Map<String, Object> pageQueryMap);

    @GET("/BEAM2/runningState/runningState/setRunningRecord.action")
    Flowable<ResultEntity> setRunningRecord(@QueryMap Map<String, String> pageQueryMap);

    //查看设备
    @POST("/BEAM2/runningGather/runningGathers/gatherMobileList-query.action?")
    Flowable<StopPoliceListEntity> gatherMobileList(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);


}
