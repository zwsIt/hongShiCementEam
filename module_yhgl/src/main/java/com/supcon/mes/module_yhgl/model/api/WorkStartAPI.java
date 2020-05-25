package com.supcon.mes.module_yhgl.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonEntity;

/**
 * @Description:
 * @Author: zhangwenshuai
 * @CreateDate: 2020/5/23 10:42
 */
@ContractFactory(entites = CommonEntity.class)
public interface WorkStartAPI {
    void workStartSubmit(String workStartDTO);
}
