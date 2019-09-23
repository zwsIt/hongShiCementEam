package com.supcon.mes.module_yhgl.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_yhgl.model.bean.LubricateOilsListEntity;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = LubricateOilsListEntity.class)
public interface LubricateOilsAPI {

    void listLubricateOilsList(long id);

}
