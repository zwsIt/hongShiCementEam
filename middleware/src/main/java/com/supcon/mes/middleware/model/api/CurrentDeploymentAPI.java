package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ApiFactory;
import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CurrentDeploymentEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/9
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@ContractFactory(entites = {CurrentDeploymentEntity.class})
public interface CurrentDeploymentAPI {
    /**
     * @description
     * @param processKey 工作流key
     * @return
     * @author zhangwenshuai1 2019/12/9
     *
     */
    void getCurrentDeployment(String processKey);
}
