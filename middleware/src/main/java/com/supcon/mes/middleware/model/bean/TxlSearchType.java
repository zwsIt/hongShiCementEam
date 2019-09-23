package com.supcon.mes.middleware.model.bean;

/**
 * @Author xushiyun
 * @Create-time 8/12/19
 * @Pageage com.supcon.mes.middleware.model.bean
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public enum TxlSearchType {
    STAFF_CODE("员工编号"),
    SRAFF_NAME("员工姓名"),
    DEPARTMENT_NAME("部门名称"),
    POSITION_NAME("岗位名称"),
    ;
    String typeName;
    
    
    
    TxlSearchType(String typeName) {
        this.typeName = typeName;
    }
    
}