package com.supcon.mes.middleware.model.event;

/**
 * Created by wangshizhan on 2019/4/29
 * Email:wangshizhan@supcom.com
 */
public class PushAppOpenEvent extends BaseEvent {

    private String mcontent;

    public PushAppOpenEvent(String content){
        this.mcontent = content;
    }

    public String getContent() {
        return mcontent;
    }
}
