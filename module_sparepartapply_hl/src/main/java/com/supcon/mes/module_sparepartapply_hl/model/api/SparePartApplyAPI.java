package com.supcon.mes.module_sparepartapply_hl.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;

import java.util.Map;

/**
 * 备件领用
 */
@ContractFactory(entites = {BapResultEntity.class})
public interface SparePartApplyAPI {
    void submitSparePartApply(Map<String, Object> map,Map<String, Object> attachmentMap,String pc);
}
