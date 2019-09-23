package com.supcon.mes.module_sbda_online.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda_online.model.bean.SBDAOnlineListEntity;

import java.util.Map;

/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/3/30.
 */
@ContractFactory(entites = {SBDAOnlineListEntity.class})
public interface SBDAOnlineListAPI {
    void getSearchSBDA(Map<String, Object> params, int page);
}
