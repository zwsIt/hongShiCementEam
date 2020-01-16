package com.supcon.mes.module_contact.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

/**
 * Created by wangshizhan on 2019/12/5
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {CommonBAPListEntity.class, CommonBAPListEntity.class})
public interface ContactDataAPI {

    void getStaffList(int pageNo, int pageSize);
}
