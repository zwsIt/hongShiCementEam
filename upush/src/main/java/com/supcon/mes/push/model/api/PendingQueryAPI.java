package com.supcon.mes.push.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

/**
 * Created by wangshizhan on 2019/4/30
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {CommonBAPListEntity.class, CommonBAPListEntity.class, CommonBAPListEntity.class})
public interface PendingQueryAPI {

    void queryYH(String tableNo);
    void queryWXGD(String tableNo);
}
