package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/9
 * Email zhangwenshuai1@supcon.com
 * Desc 获取deploymentId
 */
public class CurrentDeploymentEntity extends ResultEntity {

    /**
     * dealSuccessFlag : true
     * processVersion : 3
     * deploymentId : 1099
     */

    public boolean dealSuccessFlag;
    public int processVersion; // 版本
    public Long deploymentId;

}
