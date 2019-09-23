package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/5/14.
 * Email:wangshizhan@supcon.com
 */

public class ImageDeleteEvent extends BaseEntity {

    private String picName;
    private Integer pos;

    public ImageDeleteEvent(String picName, Integer pos) {
        this.picName = picName;
        this.pos = pos;
    }

    public Integer getPos() {
        return pos;
    }

    public ImageDeleteEvent(String picName){
        this.picName = picName;
    }

    public String getPicName() {
        return picName;
    }
}
