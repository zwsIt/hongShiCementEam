package com.supcon.mes.module_wxgd.model.dto;

import com.supcon.common.com_http.BaseEntity;

/**
 * GoodDto 物品基础模型传输对象
 * created by zhangwenshuai1 2018/9/5
 */
public class SparePartReceiveDto extends BaseEntity {
    public IdDto sparePartId;
    public String origDemandQuity;
    public String currDemandQuity;
    public String defference;
    public String remark;
}
