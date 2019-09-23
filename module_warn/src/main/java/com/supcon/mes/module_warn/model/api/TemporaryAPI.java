package com.supcon.mes.module_warn.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.Map;

@ContractFactory(entites = {CommonBAPListEntity.class})
public interface TemporaryAPI {
    void getTempLubrications(Map<String, Object> params, Map<String, Object> pageQueryParams);
}
