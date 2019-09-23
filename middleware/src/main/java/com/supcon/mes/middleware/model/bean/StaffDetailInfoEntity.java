package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/7/30
 * Email:wangshizhan@supcom.com
 */
public class StaffDetailInfoEntity extends BaseEntity {

    @SerializedName("PICTURE")
    public String picture;

    @SerializedName("STAFFID")
    public Long staffId;

    @SerializedName("BIRTHDAY")
    public String birthday;

    @SerializedName("POSITIONWORKID")
    public Long positionWorkId;

    @SerializedName("NAME")
    public String name;

    @SerializedName("LAYNO")
    public Long layno;

    @SerializedName("POSITIONNAME")
    public String positionName;

    @SerializedName("DEPARTMENTNAME")
    public String departmentName;

    @SerializedName("FULLPATHNAME")
    public String fullPathName;

    @SerializedName("SEX")
    public String sex;

    @SerializedName("CODE")
    public String code;

}
