package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonFilterSearchListEntity;
import com.supcon.mes.middleware.model.factory.SearchContentFactory;

import java.util.Map;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2210:42
 */
@ContractFactory(entites = {CommonFilterSearchListEntity.class, CommonFilterSearchListEntity.class})
public interface CommonFilterSearchAPI {
    /**
     * 通用组件的关系,所以这里采用 tag来标记每个tag是在哪个组件生成的
     */
    void getRecentTags(SearchContentFactory.FilterType type, Map<String,Object> param);

    /**
     * 获取所有组件
     */
    void getAllRecommendTags(SearchContentFactory.FilterType type, Map<String,Object> param);
}
