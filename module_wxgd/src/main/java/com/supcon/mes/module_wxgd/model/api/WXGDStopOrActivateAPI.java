package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.common.com_http.NullEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/6
 * ------------- Description -------------
 */
@ContractFactory(entites = NullEntity.class)
public interface WXGDStopOrActivateAPI {

    void stopOrStart(Map<String, Object> map);
}
