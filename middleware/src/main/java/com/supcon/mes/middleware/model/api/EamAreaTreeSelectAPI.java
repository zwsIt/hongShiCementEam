package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.DepartmentTreeViewEntity;
import com.supcon.mes.middleware.model.bean.EamAreaTreeViewEntity;

/**
 * Created by zhangwenshuai on 2020/05/25
 * Email:zhangwenshuai1@supcom.com
 * 设备区域选择API
 */
@ContractFactory(entites = EamAreaTreeViewEntity.class)
public interface EamAreaTreeSelectAPI {
    void getEamAreaList(String id);
}
