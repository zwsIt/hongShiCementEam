package com.supcon.mes.module_wxgd.model.event;

import java.util.List;

/**
 * ListEvent
 * created by zhangwenshuai1 2018/10/25
 */
public class ListEvent {
    private String flag;
    private List list;

    public ListEvent(String flag, List list) {
        this.flag = flag;
        this.list = list;
    }

    public String getFlag() {
        return flag;
    }

    public List getList() {
        return list;
    }
}
