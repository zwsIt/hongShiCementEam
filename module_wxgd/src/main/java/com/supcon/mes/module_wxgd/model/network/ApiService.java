package com.supcon.mes.module_wxgd.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.SparePartReceiveListEntity;
import com.supcon.mes.middleware.model.bean.StandingCropEntity;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckListEntity;
import com.supcon.mes.module_wxgd.model.bean.GenerateAcceptanceEntity;
import com.supcon.mes.module_wxgd.model.bean.LubricateOilsListEntity;
import com.supcon.mes.module_wxgd.model.bean.MaintenanceListEntity;
import com.supcon.mes.module_wxgd.model.bean.RepairStaffListEntity;
import com.supcon.mes.module_wxgd.model.bean.SparePartApplyHeaderInfoEntity;
import com.supcon.mes.module_wxgd.model.bean.SparePartListEntity;
import com.supcon.mes.module_wxgd.model.bean.SparePartRefListEntity;
import com.supcon.mes.module_wxgd.model.bean.SparePartsConsumeEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;

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
import retrofit2.http.Url;

/**
 * ApiService  网络接口
 * created by zhangwenshuai1 2018/8/8
 */
@ApiFactory(name = "HttpClient")
public interface ApiService {

    /**
     * @param
     * @return
     * @description 获取工单列表
     * @author zhangwenshuai1 2018/8/13
     */
    @GET
    Flowable<WXGDListEntity> listWxgds(@Url String url, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * 维修工单接单提交
     *
     * @param requestBodyMap 表单
     * @return
     */
    @POST("/BEAM2/workList/workRecord/workReceiptEdit/submit.action?__pc__=dGFzazQ1N3x3b3Jr")
    @Multipart
    Flowable<BapResultEntity> receiveSubmit(@PartMap Map<String, RequestBody> requestBodyMap);

    /**
     * 维修工单执行提交
     *
     * @param map
     * @return
     */
    @POST("/BEAM2/workList/workRecord/workExecuteEdit/submit.action?__pc__=dGFzazE4Mzh8d29yaw__")
    @Multipart
    Flowable<BapResultEntity> executeSubmit(@PartMap Map<String, RequestBody> map);


    /**
     * @param
     * @return
     * @description 获取维修工单维修人员
     * @author wnagshizhan 2018/8/28
     */
    @GET("/BEAM2/workList/workRecord/data-dg1531695961519.action")
    Flowable<RepairStaffListEntity> listRepairStaffs(@Query("workRecord.id") long id);

    /**
     * @param
     * @return
     * @description 获取维修工单备件
     * @author wnagshizhan 2018/8/28
     */
    @GET("/BEAM2/workList/workRecord/data-dg1531695961378.action")
    Flowable<SparePartListEntity> listSpareParts(@Query("workRecord.id") long id);


    /**
     * @param
     * @return
     * @description 获取维修工单润滑油
     * @author wnagshizhan 2018/8/28
     */
    @GET("/BEAM2/workList/workRecord/data-dg1531695961550.action")
    Flowable<LubricateOilsListEntity> listLubricateOils(@Query("workRecord.id") long id);


    /**
     * @param
     * @return
     * @description 获取维修工单验收
     * @author wnagshizhan 2018/8/28
     */
    @GET("/BEAM2/workList/workRecord/data-dg1531695961597.action")
    Flowable<AcceptanceCheckListEntity> listAcceptances(@Query("workRecord.id") long id);

    /**
     * @param faultInfoId 隐患单ID  repairType 维修类型
     * @return
     * @description 转化为大修或检修
     * @author zhangwenshuai1 2018/9/3
     */
    @POST("/BEAM2/workList/workRecord/overhaul.action")
    Flowable<ResultEntity> translateRepair(@Query("faultInfoId") long faultInfoId, @Query("repairType") String repairType);


    /**
     * @param
     * @return
     * @description 派工
     * @author zhangwenshuai1 2018/9/11
     */
    @POST("/BEAM2/workList/workRecord/workEdit/submit.action?__pc__=dGFzazMzOHx3b3Jr&_bapFieldPermissonModelCode_=BEAM2_1.0.0_workList_WorkRecord&_bapFieldPermissonModelName_=WorkRecord&superEdit=false")
    @Multipart
    Flowable<BapResultEntity> doSubmitDispatch(@PartMap Map<String, RequestBody> map);

    /**
     * @param
     * @return
     * @description 预警派工
     * @author zhangwenshuai1 2018/9/11
     */
    @POST("/BEAM2/workList/workRecord/workEdit/submit.action?__pc__=c3RhcnQzMTB3b3JrfHdvcms_&_bapFieldPermissonModelCode_=BEAM2_1.0.0_workList_WorkRecord&_bapFieldPermissonModelName_=WorkRecord&superEdit=false")
    @Multipart
    Flowable<BapResultEntity> doSubmitDispatchWarn(@PartMap Map<String, RequestBody> map);


    /**
     * 维修工单暂停和激活
     *
     * @param map
     * @return
     */
    @GET("/BEAM2/workList/workRecord/stopOrActivate.action")
    Flowable<CommonListEntity> stopOrActivate(@QueryMap Map<String, Object> map);

    /**
     * @param
     * @return
     * @description 验收
     * @author zhangwenshuai1 2018/9/11
     */
    @POST("/BEAM2/workList/workRecord/workCheckEdit/submit.action?__pc__=dGFzazE5ODd8d29yaw__&_bapFieldPermissonModelCode_=BEAM2_1.0.0_workList_WorkRecord&_bapFieldPermissonModelName_=WorkRecord&superEdit=false")
    @Multipart
    Flowable<BapResultEntity> doAcceptChk(@PartMap Map<String, RequestBody> map, @Part List<MultipartBody.Part> partList);

    /**
     * @param productCode 备件编码
     * @return
     * @description 备件更新现存量
     * @author zhangwenshuai1 2018/10/10
     */
    @POST("/BEAM2/workList/workRecord/getNowSum.action")
    Flowable<CommonListEntity<StandingCropEntity>> updateStandingCrop(@Query("productCode") String productCode);

    /**
     * @param
     * @return
     * @description 备件参照列表查询
     * @author zhangwenshuai1 2018/10/23
     */
    @POST("/BEAM/baseInfo/sparePart/sparePartRef-query.action?&permissio.0.0_baseInfo_sparePartRef&crossCompanyFlag=false")
    Flowable<SparePartRefListEntity> listSparePartsRef(@QueryMap Map<String, Object> queryMap, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity);

    /**
     * @param
     * @return
     * @description 备件生成领用出库单
     * @author zhangwenshuai1 2018/10/23
     */
    @POST("/BEAM2/workList/sparePart/generateSparePartApply.action")
    Flowable<ResultEntity> generateSparePartApply(@Query("sparePartJsons") String listStr);

    /**
     * @param
     * @return
     * @description 隐患管理维保
     * @author wnagshizhan 2018/8/28
     */
    @GET("/BEAM2/workList/workRecord/data-dg1557994493235.action")
    Flowable<MaintenanceListEntity> listMaintenance(@Query("workRecord.id") long id);

    /**
     * @param
     * @return
     * @description 生成验收单
     * @author wnagshizhan 2018/8/28
     */
    @GET("/BEAM2/checkApply/checkApply/generateCheckApply.action")
    Flowable<GenerateAcceptanceEntity> generateCheckApply(@Query("workOrderId") long workOrderId, @Query("eamId") long eamId, @Query("describe") String describe, @Query("installId") long installId);


    /**
     * @param
     * @return
     * @description 备件领用
     * @author zhangwenshuai1 2018/9/11
     */
    @POST("/BEAM2/sparePart/apply/sparePartEdit/submit.action?__pc__=dGFzazM0MHxzcGFyZVBhcnRBcHBseQ__&_bapFieldPermissonModelCode_=BEAM2_1.0.0_sparePart_Apply&_bapFieldPermissonModelName_=Apply&superEdit=false")
    @Multipart
    Flowable<BapResultEntity> doSubmitSparePart(@PartMap Map<String, RequestBody> map);

    /**
     * @param
     * @return
     * @description 备件消耗台账
     * @author zhangwenshuai1 2018/9/11
     */
    @GET("/BEAM2/workList/sparePart/productConsumeList-query.action")
    Flowable<CommonBAPListEntity<SparePartsConsumeEntity>> productConsumeList(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * @param
     * @return
     * @description 备件领用记录
     * @author zhangwenshuai1 2018/9/11
     */
    @GET("/BEAM2/workList/sparePart/sparePartList-query.action?1=1&permissionCode=BEAM2_1.0.0_workList_sparePartList")
    Flowable<CommonBAPListEntity<SparePartsConsumeEntity>> sparePartList(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * 获取备件领用申请单表头信息
     * @param id
     * @param includes
     * @return
     */
    @POST("/BEAM2/sparePart/apply/get.action")
    @Multipart
    Flowable<SparePartApplyHeaderInfoEntity> get(@Query("id") Long id, @Part("includes") String includes );

    /**
     * @param
     * @return
     * @description 备件领用申请PT明细
     * @author zws 2018/8/28
     */
    @POST("/BEAM2/sparePart/apply/data-dg1535960092493.action")
    Flowable<SparePartReceiveListEntity> listSparePartApplyDetail(@Query("apply.id") long id);

    /**
     * @param 
     * @return 备件领用申请：领用人申请（海螺）
     * @description 
     * @author zws 2019/9/28
     */
    @POST("/BEAM2/sparePart/apply/sparePartEdit/submit.action?__pc__=dGFzazM0MHxzcGFyZVBhcnRBcHBseQ__&_bapFieldPermissonModelCode_=BEAM2_1.0.0_sparePart_Apply&_bapFieldPermissonModelName_=Apply&superEdit=false")
    @Multipart
    Flowable<BapResultEntity> sparePartApplyDoSubmit(@PartMap Map<String, RequestBody> paramMap,@Part List<MultipartBody.Part> partList);
}
