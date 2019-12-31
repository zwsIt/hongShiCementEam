package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/11/12
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@ContractFactory(entites = {CommonBAPListEntity.class})
public interface CommonListAPI {
    /**
     * 获取相应对象数据list
     * @param queryMap
     */
    void listCommonObj(int pageNo, Map<String, Object> queryMap, boolean OffOn);
}
