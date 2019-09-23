package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/7/23
 * Email:wangshizhan@supcom.com
 */
public class BapResultEntity extends BaseEntity {

    public boolean dealSuccessFlag;
    public boolean dealSuccess;
    public String operateType;
    public String operate;
    public long id;
    public long tableInfoId;
    public String errMsg;
    public String viewselect;
    public long pendingId;
    public String zhizhiUrl;
    public String powerCode;

    public String Message;
}
