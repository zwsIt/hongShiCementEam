package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.RefMaintainListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/5
 * ------------- Description -------------
 */
@ContractFactory(entites = RefMaintainListEntity.class)
public interface RefMaintainAPI {
    void listRefMaintain(int pageNum, Long eamID, Map<String, Object> queryParam);
}
