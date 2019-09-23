package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.AreaMultiStageEntity;

/**
 * @Author xushiyun
 * @Create-time 7/22/19
 * @Pageage com.supcon.mes.middleware.model.api
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@ContractFactory(entites = AreaMultiStageEntity.class)
public interface XJMultiDepartSelectAPI {
    void getDepartmentInfoList(String id);
}
