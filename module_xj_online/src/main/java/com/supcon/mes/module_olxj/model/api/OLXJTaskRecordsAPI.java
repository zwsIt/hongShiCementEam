package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

@ContractFactory(entites = {CommonBAPListEntity.class,CommonBAPListEntity.class})
public interface OLXJTaskRecordsAPI {
    /**
     * 查询在线巡检任务列表
     * @param queryParam  startEndTime   起止时间
     */
    void getOJXJTaskList(int pageIndex,Map<String, Object> queryParam);

    /**
     * 查询在线巡检区域列表
     * @param taskIDs   任务id
     */
    void getOJXJAreaList(Long taskIDs);

}
