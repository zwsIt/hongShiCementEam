package com.supcon.mes.module_overhaul_workticket.constant;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/12
 * Email zhangwenshuai1@supcon.com
 * Desc 安全措施明细
 */
public enum SafetyMeasureDetails {
//    ONE(0,"已按照规定召开检修例会",OperateType.VIDEO.getType()),
//    TWO(1,"已办理停电工作票",OperateType.CONFIRM.getType()),
//    THREE(2,"已切断检修设备主电源、已切断现场开关、已落实检修警示标志(牌)、电源开关已挂牌上锁",OperateType.CONFIRM.getType()),
//    FOUR(3,"已穿戴好安全防护用品",OperateType.CONFIRM.getType()),
//    FIVE(4,"已通知上下工序相关人员",OperateType.PHOTO.getType()),
//    SIX(5,"检修工作单已送至中控室、检修现场",OperateType.CONFIRM.getType()),
//    SEVEN(6,"专职安全员已到现场",OperateType.CONFIRM.getType());

    ONE(0,"已按照规定召开检修例会、已穿戴好安全防护用品",OperateType.VIDEO.getType()),
    TWO(1,"已办理停电工作票、已切断检修设备主电源、电源开关已挂牌上锁",OperateType.CONFIRM.getType()),
    THREE(2,"已切断现场开关、已落实检修警示标志(牌)",OperateType.PHOTO.getType()),
//    FOUR(3,"已穿戴好安全防护用品",OperateType.CONFIRM.getType()),
    FIVE(4,"已通知上下工序相关人员",OperateType.PHOTO.getType());
//    SIX(5,"检修工作单已送至中控室、检修现场",OperateType.CONFIRM.getType()),
//    SEVEN(6,"专职安全员已到现场",OperateType.CONFIRM.getType());

    private int index;
    private String value;
    private int operateType;

    SafetyMeasureDetails(int index, String value, int operateType) {
        this.index = index;
        this.value = value;
        this.operateType = operateType;
    }
    public int getIndex(){
        return index;
    }

    public String getValue() {
        return value;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }
}
