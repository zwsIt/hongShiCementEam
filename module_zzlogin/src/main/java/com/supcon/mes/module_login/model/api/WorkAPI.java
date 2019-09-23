package com.supcon.mes.module_login.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_login.model.bean.WorkEntity;
import com.supcon.mes.module_login.model.bean.WorkNumEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wangshizhan on 2017/12/28.
 * Email:wangshizhan@supcon.com
 */
@ContractFactory(entites = {WorkEntity.class, Map.class})
public interface WorkAPI {
    void getAllPendings(String staffName);  //获取所有待办


    void getPendingsByModule(List<String> pendingParams);  //获取所有待办
}
