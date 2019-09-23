package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.util.List;

/**
 * Created by wangshizhan on 2017/8/15.
 */

public class CommonSearchListEntity<T extends CommonSearchEntity> extends BaseEntity {

    public boolean success;

    public String errMsg;

    public List<T> result;

}
