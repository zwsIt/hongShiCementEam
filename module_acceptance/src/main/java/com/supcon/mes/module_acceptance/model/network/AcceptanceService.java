package com.supcon.mes.module_acceptance.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceEditEntity;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceListEntity;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
@ApiFactory(name = "AcceptanceHttpClient")
public interface AcceptanceService {

    //临时验收
    @GET("/BEAM2/checkApply/checkApply/checkApplyList-pending.action")
    Flowable<AcceptanceListEntity> getAcceptanceList(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);


    //临时验收编辑
    @GET("/BEAM2/checkApply/checkApply/getCheckApplyDT.action")
    Flowable<CommonBAPListEntity<AcceptanceEditEntity>> getAcceptanceEdit(@Query("eamId") long eamId);


    //提交
    @POST("/BEAM2/checkApply/checkApply/checkApplyEdit/submit.action?_bapFieldPermissonModelCode_=BEAM2_1.0.0_checkApply_CheckApply&_bapFieldPermissonModelName_=CheckApply&superEdit=false")
    @Multipart
    Flowable<BapResultEntity> doSubmit(@PartMap Map<String, RequestBody> map, @Query("__pc__") String powerCode);
}
