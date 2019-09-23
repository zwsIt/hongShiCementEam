package com.supcon.mes.middleware.constant;

/**
 * Created by wangshizhan on 2018/3/21.
 * Email:wangshizhan@supcon.com
 */

public enum Module {

    DeviceModify    ("BEAM060/01","档案修改"),
    DeviceCheck     ("BEAM060/02","档案查看"),
    Point           ("BEAM060/03","点检业务"),
    Maintain        ("BEAM060/04","保养业务"),
    Lubricate       ("BEAM060/05","润滑业务"),
    Repair          ("BEAM060/06","维修业务"),
    Insection       ("BEAM060/07","移动作业"),
    Fault           ("BEAM060/08","隐患管理"),
    RunningState    ("BEAM060/09","运行管理"),
    Predict         ("BEAM060/10","备件管理"),
    MeaDevice       ("BEAM060/11","测量设备"),
    SpecialDevice   ("BEAM060/12","特种设备"),
    Other           ("BEAM060/13","其他");

    private String code;
    private String name;
    Module(String code, String name){

        this.code = code;
        this.name = name;

    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
