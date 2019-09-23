package com.supcon.mes.module_xj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_xj.model.bean.XJWorkItemListEntity;

/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

@ContractFactory(entites = {XJWorkItemListEntity.class})
public interface XJWorkItemAPI {

    void getXJWorkItemList(long areaId, long taskId, String deviceName,Boolean isFinish);
}
