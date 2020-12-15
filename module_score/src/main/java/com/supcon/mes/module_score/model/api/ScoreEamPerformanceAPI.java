package com.supcon.mes.module_score.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceListEntity;

import java.util.List;

@ContractFactory(entites = {ScoreEamPerformanceListEntity.class})
public interface ScoreEamPerformanceAPI {
    void getScorPerformance(Long eamId, Long scoreId);
}
