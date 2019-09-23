package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

public class AccessoryEamId extends BaseEntity {
    public AttachEamEntity attachEamId;
    public Long id;

    public AttachEamEntity getAttachEamId() {
        if (attachEamId == null) {
            attachEamId = new AttachEamEntity();
        }
        return attachEamId;
    }
}
