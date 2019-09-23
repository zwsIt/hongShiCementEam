package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonLabelListEntity;
import com.supcon.mes.middleware.model.factory.SearchContentFactory;

import java.util.Map;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/239:47
 */
@ContractFactory(entites = {CommonLabelListEntity.class})
public interface CommonFilterSearchListAPI {
    void getCommonFilterSearchList(int pageIndex,SearchContentFactory.FilterType type, Map<String, Object> param);
}
