package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.DepartmentInfoListEntity;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = DepartmentInfoListEntity.class)
public interface DepartmentQueryAPI {

    void listDepartment();

}
