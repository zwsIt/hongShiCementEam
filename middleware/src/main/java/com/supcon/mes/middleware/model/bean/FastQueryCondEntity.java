package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.util.List;

/**
 * FastQueryCondEntity  网络接口快速查询参数实体
 * created by zhangwenshuai1 2018/8/8
 */
public class FastQueryCondEntity extends BaseEntity {

    public String modelAlias;

    public List<BaseSubcondEntity> subconds;

}
