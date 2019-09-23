package com.supcon.mes.module_sbda_online.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda_online.model.bean.LubriListEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 润滑维保
 */
@ContractFactory(entites = LubriListEntity.class)
public interface LubriAPI {
    void lubriRecord(Long beamID);
}
