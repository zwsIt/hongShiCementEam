package com.supcon.mes.module_yhgl.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {BapResultEntity.class})
public interface YHSubmitAPI {
    void doSubmit(Map<String, Object> map, Map<String, Object> attachmentMap, boolean isEdit);
}
