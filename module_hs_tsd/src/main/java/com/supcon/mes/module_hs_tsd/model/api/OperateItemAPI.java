package com.supcon.mes.module_hs_tsd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_hs_tsd.model.bean.OperateItemListEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/28
 * Email zhangwenshuai1@supcon.com
 * Desc 操作事项list获取
 */
@ContractFactory(entites = {OperateItemListEntity.class})
public interface OperateItemAPI {
    void listOperateItems(String url,Long tableId);
}
