package com.supcon.mes.module_wxgd.model.dto;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * LubricateOilsEntityDto 润滑油实体传输对象
 * created by zhangwenshuai1 2018/9/5
 */
public class LubricateOilsEntityDto extends BaseEntity {

    public String id;
    public IdDto lubricate;
    public IdDto jwxItemID;
    public IdDto oilType;//加换油
    public String oilQuantity;
    public String remark;
    public String lubricatingPart;
}
