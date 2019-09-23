package com.supcon.mes.module_login.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = BapResultEntity.class)
public interface ZhiZhiUrlQueryAPI {

    void getZhizhiUrl();

}
