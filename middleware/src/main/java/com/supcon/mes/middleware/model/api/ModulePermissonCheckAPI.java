package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.LongResultEntity;

/**
 * Created by wangshizhan on 2018/9/21
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = LongResultEntity.class)
public interface ModulePermissonCheckAPI {
    /**
     * 检查用户是否有发起该工作流的权限
     * @param userName    用户名
     * @param proccessKey 工作流名
     */
    void checkModulePermission(String userName, String proccessKey);
}
