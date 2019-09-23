package com.supcon.mes.module_sbda_online.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda_online.model.bean.SubsidiaryListEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 附属
 */
@ContractFactory(entites = SubsidiaryListEntity.class)
public interface SubsidiaryAPI {
    void attachPart(Long beamID);
}
