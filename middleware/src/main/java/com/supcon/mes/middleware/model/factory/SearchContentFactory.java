package com.supcon.mes.middleware.model.factory;


import com.supcon.mes.middleware.model.bean.SearchContentListEntity;

import java.util.Map;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2114:02
 */
public class SearchContentFactory {
    /**
     * 检索类别
     */
    public enum FilterType {
        /**
         * 设备
         */
        DEVICE,

    }

    private static BaseFilterSearchListFactory getFilterSearchListFactory(FilterType type){
        switch (type) {
            case DEVICE:
                return DeviceFilterSearchListFactory.singleton();
                default:
        }
        return null;
    }

    /**
     * 根据具体的检索类别创造搜索的列表
     * @param type 搜索的类别
     * @return 返回的包含列表的entity
     */
    public static SearchContentListEntity createSearchContent(int pageIndex, FilterType type, Map<String, Object> param) {
        BaseFilterSearchListFactory baseFilterSearchListFactory = getFilterSearchListFactory(type);
        if(null != baseFilterSearchListFactory)
            return SearchContentListEntity.success().result(baseFilterSearchListFactory.getFilterSearchList(pageIndex,param));
        return  SearchContentListEntity.nil();
    }
}
