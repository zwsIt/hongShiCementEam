package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/10/16
 * Email:wangshizhan@supcom.com
 */
public class IDEntity extends BaseEntity {

    public IDEntity(Long id){

        if(id!=null)
            this.id = String.valueOf(id);
    }

    public IDEntity(String id){

        if(id!=null)
            this.id = id;
    }

    public String id;

}
