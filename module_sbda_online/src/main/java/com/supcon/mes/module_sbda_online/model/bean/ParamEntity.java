package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;

public class ParamEntity extends BaseEntity {

   /* "eamId": {
        "id": 1419
    },
            "id": 1462,
            "paramName": "输送能力",
            "paramValue": "380~450",
            "sort": 0,
            "unit": "t/h",
            "version": 0
           */
   public String paramName;//参数名称
   public String paramValue;//值范围
   public String unit;//单位

}
