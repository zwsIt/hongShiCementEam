package com.supcon.mes.module_olxj.model.bean;


import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.Unit;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
public class OLXJInputStandard extends BaseEntity {
    public long id;
    public String decimalPlace;
    public String name;
    public String standardCode;
    public Unit unitID;
    public String valueName;

    public SystemCodeEntity editTypeMoblie;
    public SystemCodeEntity valueTypeMoblie;
}
