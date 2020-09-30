package com.supcon.mes.module_acceptance.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.List;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
@ContractFactory(entites = CommonBAPListEntity.class)
public interface AcceptanceEditAPI {
    void getAcceptanceEdit(Long eamId, Long tableId);
}
