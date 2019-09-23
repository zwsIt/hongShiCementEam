package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowListEntity;

import retrofit2.http.Query;

/**
 * Created by wangshizhan on 2018/7/20
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {ResultEntity.class})
public interface YHCloseAPI {

    void closeWorkAndSaveReason(long id, String reason);

}
