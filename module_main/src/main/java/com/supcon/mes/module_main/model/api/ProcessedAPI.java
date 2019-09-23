package com.supcon.mes.module_main.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 */
@ContractFactory(entites = {CommonBAPListEntity.class})
public interface ProcessedAPI {

    void workflowHandleList(Map<String, Object> queryParam,int page);
}
