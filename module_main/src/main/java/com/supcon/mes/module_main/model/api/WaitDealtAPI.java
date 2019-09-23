package com.supcon.mes.module_main.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 */
@ContractFactory(entites = {CommonBAPListEntity.class, BapResultEntity.class})
public interface WaitDealtAPI {

    void getWaitDealt(int page, int pageSize, Map<String, Object> params);

    void proxyPending(long pendingId, long proxyUserId,String proxDesc);
}
