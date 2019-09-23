package com.supcon.mes.module_sbda.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda.model.bean.RecentDeviceListEntity;

/**
 * Created by wangshizhan on 2017/12/28.
 * Email:wangshizhan@supcon.com
 */
@ContractFactory(entites = {RecentDeviceListEntity.class})
public interface RecentDeviceAPI {
    void getRecentDevice(String moduleName, int pageIndex);           //我的设备
}
