package com.supcon.mes.module_data_manage.model.bean;

import com.supcon.mes.middleware.model.bean.ResultEntity;

/**
 * Created by wangshizhan on 2018/4/28.
 * Email:wangshizhan@supcon.com
 */

public class DataResultEntity extends ResultEntity {
    public DataResultEntity(boolean isSuccess){
        success = isSuccess;
    }
}
