package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/5/3.
 * Email:wangshizhan@supcon.com
 */

public class LoginValidEvent extends BaseEntity {

    private boolean isLoginValid = false;

    public LoginValidEvent(boolean isLoginValid){
        this.isLoginValid = isLoginValid;
    }

    public boolean isLoginValid() {
        return isLoginValid;
    }
}
