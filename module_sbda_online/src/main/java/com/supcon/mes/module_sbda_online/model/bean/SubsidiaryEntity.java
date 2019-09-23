package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AttachEamEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 */
public class SubsidiaryEntity extends BaseEntity {
    public AttachEamEntity attachEamId;
    public String attachMemo;
    public float sum;

    public AttachEamEntity getAttachEamId() {
        if (attachEamId == null) {
            attachEamId = new AttachEamEntity();
        }
        return attachEamId;
    }
}
