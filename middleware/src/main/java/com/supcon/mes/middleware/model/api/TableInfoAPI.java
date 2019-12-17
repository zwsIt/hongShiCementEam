package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;

/**
 * @description TableInfoAPI
 * @author  2019/9/27
 */
@ContractFactory(entites = {Object.class})
public interface TableInfoAPI {
    /**
     * @param
     * @return 获取备件领用申请单表头信息
     * @description h
     * @author user 2019/9/27
     */
    void getTableInfo(String url, Long id, String includes);
}
