package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_wxgd.model.bean.SparePartListEntity;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {SparePartListEntity.class})
public interface SparePartAPI {

    void listSparePartList(long id);

}
