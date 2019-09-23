package com.supcon.mes.module_score.model.api;

import com.app.annotation.apt.ContractFactory;

import java.util.List;

@ContractFactory(entites = {List.class})
public interface ScoreEamPerformanceAPI {
    void getScorPerformance(int scoreId);
}
