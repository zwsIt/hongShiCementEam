package com.supcon.mes.module_contact.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.DepartmentTreeViewEntity;

/**
 * @Author xushiyun
 * @Create-time 7/22/19
 * @Pageage com.supcon.mes.middleware.model.api
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@ContractFactory(entites = DepartmentTreeViewEntity.class)
public interface ContactDepartSelectAPI {
    void getDepartmentInfoList(String id);
}
