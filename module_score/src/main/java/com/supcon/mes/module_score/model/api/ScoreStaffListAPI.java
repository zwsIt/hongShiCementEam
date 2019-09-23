package com.supcon.mes.module_score.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamListEntity;

import java.util.Map;

@ContractFactory(entites = {CommonBAPListEntity.class})
public interface ScoreStaffListAPI {
    void patrolScore(String url, Map<String, Object> param, int page);
}
