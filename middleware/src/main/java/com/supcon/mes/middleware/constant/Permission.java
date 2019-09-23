package com.supcon.mes.middleware.constant;

/**
 * Created by wangshizhan on 2017/12/15.
 * Email:wangshizhan@supcon.com
 */

public enum Permission {

    FAULT(1),
    RUN_STATE(2),
    DEVICE(4),
    ALL(7);

    private int value;

    Permission(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static boolean hasPermission(Permission permission, int value){

        return (value >> permission.ordinal() & 0x01) == 1;

    }

}
