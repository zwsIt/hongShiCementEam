package com.supcon.mes.module_hs_tsd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnListEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/27
 * Email zhangwenshuai1@supcon.com
 * Desc 停送电申请list获取
 */
@ContractFactory(entites = {ElectricityOffOnListEntity.class})
public interface ElectricityOffOnListAPI {
    /**
     * @description 获取停送电list
     * @param pendingQuery 是否待办查询
     * @return isOffOn：true 送电；false：停电
     * @author zhangwenshuai1 2020/1/13
     *
     */
    void listElectricityOffOn(int pageNo, Map<String, Object> queryParams, boolean pendingQuery, boolean isOffOn);
}
