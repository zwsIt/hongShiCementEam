package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

public class AccessoryEamId extends BaseEntity {
//    public AttachEamEntity attachEamId;
    public EamEntity attachEamId;
    public Long id;

    public EamEntity getAttachEamId() {
        if (attachEamId == null) {
            attachEamId = new EamEntity();
        }
        return attachEamId;
    }
}
