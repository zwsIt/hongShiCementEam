package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/6/24
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@ContractFactory(entites = {CommonEntity.class})
public interface PcQueryAPI {
    void queryPc(String operateCode, String flowKey);
}
