package com.supcon.mes.module_hs_tsd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnListEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/27
 * Email zhangwenshuai1@supcon.com
 * Desc 停电申请list获取
 */
@ContractFactory(entites = {ElectricityOffOnListEntity.class})
public interface ElectricityOffListAPI {
    void listElectricityOff(int pageNo, Map<String, Object> queryParams, boolean pendingQuery);
}
