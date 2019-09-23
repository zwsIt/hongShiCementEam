package com.supcon.mes.module_acceptance.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
@ContractFactory(entites = {BapResultEntity.class})
public interface AcceptanceSubmitAPI {
    void doSubmit(Map<String, Object> map,String powerCode);
}
