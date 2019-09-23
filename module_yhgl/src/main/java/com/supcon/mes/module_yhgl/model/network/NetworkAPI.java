package com.supcon.mes.module_yhgl.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckListEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_yhgl.model.bean.LubricateOilsListEntity;
import com.supcon.mes.module_yhgl.model.bean.MaintenanceListEntity;
import com.supcon.mes.module_yhgl.model.bean.RepairStaffListEntity;
import com.supcon.mes.module_yhgl.model.bean.SparePartListEntity;
import com.supcon.mes.module_yhgl.model.bean.StandingCropResultEntity;
import com.supcon.mes.module_yhgl.model.bean.YHListEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2016/3/23.
 */
@ApiFactory(name = "YHGLHttpClient")
public interface NetworkAPI {

    /**
     * 查询隐患管理待办
     *
     * @return
     */
    @GET("/BEAM2/faultInfo/faultInfo/faultInfoList-pending.action?processKey=faultInfoFW")
    Flowable<YHListEntity> faultInfoListPending(@QueryMap Map<String, Object> queryParam, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity);

    /**
     * 查询隐患管理全部
     *
     * @return
     */
    @GET("/BEAM2/faultInfo/faultInfo/faultInfoList-query.action?processKey=faultInfoFW")
    Flowable<YHListEntity> faultInfoList(@QueryMap Map<String, Object> queryParam, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity);


    /**
     * 审批提交隐患单
     *
     * @param map
     * @return
     */
    @POST("/BEAM2/faultInfo/faultInfo/faultInfoView/submit.action?__pc__=dGFzazUyMHxmYXVsdEluZm9GVw__")
    Flowable<BapResultEntity> viewSubmit(@QueryMap Map<String, Object> map);

    /**
     * 编辑提交隐患单
     *
     * @param map
     * @return
     */
    @POST("/BEAM2/faultInfo/faultInfo/faultInfoEdit/submit.action?__pc__=dGFzazM0MnxmYXVsdEluZm9GVw__")
    @Multipart
    Flowable<BapResultEntity> editSubmit(@QueryMap Map<String, Object> map, @Part List<MultipartBody.Part> partList);


    /**
     * 查询隐患管理待办
     *
     * @return
     */
    @GET("/services/public/BEAM2/faultInfo/getFaultInfos/{pageNo}/{pageSize}?securityKey=securityKey&securityId=restfulKey&returnType=json")
    Flowable<YHListEntity> faultInfoList(@Path("pageNo") int pageNo, @Path("pageSize") int pageSize);

    /**
     * @param
     * @return
     * @description 隐患管理维修人员
     * @author wnagshizhan 2018/8/28
     */
    @GET("/BEAM2/faultInfo/faultInfo/data-dg1557402325583.action")
    Flowable<RepairStaffListEntity> listRepairStaffs(@Query("faultInfo.id") long id);

    /**
     * @param
     * @return
     * @description 隐患管理备件
     * @author wnagshizhan 2018/8/28
     */
    @GET("/BEAM2/faultInfo/faultInfo/data-dg1557402465409.action")
    Flowable<SparePartListEntity> listSpareParts(@Query("faultInfo.id") long id);

    /**
     * @param
     * @return
     * @description 隐患管理润滑油
     * @author wnagshizhan 2018/8/28
     */
    @GET("/BEAM2/faultInfo/faultInfo/data-dg1557455440578.action")
    Flowable<LubricateOilsListEntity> listLubricateOils(@Query("faultInfo.id") long id);

    /**
     * @param
     * @return
     * @description 隐患管理维保
     * @author wnagshizhan 2018/8/28
     */
    @GET("/BEAM2/faultInfo/faultInfo/data-dg1557457043896.action")
    Flowable<MaintenanceListEntity> listMaintenance(@Query("faultInfo.id") long id);

    /**
     * @param
     * @return
     * @description 获取维修工单验收
     * @author wnagshizhan 2018/8/28
     */
    @GET("/BEAM2/workList/workRecord/data-dg1531695961597.action")
    Flowable<AcceptanceCheckListEntity> listAcceptances(@Query("workRecord.id") long id);

    /**
     * @param productCode 备件编码
     * @return
     * @description 备件更新现存量
     * @author zhangwenshuai1 2018/10/10
     */
    @POST("/BEAM2/workList/workRecord/getNowSum.action")
    Flowable<CommonListEntity<StandingCropResultEntity>> updateStandingCrop(@Query("productCode") String productCode);

    /**
     * @param
     * @return
     * @description 备件生成领用出库单
     * @author zhangwenshuai1 2018/10/23
     */
    @POST("/BEAM2/workList/sparePart/generateSparePartApply.action")
    Flowable<ResultEntity> generateSparePartApply(@Query("sparePartJsons") String listStr);

}
