package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.ContractListEntity;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = ContractListEntity.class)
public interface ContractQueryAPI {

    void listStaff(String staffName, int pageNo);

}
