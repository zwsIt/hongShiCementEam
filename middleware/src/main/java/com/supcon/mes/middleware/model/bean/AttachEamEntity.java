package com.supcon.mes.middleware.model.bean;

import android.text.TextUtils;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamType;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 */
public class AttachEamEntity extends BaseEntity {
    public String code;
    public EamType eamType;
    public Long id;
    public String name;
    public String state;
    public String stateForDisplay;
    public String produceDate;
    public String produceFirm;//制造厂
    public String produceCode;//出厂编号
    public String model;//规格型号


    public EamType getEamType() {
        if (eamType == null) {
            eamType = new EamType();
        }
        return eamType;
    }

    public Long getProduceDate() {
        Long aLong = null;
        try {
            if (!TextUtils.isEmpty(produceDate)) {
                aLong = Long.valueOf(produceDate);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return aLong;
    }
}
