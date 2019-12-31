package com.supcon.mes.module_hs_tsd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/27
 * Email zhangwenshuai1@supcon.com
 * Desc 停电作业票submit
 */
@ContractFactory(entites = {BapResultEntity.class})
public interface ElectricitySubmitAPI {
    void submit(String view,Map<String, Object> queryParams,Map<String, Object> attachmentMap, String __pc__);
}
