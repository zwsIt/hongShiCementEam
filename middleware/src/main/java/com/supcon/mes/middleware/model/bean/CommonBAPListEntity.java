package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/7/10
 * Email:wangshizhan@supcom.com
 */
public class CommonBAPListEntity<T extends BaseEntity> extends CommonListEntity<T> {

    public int first;
    public boolean hasNext;
    public int pageNo;
    public int pageSize;


    public int totalCount;
    public int totalPages;

}
