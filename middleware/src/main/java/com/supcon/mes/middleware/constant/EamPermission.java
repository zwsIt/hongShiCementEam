package com.supcon.mes.middleware.constant;

/**
 * Created by wangshizhan on 2018/3/21.
 * Email:wangshizhan@supcon.com
 */

public enum EamPermission {

    DeviceModify    (1),
    DeviceCheck     (2),
    Point           (4),
    Maintain        (8),
    Lubricate       (16),
    Repair          (32),
    Insection       (64),
    Fault           (128),
    RunningState    (256),
    Predict         (512),
    MeaDevice       (1024),
    SpecialDevice   (2048),
    Other           (4096);

    private int value;

    EamPermission(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }



    public static boolean hasPermission(EamPermission permission, long value){

        return (value >> permission.ordinal() & 0x01) == 1;

    }

    public static boolean hasPermission(EamPermission permission, String moduleName){

        return permission.name().equals(moduleName);

    }

    public static boolean hasPermission(String moduleName, long value){

        EamPermission eamPermission = EamPermission.valueOf(moduleName);

        return hasPermission(eamPermission, value);

    }

    public static boolean hasPermission(String moduleName, String permissionStr){

        return permissionStr.contains(moduleName);

    }
}
