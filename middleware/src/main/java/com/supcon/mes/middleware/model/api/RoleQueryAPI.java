package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.RoleListEntity;

/**
 * Created by wangshizhan on 2018/7/30
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = RoleListEntity.class)
public interface RoleQueryAPI {
    void queryRoleListEntity(String userName);
}
