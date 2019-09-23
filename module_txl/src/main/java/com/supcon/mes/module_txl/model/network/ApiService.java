package com.supcon.mes.module_txl.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.TxlListEntity;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

@ApiFactory(name = "TxlHttpClient")
public interface ApiService {

    /**192.168.90.9:8080/foundation/staff/list-data.action?
     1=1
     &staff.code= # 员工编码
     &staff.name= # 员工姓名
     &department.name= # 部门名称
     &position.name= #
     &companyId=1000 # 公司id
     &cusDepartmentDown=yes
     &cusPositionDown=yes
     &pageSize=20
     &&records.pageSize=20
     &records.maxPageSize=500
     **/
//    @GET("/foundation/staff/list-data.action?&staff.code=&staff.name=&department.name=&position.name=&companyId=1000&cusDepartmentDown=yes&cusPositionDown=yes&pageSize=20&&records.pageSize=20&records.maxPageSize=500")
//    Flowable<TxlListEntity> getStaffList(@Query("records.pageNo") int pageNum);
    @GET("/foundation/staff/list-data.action?&companyId=1000&cusDepartmentDown=yes&cusPositionDown=yes&pageSize=20&&records.pageSize=20&records.maxPageSize=500")
    Flowable<TxlListEntity> getStaffList(@Query("records.pageNo") int pageNum,@Query("staff.code")String staffCode,@Query("staff.name")String staffName, @Query("position.name")String positionname);
    
}
