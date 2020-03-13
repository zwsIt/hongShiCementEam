package com.supcon.mes.middleware.constant;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/12
 * Email zhangwenshuai1@supcon.com
 * Desc 时间查询按钮
 */
public enum DateBtn {
    ONE_MONTH(1000,"近一月"),
    THREE_MONTH(1001,"近三月"),
    SIX_MONTH(1002,"近半年");

    private int type;
    private String value;

    DateBtn(int type, String value) {
        this.type = type;
        this.value = value;
    }
    public int getType(){
        return type;
    }

    public String getValue() {
        return value;
    }

}
