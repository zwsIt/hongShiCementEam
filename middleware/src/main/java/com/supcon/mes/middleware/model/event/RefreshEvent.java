package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2017/12/29.
 * Email:wangshizhan@supcon.com
 */

public class RefreshEvent extends BaseEntity {
    public String action;
    public Integer pos;
    public Long delId;  //删除表体某项id
    public RefreshEvent(){

    }
    public RefreshEvent(String action, Integer pos) {
        this.action = action;
        this.pos = pos;
    }

    public RefreshEvent(Long delId) {
        this.delId = delId;
    }
}
