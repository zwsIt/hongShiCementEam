package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;

/**
 * Created by wangshizhan on 2018/9/21
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = BapResultEntity.class)
public interface ModulePowerAPI {

    void getStartActivePowerCode( long deploymentId);
}
