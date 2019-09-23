package com.supcon.mes.module_sbda_online.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_sbda_online.model.bean.StopPoliceListEntity;

import java.util.Map;

@ContractFactory(entites = {StopPoliceListEntity.class, ResultEntity.class})
public interface StopPoliceAPI {
    void runningGatherList(Map<String, Object> params, int page);
    void updateStopPoliceItem(Map<String, String> params);
}
