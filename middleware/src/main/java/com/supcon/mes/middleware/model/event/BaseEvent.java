package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/3/26.
 * Email:wangshizhan@supcon.com
 */

public class BaseEvent extends BaseEntity {

    private boolean isSuccess;
    private String msg;

    public BaseEvent(boolean isSuccess, String msg){
        this.isSuccess = isSuccess;
        this.msg = msg;
    }

    public BaseEvent(){
        this.isSuccess = true;//默认为成功
    }

    public BaseEvent(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {this.msg = msg;}
}
