package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2017/8/15.
 */

public class CommonEntity<T> extends BaseEntity {

    public boolean success;

    public String errMsg;

    public T result;

}
