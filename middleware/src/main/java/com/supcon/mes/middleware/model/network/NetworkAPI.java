package com.supcon.mes.middleware.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.AreaListEntity;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.ContractListEntity;
import com.supcon.mes.middleware.model.bean.DepartmentInfoListEntity;
import com.supcon.mes.middleware.model.bean.DeviceDCSEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.LinkListEntity;
import com.supcon.mes.middleware.model.bean.LongResultEntity;
import com.supcon.mes.middleware.model.bean.LubricateListEntity;
import com.supcon.mes.middleware.model.bean.MyInfo;
import com.supcon.mes.middleware.model.bean.RefLubricateListEntity;
import com.supcon.mes.middleware.model.bean.RefMaintainListEntity;
import com.supcon.mes.middleware.model.bean.RefProductListEntity;
import com.supcon.mes.middleware.model.bean.RepairGroupListEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.RoleListEntity;
import com.supcon.mes.middleware.model.bean.SparePartRefListEntity;
import com.supcon.mes.middleware.model.bean.StaffDetailInfoListEntity;
import com.supcon.mes.middleware.model.bean.StandingCropEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeListEntity;
import com.supcon.mes.middleware.model.bean.TxlListEntity;
import com.supcon.mes.middleware.model.bean.UserInfoListEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.WorkCountEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowListEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by wangshizhan on 2018/7/10
 * Email:wangshizhan@supcom.com
 */
@ApiFactory(name = "MiddlewareHttpClient")
public interface NetworkAPI {

    @GET("/foundation/staff/list-data.action?&staff.code=&staff.name=&department.name=&position.name=&companyId=1000&cusDepartmentDown=yes&cusPositionDown=yes&pageSize=100000&&records.pageSize=100000&records.maxPageSize=500")
    Flowable<TxlListEntity> getStaffList(@Query("records.pageNo") int pageNum);

    /**
     * 用户列表
     */
//    @GET("/foundation/user/queryList.action")
    @GET("/foundation/user/common/getDepartmentUserList.action")
    Flowable<UserInfoListEntity> queryUserInfo(@Query("user.name") String userName);

    /**
     * 用户信息全
     */
//    @GET("/foundation/user/queryList.action")
    @GET("/mobile/mobileCommon/foundation/getCurrentUserinfo.action")
    Flowable<MyInfo> getCurrentUserinfo();

    /**
     * 用户列表
     */
//    @GET("/foundation/user/queryList.action?pageSize=500&&page.pageSize=500&page.maxPageSize=500")
    @GET("/foundation/user/common/userRefList.action?userPage.pageSize=500&pageOrder=DESC")
    Flowable<UserInfoListEntity> queryUserInfoList(@Query("staff.name") String staffName, @Query("userPage.pageNo") int pageNo);

    /**
     * 获取系统编码
     */
    @GET("/foundation/systemCode/codeValueManager/valueList.action?systemCodePage.pageSize=100&systemCodePage.maxPageSize=500")
    Flowable<SystemCodeListEntity> querySystemCode(@Query("systemEntityCode") String systemEntityCode);


    /**
     * 获取工作流
     */
    @GET("/mobile/mobileCommon/workflow/findWorkFlowVar.action")
    Flowable<WorkFlowListEntity> findWorkFlow(@Query("pendingId") long pendingId);

    /**
     * 获取角色
     */
//    @GET("/foundation/role/roleUserList.action?roleUserPage.pageSize=500&roleUserPage.maxPageSize=500")
    @GET("/foundation/role/common/roleUserList.action?roleUserPage.pageSize=500&roleUserPage.maxPageSize=500")
    Flowable<RoleListEntity> queryRoleUserList(@Query("user.name") String userName);

    /**
     * 获取员工详细信息
     */
//    @GET("/foundation/staff/list-data.action?pageSize=20&&records.pageSize=20&records.maxPageSize=500")
    @GET("/foundation/systemCode/systemCodeJson.action?pageSize=20&&records.pageSize=20&records.maxPageSize=500")
    Flowable<StaffDetailInfoListEntity> queryStaffDetailInfo(@Query("staff.code") String staffCode, @Query("companyId") long companyId);

    /**
     * 下载头像
     *
     * @param id 图片id
     * @return
     */
    @GET("/foundation/staff/image-staff.action")
    Flowable<ResponseBody> downloadStaffPic(@Query("id") long id);

    /**
     * 上传附件
     *
     * @param partList 附件分包
     * @return
     */
    @POST("/foundation/workbench/uploadFile.action?__file_upload=true")
    @Multipart
    Flowable<String> uploadFile(@Part List<MultipartBody.Part> partList);

    /**
     * 删除附件
     *
     * @param ids2del 附件 id
     * @return
     */
    @POST("/foundation/workbench/file-delete.action")
    Flowable<BapResultEntity> deleteFile(@Query("ids2del") long ids2del);

    /**
     * 下载附件
     *
     * @param id 附件id
     * @return
     */
    @GET("/foundation/workbench/download.action")
    Flowable<ResponseBody> downloadFile(@Query("id") long id, @Query("entityCode") String entityCode);//BEAM2_1.0.0_faultInfo

    /**
     * 获取附件信息
     *
     * @param tableId 单据id
     * @return
     */
    @GET("/BEAM2/mobile/faultInfo/listAttachFilesPaths.action")
    Flowable<CommonEntity> listAttachFilesPaths(@Query("tableId") long tableId);

    /**
     * 获取附件信息
     *
     * @param tableInfoId 单据id
     * @return
     */
    @GET("/mobile/mobileCommon/common/listAttachFiles.action")
    Flowable<AttachmentListEntity> listAttachFiles(@Query("tableInfoId") long tableInfoId);

    /**
     * 获取区域位置
     *
     * @return
     */
    @GET("/BEAM/area/area/areaNoCheckListRef-query.action?page.pageSize=500")
    Flowable<AreaListEntity> listArea();

    /**
     * 获取维修组
     *
     * @return
     */
    @GET("/BEAM/repairGroup/repairGroup/groupList-query.action?page.pageSize=500")
    Flowable<RepairGroupListEntity> listWXGroup();

    /**
     * 获取新建单据工作流
     *
     * @param flowKey 工作流名字
     * @return
     */
    @GET("/mobile/mobileCommon/workflow/getStartTransition.action")
    Flowable<LinkListEntity> getStartTransition(@Query("flowKey") String flowKey);


    /**
     * 获取deploymentId
     *
     * @param flowKey 工作流名字
     * @return
     */
    @GET("/mobile/mobileCommon/workflow/getDeploymentId.action")
    Flowable<LongResultEntity> getDeploymentId(@Query("flowKey") String flowKey);

    /**
     * 获取制定PowerCode
     *
     * @param deploymentId
     */
    @GET("/ec/workflow/getStartActivePowerCode.action")
    Flowable<BapResultEntity> getStartActivePowerCode(@Query("deploymentId") long deploymentId);


    /**
     * 检查用户是否有发起该工作流的权限
     *
     * @param userName   用户名
     * @param processKey 工作流名字
     * @return
     */
    @GET("/mobile/mobileCommon/workflow/judgeFlowPermission.action")
    Flowable<LongResultEntity> checkModulePermission(@Query("userName") String userName, @Query("processKey") String processKey);

    /**
     * 获取单据当前工作流
     *
     * @param pendingId 待办id
     * @return
     */
    @GET("/mobile/mobileCommon/workflow/getCurrentActTransition.action")
    Flowable<LinkListEntity> getCurrentActTransition(@Query("pendingId") long pendingId);

    /**
     * 获取用户，包含没有用户名的员工
     *
     * @return
     */
    @GET("/foundation/staff/common/getDepartmentWorkList.action?departmentWorkPage.pageSize=500&pageOrder=DESC")
    Flowable<UserInfoListEntity> listCommonContractStaff(@Query("staff.name") String staffName, @Query("departmentWorkPage.pageNo") int pageNo);

    /**
     * 获取通讯录
     *
     * @param staffName
     * @param pageNo
     * @return
     */
    @GET("/foundation/addressBook/list-data.action?records.pageSize=20&records.maxPageSize=500")
    Flowable<ContractListEntity> listContract(@Query("addressBookContent") String staffName, @Query("records.pageNo") int pageNo);

    /**
     * 获取部门列表
     *
     * @return
     */
    @GET("/foundation/department/queryList.action?companyId=1000&page.pageSize=500&&page.maxPageSize=500")
    Flowable<DepartmentInfoListEntity> listDepartment(@Query("page.pageNo") int pageNo);


    /**
     * 获取departmentInfo列表
     */
    @GET("/foundation/department/queryList.action?a=-1&departmentCode=&departmentName=&departmentId=&managerId=&managerName=&companyId=1000&page.pageSize=500&&page.maxPageSize=500")
    Flowable<DepartmentInfoListEntity> listDepartmentInfo();


    /**
     * 发送device token
     */
    @POST("/BEAM/loginAndLogout.action")
    Flowable<CommonEntity> sendDeviceToken(@Query("deviceToken") String deviceToken, @Query("loginStatus") String loginStatus, @Query("clientType") String clientType);


    /**
     * 查询隐患管理待办
     *
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
     * @description 获取备件物品列表
     * @author zhangwenshuai1 2018/8/13
     */
    @GET("/MESBasic/product/product/refProduct_sp-query.action?&permissionCode=MESBasic_1_product_refProductLayout_sp&crossCompanyFlag=false")
    Flowable<RefProductListEntity> listRefProduct(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * @param
     * @return
     * @description 获取润滑列表
     * @author zhangwenshuai1 2018/8/13
     */
    @GET("/BEAM/lubricateOil/lubricateOil/oilRef-query.action?&permissionCode=BEAM_1.0.0_lubricateOil_oilRef&crossCompanyFlag=false")
    Flowable<LubricateListEntity> listLubricate(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * @param
     * @return
     * @description 获取润滑参照列表
     * @author zhangwenshuai1 2018/8/13
     */
    @GET("/BEAM/baseInfo/jWXItem/lubricateBeamRef-query.action?&permissionCode=BEAM_1.0.0_lubricateOil_oilRef&crossCompanyFlag=false")
    Flowable<RefLubricateListEntity> listRefLubricate(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);


    /**
     * @param
     * @return
     * @description 获取维保参照列表
     * @author zhangwenshuai1 2018/8/13
     */
    @GET("/BEAM/baseInfo/jWXItem/maintainBeamRef-query.action?&permissionCode=BEAM_1.0.0_baseInfo_maintainBeamRef&crossCompanyFlag=")
    Flowable<RefMaintainListEntity> listRefMaintain(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);


    /**
     * 获取DCS设备数据
     */
    @GET("/BEAM/baseInfo/baseInfo/getMeasParam.action")
    Flowable<CommonListEntity<DeviceDCSEntity>> getMeasParam(@Query("eamId") long eamId);

    /**
     * 获取设备
     */
    @GET("/BEAM/baseInfo/baseInfo/baseInfoPartForview-query.action")
    Flowable<CommonListEntity<EamType>> getEam(@Query("advQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);


    @GET("/BEAM/baseInfo/baseInfo/getSupOSStandingCrop.action")
    Flowable<CommonListEntity<StandingCropEntity>> updateStandingCrop(@Query("sparePartCodes") String sparePartCodes);

    //获取工单,隐患数量
    @GET
    Flowable<CommonListEntity<WorkCountEntity>> getWorkCount(@Url String url, @QueryMap Map<String, Object> queryMap);

    /**
     * 更新隐患单
     */
    @GET("/BEAM2/faultInfo/faultInfo/closeWorkAndSaveReason.action")
    Flowable<ResultEntity> closeWorkAndSaveReason(@Query("id") long id, @Query("closeReason") String reason);

    /**
     * 获取菜单权限
     */
    @GET("/foundation/userPermission/checkUserPower.action")
    Flowable<Object> checkUserPower(@Query("companyId") long companyId, @Query("menuOperateCodes") String menuOperateCodes);
}
