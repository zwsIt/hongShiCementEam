package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_wxgd.model.bean.GenerateAcceptanceEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/2
 * ------------- Description -------------
 */
@ContractFactory(entites = GenerateAcceptanceEntity.class)
public interface GenerateAcceptanceAPI {

    void generateCheckApply(long workOrderId, long eamId, String describe, long installId);

}
