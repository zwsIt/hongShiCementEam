package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/8/13
 * ------------- Description -------------
 */
@ContractFactory(entites = {CommonListEntity.class})
public interface WorkCountAPI {

    void getWorkCount(String url, Map<String, Object> queryParam);
}
