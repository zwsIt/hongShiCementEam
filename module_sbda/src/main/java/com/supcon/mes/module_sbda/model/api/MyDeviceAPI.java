package com.supcon.mes.module_sbda.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda.model.bean.MyDeviceListEntity;

/**
 * Created by wangshizhan on 2017/12/28.
 * Email:wangshizhan@supcon.com
 */

@ContractFactory(entites = {MyDeviceListEntity.class})
public interface MyDeviceAPI {
    void getMyDevice(String moduleCode, int page, int pageNum);           //我的设备
}
