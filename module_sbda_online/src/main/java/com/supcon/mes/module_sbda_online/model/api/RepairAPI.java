package com.supcon.mes.module_sbda_online.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda_online.model.bean.RepairListEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 维修
 */
@ContractFactory(entites = RepairListEntity.class)
public interface RepairAPI {
    void workRecord(Long beamID, int page);
}
