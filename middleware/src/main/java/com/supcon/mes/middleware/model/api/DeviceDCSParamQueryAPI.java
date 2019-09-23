package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

/**
 * Created by wangshizhan on 2019/5/28
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = CommonListEntity.class)
public interface DeviceDCSParamQueryAPI {

    void getDeviceDCSParams(long eamId);

}
