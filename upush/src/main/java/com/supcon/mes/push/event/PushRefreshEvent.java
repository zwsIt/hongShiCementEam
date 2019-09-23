package com.supcon.mes.push.event;

import java.io.Serializable;

/**
 * Created by wangshizhan on 2017/12/29.
 * Email:wangshizhan@supcon.com
 */

public class PushRefreshEvent implements Serializable {
    public String action;

    public PushRefreshEvent() {

    }

    public PushRefreshEvent(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
