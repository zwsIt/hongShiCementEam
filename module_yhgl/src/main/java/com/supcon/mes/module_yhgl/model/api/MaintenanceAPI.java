package com.supcon.mes.module_yhgl.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_yhgl.model.bean.LubricateOilsListEntity;
import com.supcon.mes.module_yhgl.model.bean.MaintenanceListEntity;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = MaintenanceListEntity.class)
public interface MaintenanceAPI {

    void listMaintenance(long id);

}
