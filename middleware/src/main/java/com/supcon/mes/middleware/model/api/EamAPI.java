package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

import java.util.Map;

@ContractFactory(entites = CommonListEntity.class)
public interface EamAPI {
    void getEam(Map<String, Object> params, int page);
}
