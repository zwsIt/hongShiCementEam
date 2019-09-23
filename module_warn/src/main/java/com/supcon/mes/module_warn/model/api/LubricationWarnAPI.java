package com.supcon.mes.module_warn.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_warn.model.bean.LubricationWarnListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 润滑维保
 */
@ContractFactory(entites = LubricationWarnListEntity.class)
public interface LubricationWarnAPI {
    void getLubrication(String url, Map<String, Object> params, int page, long id);
}
