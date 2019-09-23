package com.supcon.mes.module_score.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;

import java.util.Map;

@ContractFactory(entites = {BapResultEntity.class})
public interface ScoreStaffSubmitAPI {
    void doStaffSubmit(String url, Map<String, Object> map);
}
