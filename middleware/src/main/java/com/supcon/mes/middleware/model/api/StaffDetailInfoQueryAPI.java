package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.StaffDetailInfoEntity;

/**
 * Created by wangshizhan on 2018/8/1
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {StaffDetailInfoEntity.class})
public interface StaffDetailInfoQueryAPI {

    void queryStaffDetailInfo(String staffCode, long companyId);

}
