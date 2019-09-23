package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BaseSearchListEntity;
import com.supcon.mes.middleware.model.contract.BaseSearchContract;

/**
 * Created by wangshizhan on 2017/12/28.
 * Email:wangshizhan@supcon.com
 */

@ContractFactory(entites = {BaseSearchListEntity.class, BaseSearchListEntity.class})
public interface BaseSearchAPI {
    void baseSearch(int pageIndex,String code, String mes, String mod);           //按照条件搜索

    /**
     * @param propertyValue 字段属性值
     * @param blurValue     模糊值
     * @param permission    权限
     * @return
     * @description 设备查询
     * @author zhangwenshuai1 2018/9/29
     */
    void baseEamSearch(int pageIndex, String propertyValue, String blurValue, String permission); // 设备查询
}
