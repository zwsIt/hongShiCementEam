package com.supcon.mes.push.event;


import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by wangshizhan on 2019/4/29
 * Email:wangshizhan@supcom.com
 */
public class PushOpenEvent implements Serializable {

    static final long serialVersionUID=536871008L;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    private String mcontent;

    public PushOpenEvent(String content){
        this.mcontent = content;
    }

    public String getContent() {
        return mcontent;
    }
}
