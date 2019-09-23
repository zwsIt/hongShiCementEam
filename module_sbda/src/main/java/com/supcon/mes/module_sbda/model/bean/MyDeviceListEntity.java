package com.supcon.mes.module_sbda.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;

import java.util.List;

/**
 * Created by wangshizhan on 2017/11/21.
 * Email:wangshizhan@supcon.com
 */

public class MyDeviceListEntity extends ResultEntity {

    @SerializedName("result")
    public List<CommonDeviceEntity> devices;
}
