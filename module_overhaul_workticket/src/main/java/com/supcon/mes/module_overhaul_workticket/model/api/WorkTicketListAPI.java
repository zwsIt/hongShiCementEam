package com.supcon.mes.module_overhaul_workticket.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresList;
import com.supcon.mes.module_overhaul_workticket.model.bean.WorkTicketList;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc 检修作业票list获取
 */
@ContractFactory(entites = {WorkTicketList.class})
public interface WorkTicketListAPI {
    void listWorkTickets(int pageNo, Map<String, Object> queryParams,boolean pendingQuery);
}
