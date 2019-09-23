package com.supcon.mes.module_sbda_online.screen;

import com.supcon.mes.middleware.model.bean.ScreenEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/2
 * ------------- Description -------------
 */
public enum ScreenState {
    UNLIMIT("状态不限", ""),
    INUSE("在用", "01"),
    PROHIBIT("禁用", "02"),
    SEALUP("封存", "03"),
    SCRAP("报废", "04");

    private String name;
    private String layRec;

    ScreenState(String name, String layRec) {
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
