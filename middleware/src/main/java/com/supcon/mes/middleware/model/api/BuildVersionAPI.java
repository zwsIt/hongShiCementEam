package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BuildVersionEntity;

/**
 * Created by wangshizhan on 2019/12/26
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = BuildVersionEntity.class)
public interface BuildVersionAPI {

    void getLatestBuildVersion(String appId);

}
