package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.WorkFlowListEntity;

/**
 * Created by wangshizhan on 2018/7/20
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {WorkFlowListEntity.class})
public interface WorkFlowAPI {

    void findWorkFlow(long pendingId);

}
