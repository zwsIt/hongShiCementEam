package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AccessoryEamId;
import com.supcon.mes.middleware.model.bean.SparePartId;
import com.supcon.mes.middleware.model.bean.ValueEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class LubriEntity extends BaseEntity {


    public Long id;
    public LubricateOil lubricateOil;//润滑油
    public ValueEntity oilType;//加换油
    public String lubricatePart;//润滑部位
    public Float lastDuration;//上次润滑时长
    public Float nextDuration;//下次润滑时长
    public Long lastTime;//上次润滑时间
    public Long nextTime;//下次润滑时间
    public Float sum;//用量
    public ValueEntity periodType;//润滑类型

    public SparePartId sparePartId;//备件编码
    public AccessoryEamId accessoryEamId;//附属设备

    public LubricateOil getLubricateOil() {
        if (lubricateOil == null) {
            lubricateOil = new LubricateOil();
        }
        return lubricateOil;
    }

    public ValueEntity getOilType() {
        if (oilType == null) {
            oilType = new ValueEntity();
        }
        return oilType;
    }

    public class LubricateOil extends BaseEntity {
        public String name;
    }

    //是否润滑时长
    public boolean isDuration() {
        if (periodType != null && periodType.id.equals("BEAM014/02")) {
            return true;
        }
        return false;
    }

    public SparePartId getSparePartId() {
        if (sparePartId == null) {
            sparePartId = new SparePartId();
        }
        return sparePartId;
    }

    public AccessoryEamId getAccessoryEamId() {
        if (accessoryEamId == null) {
            accessoryEamId = new AccessoryEamId();
        }
        return accessoryEamId;
    }
}
