package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2019/5/28
 * Email:wangshizhan@supcom.com
 */
public class DeviceDCSEntity extends BaseEntity {

    public String name;
    public String itemNumber;
    //BEAM053/01 	 开关信号,BEAM053/02  非开关信号,BEAM053/03 温度,BEAM053/04 压力
    public String valueType;
    public String latestValue;
    public String maxValue;
    public String minValue;

    @Expose
    public boolean isFinished;
}
