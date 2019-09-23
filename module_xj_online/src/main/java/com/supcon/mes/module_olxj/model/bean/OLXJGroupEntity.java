package com.supcon.mes.module_olxj.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * Created by wangshizhan on 2019/3/29
 * Email:wangshizhan@supcom.com
 */
public class OLXJGroupEntity extends BaseEntity {

    public long id;

    public long cid;

    public String code;

    public boolean isRun;//是否启用

    public String name;

    public String remark;//备注

    public String status;

    public String tableInfoId;

    public boolean valid;

    public SystemCodeEntity valueType;//巡检类型

    public long version;
}
