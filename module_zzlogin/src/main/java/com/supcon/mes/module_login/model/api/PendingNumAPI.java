package com.supcon.mes.module_login.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_login.model.bean.PendingNumEntity;

/**
 * Created by wangshizhan on 2018/12/7
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = PendingNumEntity.class)
public interface PendingNumAPI {

    void queryPendingNum(long userId);

}
