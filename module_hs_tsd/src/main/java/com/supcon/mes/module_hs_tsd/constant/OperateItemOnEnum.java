package com.supcon.mes.module_hs_tsd.constant;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/29
 * Email zhangwenshuai1@supcon.com
 * Desc 送电操作项措施明细
 */
public enum OperateItemOnEnum {
    ONE(0,"取下现场检修开关上的停电工作票，合上检修开关"),
    TWO(1,"取下控制按钮钥匙上禁示牌，将钥匙打在现场操作位置"),
    THREE(2,"单机试运行正常后，控制开关的钥匙打到中控操作位置"),
    FOUR(3,"操作人，检修负责人共同去电力室校对低压柜双重命名"),
    FIVE(4,"取下低压柜上的停电工件票，车间发令人、操作人、检修负责人、检修人员各自打开挂锁"),
    SIX(5,"低压柜由冷备用改为热备用状态（合上总开关），并检查中控显示正常"),
    SEVEN(6,"取下控制按钮钥匙上的禁示牌，将钥匙打在现场操作位置"),
    EIGHT(7,"单车试运行正常确认安全后，控制开关的钥匙打到中控操作位置");

    private int index;
    private String value;

    OperateItemOnEnum(int index, String value) {
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
