package com.supcon.mes.module_sbda_online.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda_online.model.bean.SparePartsLedgerListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/3
 * ------------- Description -------------
 */
@ContractFactory(entites = SparePartsLedgerListEntity.class)
public interface SparePartsLedgerAPI {
    void baseInfoProduct(Map<String, Object> params, Long productID, int page);
}
