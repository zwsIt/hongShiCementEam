package com.supcon.mes.module_overhaul_workticket.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.PageParamUtil;
import com.supcon.mes.module_overhaul_workticket.model.bean.WorkTicketList;
import com.supcon.mes.module_overhaul_workticket.model.contract.WorkTicketListContract;
import com.supcon.mes.module_overhaul_workticket.model.network.HttpClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class WorkTicketListPresenter extends WorkTicketListContract.Presenter {

    @Override
    public void listWorkTickets(int pageNo, Map<String, Object> queryParams, boolean pendingQuery) {
        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
        if (queryParams.containsKey(Constant.BAPQuery.RISK_ASSESSMENT)){
            Map<String, Object> map = new HashMap<>();
            map.put(Constant.BAPQuery.RISK_ASSESSMENT,queryParams.get(Constant.BAPQuery.RISK_ASSESSMENT));
            fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(map);
        }

        if(queryParams.containsKey(Constant.BAPQuery.EAM_CODE)){
            Map<String, Object> map = new HashMap<>();
            map.put(Constant.BAPQuery.EAM_CODE,queryParams.get(Constant.BAPQuery.EAM_CODE));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(map,"EAM_BaseInfo,EAM_ID,WORKTICKET_OHWORKTICKETS,EAM_ID");
            if (fastQueryCondEntity.subconds == null){
                fastQueryCondEntity.subconds = new ArrayList<>();
            }
            fastQueryCondEntity.subconds.add(joinSubcondEntity);
        }
        fastQueryCondEntity.modelAlias = "ohworkticket";

        Map<String, Object> pageQueryParam = PageParamUtil.pageQueryParam(pageNo,20);
        pageQueryParam.put("fastQueryCond",fastQueryCondEntity);
        String url;
        if (pendingQuery){
            url = "/WorkTicket/workTicket/ohworkticket/workTicketList-pending.action?1=1&permissionCode=WorkTicket_8.20.3.03_workTicket_workTicketList";
        }else {
            url = "/WorkTicket/workTicket/ohworkticket/workTicketList-query.action?1=1&permissionCode=WorkTicket_8.20.3.03_workTicket_workTicketList";
        }

        mCompositeSubscription.add(
                HttpClient.workTicketList(url,pageQueryParam)
                        .onErrorReturn(new Function<Throwable, WorkTicketList>() {
                            @Override
                            public WorkTicketList apply(Throwable throwable) throws Exception {
                                WorkTicketList workTicketList = new WorkTicketList();
                                workTicketList.errMsg = throwable.toString();
                                return workTicketList;
                            }
                        })
                        .subscribe(new Consumer<WorkTicketList>() {
                            @Override
                            public void accept(WorkTicketList workTicketList) throws Exception {
                                if (TextUtils.isEmpty(workTicketList.errMsg)){
                                    getView().listWorkTicketsSuccess(workTicketList);
                                }else {
                                    getView().listWorkTicketsFailed(workTicketList.errMsg);
                                }
                            }
                        })
        );
    }
}
