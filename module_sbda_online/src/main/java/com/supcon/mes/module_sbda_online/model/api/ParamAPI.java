package com.supcon.mes.module_sbda_online.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda_online.model.bean.ParamListEntity;

@ContractFactory(entites = ParamListEntity.class)
public interface ParamAPI {
    void getEamParam(Long beamID);
}
