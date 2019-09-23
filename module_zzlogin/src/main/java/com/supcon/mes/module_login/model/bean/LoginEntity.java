package com.supcon.mes.module_login.model.bean;


import com.supcon.common.com_http.BaseEntity;

import java.io.Serializable;

/**
 * Created by wangshizhan on 2017/8/7.
 */

public class LoginEntity extends BaseEntity implements Serializable{

    public UserEntity result;
    public boolean success;
    public String errMsg;
}
