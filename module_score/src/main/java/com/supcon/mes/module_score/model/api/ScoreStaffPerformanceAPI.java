package com.supcon.mes.module_score.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceListEntity;

import java.util.List;

@ContractFactory(entites = {ScoreStaffPerformanceListEntity.class, ScoreDutyEamEntity.class})
public interface ScoreStaffPerformanceAPI {

    void getStaffScore(Long staffId,Long tableId);

    void getDutyEam(Long staffId, String scoreType);

}
