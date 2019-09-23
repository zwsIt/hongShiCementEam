package com.supcon.mes.middleware.model.inter;

/**
 * @Author xushiyun
 * @Create-time 7/8/19
 * @Pageage com.supcon.mes.middleware.model.inter
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public interface ITxlEntity{
    /**
     * 列表中显示的数据信息
     */
    String getStaffCode();
    String getStaffName();
    String getSex();
    String getDepartmentName();
    String getDepartmentFullPath();
    String getStaffWork();
    String getStaffBirthday();
    String getDeploymentTime();
    String getCompanyName();
    Long getStaffId();
    
    /**
     * 详情中显示的数据信息
     */
    
}
