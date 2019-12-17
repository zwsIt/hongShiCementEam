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
public interface EamTypeRefAPI {
    /**
     * 获取设备类型参照list
     * @param queryMap
     */
    void listEamType(int pageNo, Map<String, Object> queryMap);
}
