package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/3
 * ------------- Description -------------
 */
@ContractFactory(entites = CommonBAPListEntity.class)
public interface SparePartReceiveRecordAPI {

    void sparePartList(Map<String, Object> params, int page);
}
