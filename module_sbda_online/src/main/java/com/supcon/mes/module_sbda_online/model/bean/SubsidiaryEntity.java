package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AttachEamEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 附属设备
 */
public class SubsidiaryEntity extends BaseEntity {
    public EamEntity attachEamId;
    public String attachMemo;
    public float sum;

    public EamEntity getAttachEamId() {
        if (attachEamId == null) {
            attachEamId = new EamEntity();
        }
        return attachEamId;
    }
}
