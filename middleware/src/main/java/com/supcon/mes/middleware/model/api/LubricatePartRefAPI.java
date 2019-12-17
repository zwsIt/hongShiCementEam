package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/11/5
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@ContractFactory(entites = {CommonBAPListEntity.class})
public interface LubricatePartRefAPI {
    /**
     * 获取润滑部位参照list
     * @param queryMap
     */
    void listLubricatePart(int pageNo,Map<String,Object> queryMap);
}
