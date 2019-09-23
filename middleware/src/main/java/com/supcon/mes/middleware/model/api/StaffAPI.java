package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.UserInfoListEntity;

@ContractFactory(entites = UserInfoListEntity.class)
public interface StaffAPI {

    void listCommonContractStaff(String staffName,int pageNo);
}
