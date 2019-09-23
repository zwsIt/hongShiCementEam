package com.supcon.mes.module_warn.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_warn.model.bean.SparePartWarnListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 润滑维保
 */
@ContractFactory(entites = SparePartWarnListEntity.class)
public interface SparePartWarnAPI {
    void getSparePart(String url,Map<String, Object> params, int page,long id);
}
