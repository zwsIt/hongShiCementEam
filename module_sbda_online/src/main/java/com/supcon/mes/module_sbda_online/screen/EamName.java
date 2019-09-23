package com.supcon.mes.module_sbda_online.screen;

import com.supcon.mes.middleware.model.bean.ScreenEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/2
 * ------------- Description -------------
 */
public enum EamName {
    UNLIMITE("设备不限", "01"),
    CRUSHER("破碎机", "02"),
    RAWMILL("生料磨", "03"),
    ROTARYCELLAR("回转窖", "04"),
    CEMENTMILL("水泥磨", "05"),
    ROLLERPRESS("辊压机", "06");

    private String name;
    private String layRec;

    EamName(String name, String layRec) {
        this.name = name;
        this.layRec = layRec;
    }

    public String getName() {
        return name;
    }

    public String getLayRec() {
        return layRec;
    }

    public ScreenEntity getScreenEntity() {
        ScreenEntity screenEntity = new ScreenEntity();
        screenEntity.name = name;
        screenEntity.layRec = layRec;
        return screenEntity;
    }

}
