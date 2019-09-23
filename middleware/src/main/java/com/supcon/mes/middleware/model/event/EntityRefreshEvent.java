package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/7/9
 * Email:wangshizhan@supcom.com
 */
public class EntityRefreshEvent<T extends BaseEntity> {

    private T entity;

    public EntityRefreshEvent(T entity){
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }
}
