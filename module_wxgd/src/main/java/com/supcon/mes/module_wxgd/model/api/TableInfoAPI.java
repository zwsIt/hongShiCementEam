package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_wxgd.model.bean.SparePartApplyHeaderInfoEntity;

/**
 * @description TableInfoAPI
 * @author  2019/9/27
 */
@ContractFactory(entites = {SparePartApplyHeaderInfoEntity.class})
public interface TableInfoAPI {
    /**
     * @param
     * @return 获取备件领用申请单表头信息
     * @description h
     * @author user 2019/9/27
     */
    void getSparePartApplyTableInfo(Long id, String includes);
}
