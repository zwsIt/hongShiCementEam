package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;

public class NFCEvent extends BaseEntity {

    private String nfc;
    private String tag;

    public NFCEvent(String nfc) {
        this.nfc = nfc;
    }

    public NFCEvent(String nfc, String tag) {
        this.nfc = nfc;
        this.tag = tag;
    }

    public String getNfc() {
        return nfc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
