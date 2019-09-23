package com.supcon.mes.module_warn.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_warn.model.bean.DelayEntity;

import java.util.Map;


@ContractFactory(entites = DelayEntity.class)
public interface DelayAPI {
    void delayDate(Map<String, Object> param);
}
