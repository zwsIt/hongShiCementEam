package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.RefProductListEntity;
import com.supcon.mes.middleware.model.bean.SparePartRefListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/5
 * ------------- Description -------------
 */
@ContractFactory(entites = {RefProductListEntity.class, SparePartRefListEntity.class})
public interface RefProductAPI {

    void listRefProduct(int pageNum, Map<String, Object> queryParam);

    /**
     * @param
     * @return
     * @description 备件参照列表查询
     * @author zhangwenshuai1 2018/10/25
     */
    void listRefSparePart(int pageNum, Long eamID, Map<String, Object> queryParam);
}
