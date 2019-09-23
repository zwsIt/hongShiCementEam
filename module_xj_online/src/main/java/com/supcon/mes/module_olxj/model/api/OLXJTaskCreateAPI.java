package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.common.com_http.NullEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;

import java.util.Map;

import retrofit2.http.QueryMap;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {BapResultEntity.class, CommonEntity.class})
public interface OLXJTaskCreateAPI {

    void createTempTask(Map<String, Object> map);

    void createTempTaskNew(Map<String, Object> map);
}
