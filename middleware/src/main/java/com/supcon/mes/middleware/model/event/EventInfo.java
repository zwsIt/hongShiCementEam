package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.eventbus.EventBus;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/6/8
 * ------------- Description -------------
 * 事件分发器
 */
public class EventInfo extends BaseEntity {
    private int eventId;
    private Object value;
    private String msg;

    public EventInfo(int eventId, Object value) {
        this.eventId = eventId;
        this.value = value;
    }

    public EventInfo(int eventId, String msg) {
        this.eventId = eventId;
        this.msg = msg;
    }

    public EventInfo(int eventId, Object value, String msg) {
        this.eventId = eventId;
        this.value = value;
        this.msg = msg;
    }

    public static void register(Object r) {
        EventBus.getDefault().register(r);
    }

    public static void unregister(Object un) {
        EventBus.getDefault().unregister(un);
    }

    public static void postEvent(int eventId, Object value) {
        EventInfo eventInfo = new EventInfo(eventId, value);
        EventBus.getDefault().post(eventInfo);
    }

    public static void postEvent(int eventId, String msg) {
        EventInfo eventInfo = new EventInfo(eventId, msg);
        EventBus.getDefault().post(eventInfo);
    }

    public static void postEvent(int eventId, Object value, String msg) {
        EventInfo eventInfo = new EventInfo(eventId, value, msg);
        EventBus.getDefault().post(eventInfo);
    }

    public int getEventId() {
        return eventId;
    }

    public Object getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }
}
