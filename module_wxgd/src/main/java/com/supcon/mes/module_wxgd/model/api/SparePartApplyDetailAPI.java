package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.SparePartReceiveListEntity;

@ContractFactory(entites = {SparePartReceiveListEntity.class})
public interface SparePartApplyDetailAPI {
    void listSparePartApplyDetail(Long id);
}
