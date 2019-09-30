package com.supcon.mes.module_main.model.api;

import com.app.annotation.apt.ContractFactory;

import java.util.List;

@ContractFactory(entites = {List.class})
public interface DealInfoAPI {
    /**
     * @param moduleName 实体编码
     * @param tableName  表名
     * @param tableInfoId  单据id
     * @return
     * @description 获取单据处理意见
     * @author user 2019/9/25
     */
    void listDealInfo(String moduleName, String tableName, Long tableInfoId);
}
