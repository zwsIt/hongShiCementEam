package com.supcon.mes.middleware.model.event;

import android.nfc.Tag;

import com.supcon.mes.middleware.model.bean.TagEntity;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2216:08
 */
public class FilterSearchTabEvent extends BaseEvent {
    private FilterSearchTabEvent() {

    }
    public FilterSearchTabEvent tag(TagEntity tagEntity) {
        this.tag = tagEntity;
        return this;
    }
    public static FilterSearchTabEvent nil(){
        return new FilterSearchTabEvent();
    }
    public TagEntity tag;
}
