package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_wxgd.model.bean.LubricateOilsListEntity;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = LubricateOilsListEntity.class)
public interface LubricateOilsAPI {

    void listLubricateOilsList(long id);

}
