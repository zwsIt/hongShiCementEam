package com.supcon.mes.module_sbda.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonSearchDeviceListEntity;

import java.util.Map;

/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/5/17.
 * Desc: Todo 搜索fragment通用框架用于获取对应的所有的设备信息
 */
@ContractFactory(entites = {CommonSearchDeviceListEntity.class})
public interface CommonSearchDeviceAPI {
    void getSearchDevice(String moduleName, String blurMes, Map<String, Object> params, int pageIndex);           //我的设备
}
