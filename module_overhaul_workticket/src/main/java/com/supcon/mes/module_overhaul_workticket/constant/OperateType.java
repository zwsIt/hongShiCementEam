package com.supcon.mes.module_overhaul_workticket.constant;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/12
 * Email zhangwenshuai1@supcon.com
 * Desc 安全措施操作类型
 */
public enum OperateType {
    VIDEO(0,"视频"),
    PHOTO(1,"拍照"),
    NFC(2,"刷卡"),
    CONFIRM(3,"确认");

    private int type;
    private String value;

    OperateType(int type, String value) {
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
