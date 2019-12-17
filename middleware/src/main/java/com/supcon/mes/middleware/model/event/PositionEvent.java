package com.supcon.mes.middleware.model.event;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/11/5
 * Email zhangwenshuai1@supcon.com
 * Desc 适用于adapter中涉及位置项回填数据
 */
public class PositionEvent {
    private int position;
    private Object obj;

    public PositionEvent(int position, Object obj) {
        this.position = position;
        this.obj = obj;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
