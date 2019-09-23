package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.RepairGroupListEntity;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = RepairGroupListEntity.class)
public interface RepairGroupQueryAPI {

    void queryRepairGroup();

}
