package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * StandingCropEntity 现存量更新返回实体
 * created by zhangwenshuai1 2018/10/10
 */
public class StandingCropEntity extends BaseEntity {
    public String productCode; // 备件编码
    public String useQuantity; // 现存量
}
