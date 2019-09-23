package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDTableInfoEntity;

import java.util.Map;

@ContractFactory(entites = {WXGDTableInfoEntity.class, ResultEntity.class})
public interface WXGDDispatcherAPI {

    void getWxgdInfo(long pending, long tableInfoId);

    /**
     * @param
     * @return
     * @description 转为大修 或 检修
     * @author zhangwenshuai1 2018/9/4
     */
    void translateRepair(long faultInfoId, String repairType);


}
