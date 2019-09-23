package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckListEntity;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = AcceptanceCheckListEntity.class)
public interface AcceptanceCheckAPI {

    void listAcceptanceCheckList(long id);

}
