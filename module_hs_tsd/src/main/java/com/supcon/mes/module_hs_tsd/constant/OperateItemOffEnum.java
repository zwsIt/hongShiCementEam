package com.supcon.mes.module_hs_tsd.constant;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/29
 * Email zhangwenshuai1@supcon.com
 * Desc 停电操作项措施明细
 */
public enum OperateItemOffEnum {
    ONE(0,"核对工作票内容，检查现场控制按钮钥匙在中控操作位置"),
    TWO(1,"现场确认中控操作员已将设备在停止状态"),
    THREE(2,"将控制按钮钥匙开关打到检修位置，取下钥匙"),
    FOUR(3,"拉开现场检修开关挂上低压停电工作票"),
    FIVE(4,"去电力室校对低压柜双重命名"),
    SIX(5,"检查低压柜确在热备用状态（主电源已断开）"),
    SEVEN(6,"低压柜由热备用改为冷备用状态（拉开总开关）"),
    EIGHT(7,"车间发令人、操作人、检修负责人、检修人员各自挂上低压停电工作票及警示牌");

    private int index;
    private String value;

    OperateItemOffEnum(int index, String value) {
        this.index = index;
        this.value = value;
    }
    public int getIndex(){
        return index;
    }

    public String getValue() {
        return value;
    }
}
