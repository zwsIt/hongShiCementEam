package com.supcon.mes.push.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wangshizhan on 2018/7/10
 * Email:wangshizhan@supcom.com
 */
@ApiFactory(name = "PushHttpClient")
public interface NetworkAPI {


    /**
     * 发送device token
     */
    @POST("/BEAM/loginAndLogout.action")
    Flowable<CommonEntity> sendDeviceToken(@Query("deviceToken") String deviceToken, @Query("loginStatus") String loginStatus, @Query("clientType") String clientType);


    /**
     * 查询隐患管理待办
     * @return
     */
    @GET("/BEAM2/faultInfo/faultInfo/faultInfoList-pending.action?processKey=faultInfoFW")
    Flowable<CommonBAPListEntity<YHEntity>> queryFaultInfotPending(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity);


    /**
     * @param
     * @return
     * @description 获取工单列表
     * @author zhangwenshuai1 2018/8/13
     */
    @GET("/BEAM2/workList/workRecord/workList-pending.action?1=1&permissionCode=BEAM2_1.0.0_workList_workList")
    Flowable<CommonBAPListEntity<WXGDEntity>> queryWXGDPending(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity);


    /**
     * 获取隐患处理意见
     * @param tableInfoId 表单id
     */
    @GET("/BEAM2/{module}/{table}/dealInfo-list.action")
    Flowable<List> getDealInfoList(@Path("module") String moduleName, @Path("table") String tableName, @Query("tableInfoId") long tableInfoId);




}
