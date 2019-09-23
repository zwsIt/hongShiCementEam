package com.supcon.mes.module_score.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_score.model.bean.ScoreEamListEntity;

import java.util.Map;

@ContractFactory(entites = {ScoreEamListEntity.class})
public interface ScoreEamListAPI {
    void getScoreList(Map<String, Object> param,int page);
}
