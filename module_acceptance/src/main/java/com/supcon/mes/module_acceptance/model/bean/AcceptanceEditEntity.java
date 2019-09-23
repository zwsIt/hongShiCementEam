package com.supcon.mes.module_acceptance.model.bean;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
public class AcceptanceEditEntity extends BaseEntity {
    public final static int EDITNUM = 0x01;
    public final static int EDITBOL = 0x02;
    public final static int EDITTEXT = 0x03;

    public Long id;
    public ValueEntity defaultValueType;//类型
    public boolean defaultValue;
    public String item;
    public String isItemValue;
    public String noItemValue;
    public boolean result;
    public String conclusion;

    public String category;//类别
    public Float total;//总计

    public List<AcceptanceEditEntity> categorys = new LinkedList<>();

    @Expose
    public int viewType = 0;

    public int valueType() {
        if (defaultValueType != null && !TextUtils.isEmpty(defaultValueType.id)) {
            if (defaultValueType.id.equals("BEAM_066/01")) {
                return EDITNUM;
            }
            if (defaultValueType.id.equals("BEAM_066/02")) {
                return EDITBOL;
            }
            if (defaultValueType.id.equals("BEAM_066/03")) {
                return EDITTEXT;
            }
        }

        return EDITBOL;
    }
}
