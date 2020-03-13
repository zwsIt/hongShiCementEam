package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.AreaListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.Company;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = CommonListEntity.class)
public interface CompanyQueryAPI {

    void listCompany();

}
