package com.supcon.mes.module_sbda.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda.model.bean.SearchDeviceEntity;

/**
 * Created by wangshizhan on 2017/12/28.
 * Email:wangshizhan@supcon.com
 */

@ContractFactory(entites = {SearchDeviceEntity.class})
public interface SearchDeviceAPI {
    void searchDevice(String code);           //搜索设备
}
