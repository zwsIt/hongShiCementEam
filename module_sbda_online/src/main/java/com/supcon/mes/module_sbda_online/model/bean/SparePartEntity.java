package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AccessoryEamId;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.ValueEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class SparePartEntity extends BaseEntity {

    public long id;
    public ValueEntity periodType;//类型
    public Long lastTime;//上次更换日期
    public Long nextTime;//下次更换日期
    public Float nextDuration;//下次更换时长
    public Float lastDuration;//上次更换时长
    public String spareMemo;//备注
    public float depleteSum;//数量
    public float standingCrop;//现存量
    public Good productID;//备件

    public AccessoryEamId accessoryEamId;//附属设备

    public Good getProductID() {
        if (productID == null) {
            productID = new Good();
        }
        return productID;
    }

    //是否是运行时长
    public boolean isDuration() {
        if (periodType != null && periodType.id.equals("BEAM014/02")) {
            return true;
        }
        return false;
    }

    public AccessoryEamId getAccessoryEamId() {
        if (accessoryEamId == null) {
            accessoryEamId = new AccessoryEamId();
        }
        return accessoryEamId;
    }
}
