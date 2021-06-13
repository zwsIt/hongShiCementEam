package com.supcon.mes.module_olxj.model.bean;

import com.supcon.common.com_http.BaseEntity;

public class CompletedPeopleActual extends BaseEntity {
    private String code;
    private Long id;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
