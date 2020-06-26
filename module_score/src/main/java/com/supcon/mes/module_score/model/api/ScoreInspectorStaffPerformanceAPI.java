package com.supcon.mes.module_score.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;

import java.util.List;

@ContractFactory(entites = {List.class, ScoreDutyEamEntity.class})
public interface ScoreInspectorStaffPerformanceAPI {

    void getInspectorStaffScore(Long staffId,Long scoreId);

    void getDutyEam(long staffId, String scoreType);

}
