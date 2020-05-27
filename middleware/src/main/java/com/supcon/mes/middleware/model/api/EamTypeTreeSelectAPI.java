package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.EamAreaTreeViewEntity;
import com.supcon.mes.middleware.model.bean.EamTypeTreeViewEntity;

/**
 * Created by zhangwenshuai on 2020/05/25
 * Email:zhangwenshuai1@supcom.com
 * 设备类型选择API
 */
@ContractFactory(entites = EamTypeTreeViewEntity.class)
public interface EamTypeTreeSelectAPI {
    void getEamTypeList(String id);
}
