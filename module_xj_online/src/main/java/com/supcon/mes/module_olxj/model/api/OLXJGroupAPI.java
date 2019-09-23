package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

/**
 * Created by wangshizhan on 2019/3/29
 * Email:wangshizhan@supcom.com
 */

@ContractFactory(entites = {CommonBAPListEntity.class, CommonBAPListEntity.class})
public interface OLXJGroupAPI {

    /**
     * 获取巡检线路列表
     */
     void queryGroupList();

    /**
     * 获取已经有巡检任务的线路列表
     */
    void queryTaskGroupList();

}
