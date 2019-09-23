package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskListEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

@ContractFactory(entites = {List.class, List.class})
public interface OLXJTaskAPI {
    /**
     * 查询在线巡检任务列表
     * @param queryParam  startEndTime   起止时间
     */
    void getOJXJTaskList(Map<String, Object> queryParam);


    /**
     * 查询最近一条已领取的巡检任务
     * @param queryParam 时间 任务状态
     */
    void getOJXJLastTaskList(Map<String, Object> queryParam);
}
