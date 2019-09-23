package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

import java.util.Map;

@ContractFactory(entites = {CommonListEntity.class})
public interface OLXJStatisticsAPI {
    void getInspectStaticsInfo(Map<String, Object> queryParam);
}
