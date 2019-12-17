package com.supcon.mes.middleware.constant;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/12
 * Email zhangwenshuai1@supcon.com
 * Desc 安全措施操作类型
 */
public enum QueryBtnType {
    PENDING_QUERY(1000,"仅查待办"),
    ALL_QUERY(1001,"查询");

    private int type;
    private String value;

    QueryBtnType(int type, String value) {
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
