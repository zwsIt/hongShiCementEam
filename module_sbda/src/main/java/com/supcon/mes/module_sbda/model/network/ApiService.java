package com.supcon.mes.module_sbda.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.module_sbda.model.bean.MyDeviceListEntity;
import com.supcon.mes.module_sbda.model.bean.SearchDeviceEntity;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/3/23.
 */
@ApiFactory(name = "SBDAHttpClient")
public interface ApiService {

    /**
     * 模糊搜索设备
     * @param queryParam
     * @return
     */
    @GET("/BEAM2/runningState/mobile/searchDevice.action")
    Flowable<SearchDeviceEntity> searchDevice(@Query("queryParam") String queryParam);

    /**
     * 获得有登录人权限的设备列表
     * @return
     */
    @GET("/BEAM2/runningState/mobile/getPoweredEam.action")
    Flowable<MyDeviceListEntity> getPoweredEam();


}
