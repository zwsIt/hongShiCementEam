package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.beans.WorkFlowEntity;

import java.util.List;

/**
 * Created by wangshizhan on 2018/7/19
 * Email:wangshizhan@supcom.com
 */
public class WorkFlow extends BaseEntity {

    public String outcomeDes;
    public List<WorkFlowEntity> outcomeMapJson;
    public String additionalUsersStr;
    public String outcome;
    public String commont;
}
