package com.supcon.mes.module_hs_tsd.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_hs_tsd.model.bean.EleOffOnTemplate;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnListEntity;
import com.supcon.mes.module_hs_tsd.model.bean.OperateItemListEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/27
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@ApiFactory()
public interface ApiService {

    /**
     * 获取停电工作票安全措施list
     * @param tableId
     * @return
     */
    @POST("/BEAMEle/onOrOff/onoroff/data-dg1545361488690.action?datagridCode=BEAMEle_1.0.0_onOrOff_eleOffEditdg1545361488690&rt=json")
    Flowable<OperateItemListEntity> listOperateItem(@Query(value = "onoroff.id") Long tableId);

    /**
     * 停电作业票list
     * @param url
     * @param pageQueryMap
     * @return
     */
    @POST()
    Flowable<ElectricityOffOnListEntity> eleOffList(@Url String url, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * 停电作业票submit
     * @param paramsMap
     * @param __pc__
     * @return
     */
    @POST("/BEAMEle/onOrOff/onoroff/{view}/submit.action?_bapFieldPermissonModelCode_=BEAMEle_1.0.0_onOrOff_Onoroff&_bapFieldPermissonModelName_=Onoroff&superEdit=false")
    @Multipart
    Flowable<BapResultEntity> submit(@Path(value = "view") String view, @PartMap Map<String, RequestBody> paramsMap, @Part List<MultipartBody.Part> partList, @Query("__pc__") String __pc__);

    /**
     * 获取停电模板页面
     * @return
     */
    @POST("/BEAMEle/template/onoroffTemplate/eleTemplateRef-query.action?&permissionCode=BEAMEle_1.0.0_template_eleTemplateRef&crossCompanyFlag=false")
    Flowable<CommonBAPListEntity<EleOffOnTemplate>> eleOffTemplateRef(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * 获取送电模板页面
     * @return
     */
    @POST("/BEAMEle/template/onoroffTemplate/eleOnTemplateRef-query.action?&permissionCode=BEAMEle_1.0.0_template_eleOnTemplateRef&crossCompanyFlag=false")
    Flowable<CommonBAPListEntity<EleOffOnTemplate>> eleOnTemplateRef(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

}
