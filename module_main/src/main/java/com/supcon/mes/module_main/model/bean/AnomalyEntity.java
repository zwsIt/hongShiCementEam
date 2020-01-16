package com.supcon.mes.module_main.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/9/17
 * ------------- Description -------------
 */
public class AnomalyEntity extends BaseEntity {
    public String state;
    public String tableno;
    @SerializedName(value = "worktableno")
    public String workTableNo;  // 单据编号, 统一使用
    @SerializedName(value = "soucretype")
    public String sourceType; // 来源
    @SerializedName(value = "staffname")
    public String staffName; // 待办人名称
    public String content;
    @SerializedName(value = "creatime")
    public Long createTime; // 创建时间

    @SerializedName(value = "eamcode")
    public String eamCode; // 设备编码
    @SerializedName(value = "eamname")
    public String eamName; // 设备名称
    @SerializedName(value = "dataid")
    public Long dataId; // 数据ID
    @SerializedName(value = "openurl")
    public String openUrl; // url地址
    @SerializedName(value = "pendingid")
    public Long pendingId; // 待办ID
    @SerializedName(value = "processkey")
    public String processKey; // 单据流程关键字

}
