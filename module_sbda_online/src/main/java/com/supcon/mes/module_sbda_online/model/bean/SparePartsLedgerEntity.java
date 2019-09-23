package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.ValueEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/3
 * ------------- Description -------------
 */
public class SparePartsLedgerEntity extends BaseEntity {

    public String name;
    public String code;
    public EamType eamType;
    public String model;

    public String state;
    public String stateForDisplay;

    public ValueEntity fileState;

    public EamType getEamType() {
        if (eamType == null) {
            eamType = new EamType();
        }
        return eamType;
    }

    public ValueEntity getFileState() {
        if (fileState == null) {
            fileState = new ValueEntity();
        }
        return fileState;
    }
}
