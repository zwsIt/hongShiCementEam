package com.supcon.mes.module_sbda_online.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda_online.model.bean.SparePartListEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 备件
 */
@ContractFactory(entites = SparePartListEntity.class)
public interface SpareAPI {
    void spareRecord(Long eamID);
}
