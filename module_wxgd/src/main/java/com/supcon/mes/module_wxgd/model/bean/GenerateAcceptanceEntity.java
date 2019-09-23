package com.supcon.mes.module_wxgd.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/2
 * ------------- Description -------------
 */
public class GenerateAcceptanceEntity extends BaseEntity {


    public boolean success;
    public String errMsg;

    public Long checkApplyId;
    public String tableNo;
}
