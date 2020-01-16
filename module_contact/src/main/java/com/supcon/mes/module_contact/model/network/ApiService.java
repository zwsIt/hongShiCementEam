package com.supcon.mes.module_contact.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.ContactEntity;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

@ApiFactory(name = "ContactHttpClient")
public interface ApiService {


    @GET("/foundation/staff/list-data.action?&companyId=1000&cusDepartmentDown=yes&cusPositionDown=yes")
    Flowable<CommonBAPListEntity<ContactEntity>> getStaffList(@Query("records.pageNo") int pageNum, @Query("records.pageSize") int pageSize);

}
