package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.SparePartReceiveListEntity;

@ContractFactory(entites = {SparePartReceiveListEntity.class})
public interface SparePartApplyDetailAPI {
    /**
     * @param
     * @return
     * @description 获取备件领用申请明细list
     * @author user 2019/10/29
     */
    void listSparePartApplyDetail(Long id);
}
