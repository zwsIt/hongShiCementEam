package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;

import java.util.Map;

@ContractFactory(entites = {CommonEntity.class, ResultEntity.class})
public interface OLXJEamTaskAPI {

    /**
     * 创建临时任务
     * @param paramMap
     */
    void createTempPotrolTaskByEam(Map<String, Object> paramMap);

    /**
     * 修改临时任务状态：valid = 1
     * @param taskID
     */
    void updateTaskById(long taskID);

}
