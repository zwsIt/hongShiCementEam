package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;

import java.util.List;

@ContractFactory(entites = List.class)
public interface OLXJExemptionAPI {
    void getExemptionEam(List<OLXJWorkItemEntity> olxjWorkItemEntities);
}
