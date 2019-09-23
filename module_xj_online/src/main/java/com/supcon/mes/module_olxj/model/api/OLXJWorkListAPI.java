package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkListEntity;

import java.util.Map;

/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

@ContractFactory(entites = {OLXJWorkListEntity.class, CommonBAPListEntity.class, CommonBAPListEntity.class})
public interface OLXJWorkListAPI {

    void getXJWorkItemList(long areaId, long taskId, String deviceName, Boolean isFinish);

    void getWorkItemList(long taskId, int pageNum);

    void getWorkItemListRef(long taskId, int pageNum);
}
