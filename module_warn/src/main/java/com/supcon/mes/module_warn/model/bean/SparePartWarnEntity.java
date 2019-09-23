package com.supcon.mes.module_warn.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AccessoryEamId;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.SparePartId;
import com.supcon.mes.middleware.model.bean.ValueEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/23
 * ------------- Description -------------
 */
public class SparePartWarnEntity extends BaseEntity {

    public Long id;
    public WXGDEam eamID;
    public String spareMemo;
    public ValueEntity sparePartStatus;
    public ValueEntity periodUnit;
    public Good productID;//备件
    public Long lastTime;
    public Long nextTime;
    public Float currentDuration;//当前运行时长
    public Float lastDuration;//上次更换时长
    public Float nextDuration;//下次更换时长
    public ValueEntity periodType;//类型
    public Long period; //周期

    public AccessoryEamId accessoryEamId;//附属设备

    public String remark;//备注

    public boolean isCheck;

    public AccessoryEamId getAccessoryEamId() {
        if (accessoryEamId == null) {
            accessoryEamId = new AccessoryEamId();
        }
        return accessoryEamId;
    }


    public WXGDEam getEamID() {
        if (eamID == null) {
            eamID = new WXGDEam();
        }
        return eamID;
    }

    public Good getProductID() {
        if (productID == null) {
            productID = new Good();
        }
        return productID;
    }

    //是否润滑时长
    public boolean isDuration() {
        if (periodType != null && periodType.id.equals("BEAM014/02")) {
            return true;
        }
        return false;
    }
}
