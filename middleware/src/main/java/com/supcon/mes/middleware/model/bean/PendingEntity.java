package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/7/17
 * Email:wangshizhan@supcom.com
 */
public class PendingEntity extends BaseEntity {

    public String activityName;
    public String activityType;
    public Boolean bulkDealFlag;
    public Long deploymentId;

    public Long id;
    public String openUrl;
    public String processDescription;
    public String processId;
    public String processKey;
    public long processVersion;
    public String taskDescription;  //单据状态
    public long userId;

}
