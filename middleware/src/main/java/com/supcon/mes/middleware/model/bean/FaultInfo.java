package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * FaultInfo 隐患信息
 * created by zhangwenshuai1 2018/8/13
 */
public class FaultInfo extends BaseEntity {
    public Long id;
    public String name;
    public String describe;//隐患现象
    public SystemCodeEntity faultInfoType;//隐患类型
    public SystemCodeEntity repairType;//维修类型
    public SystemCodeEntity priority;//优先级
    public Staff findStaffID;//发现人
    public String tableNo;//隐患单据编号
}
