package com.supcon.mes.module_warn.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_warn.model.bean.DelayRecordListEntity;

import java.util.Map;

@ContractFactory(entites = DelayRecordListEntity.class)
public interface DelayRecordAPI {
    void delayRecords(String url,Map<String, Object> params);
}
