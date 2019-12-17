package com.supcon.mes.module_overhaul_workticket.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresList;
import com.supcon.mes.module_overhaul_workticket.model.bean.WorkTicketList;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@ApiFactory()
public interface ApiService {

    /**
     * 获取检修工作票安全措施list
     * @param workTicketId
     * @return
     */
    @POST("/WorkTicket/workTicket/ohworkticket/data-dg1575615975095.action?datagridCode=WorkTicket_8.20.3.03_workTicket_workTicketEditdg1575615975095&rt=json")
    Flowable<SafetyMeasuresList> listSafetyMeasures(@Query(value = "ohworkticket.id") Long workTicketId);

    /**
     * 检修作业票list
     * @param url
     * @param pageQueryMap
     * @return
     */
    @POST()
    Flowable<WorkTicketList> workTicketList(@Url String url, @QueryMap Map<String, Object> pageQueryMap);
}
