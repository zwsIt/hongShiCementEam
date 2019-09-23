package com.supcon.mes.module_sbda_online.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda_online.model.bean.RoutineCommonEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/2
 * ------------- Description -------------
 */
@ContractFactory(entites = RoutineCommonEntity.class)
public interface RoutineAPI {
    void getEamOtherInfo(Long eamID);
}
