package com.supcon.mes.module_contact.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.PositionTreeViewEntity;

/**
 * @Author xushiyun
 * @Create-time 7/22/19
 * @Pageage com.supcon.mes.middleware.model.api
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@ContractFactory(entites = PositionTreeViewEntity.class)
public interface ContactPositionSelectAPI {
    void getPositionInfoList(String id);
}
