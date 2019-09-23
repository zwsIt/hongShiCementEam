package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;

import java.util.Map;

@ContractFactory(entites = {CommonEntity.class, ResultEntity.class})
public interface OLXJEamTaskAPI {

    void createTempPotrolTaskByEam(Map<String, Object> paramMap);

    void updateTaskById(long taskID);

}
