package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;

@ContractFactory(entites = {CommonListEntity.class,ResultEntity.class})
public interface SparePartListAPI {

    /**
     * @description 更新现存量
     * @param productCode 备件编码字符串
     * @return
     * @author zhangwenshuai1 2018/10/10
     *
     */
    void updateStandingCrop(String productCode);

    /**
     * @description 领用出库单
     * @return
     * @author zhangwenshuai1 2018/10/24
     *
     */
    void generateSparePartApply(String listStr);

}
