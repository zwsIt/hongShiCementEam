package com.supcon.mes.module_acceptance.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
@ContractFactory(entites = AcceptanceListEntity.class)
public interface AcceptanceListAPI {
    void getAcceptanceList(Map<String, Object> param, int page);
}
