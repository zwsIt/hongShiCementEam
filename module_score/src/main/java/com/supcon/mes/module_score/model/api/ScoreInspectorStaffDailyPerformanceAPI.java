package com.supcon.mes.module_score.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

import java.util.List;

@ContractFactory(entites = {List.class})
public interface ScoreInspectorStaffDailyPerformanceAPI {

    void getInspectorStaffDailyScore(Long scoreId);

}
