package com.supcon.mes.module_data_manage.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;

import java.util.List;

/**
 * Created by wangshizhan on 2017/11/21.
 * Email:wangshizhan@supcon.com
 */

public class AlllDeviceResultEntity extends ResultEntity {

    public AlllDeviceResult result;

    public class AlllDeviceResult extends BaseEntity {
        @SerializedName("BEAM060/01")
        public List<CommonDeviceEntity> BEAM060_01;

        @SerializedName("BEAM060/02")
        public List<CommonDeviceEntity> BEAM060_02;

        @SerializedName("BEAM060/03")
        public List<CommonDeviceEntity> BEAM060_03;

        @SerializedName("BEAM060/04")
        public List<CommonDeviceEntity> BEAM060_04;

        @SerializedName("BEAM060/05")
        public List<CommonDeviceEntity> BEAM060_05;

        @SerializedName("BEAM060/06")
        public List<CommonDeviceEntity> BEAM060_06;

        @SerializedName("BEAM060/07")
        public List<CommonDeviceEntity> BEAM060_07;

        @SerializedName("BEAM060/08")
        public List<CommonDeviceEntity> BEAM060_08;

        @SerializedName("BEAM060/09")
        public List<CommonDeviceEntity> BEAM060_09;

        @SerializedName("BEAM060/10")
        public List<CommonDeviceEntity> BEAM060_10;

        @SerializedName("BEAM060/11")
        public List<CommonDeviceEntity> BEAM060_11;

        @SerializedName("BEAM060/12")
        public List<CommonDeviceEntity> BEAM060_12;

        @SerializedName("BEAM060/13")
        public List<CommonDeviceEntity> BEAM060_13;
    }
}
