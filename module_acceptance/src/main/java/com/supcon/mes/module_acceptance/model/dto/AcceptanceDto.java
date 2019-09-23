package com.supcon.mes.module_acceptance.model.dto;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
public class AcceptanceDto extends BaseEntity {

    public String id;
    public String item;
    public String result;
    public String noItemValue;
    public String isItemValue;
    public String conclusion;
    public String defaultValue;
    public ValueEntity defaultValueType;

    public String category;
    public String total;

}
