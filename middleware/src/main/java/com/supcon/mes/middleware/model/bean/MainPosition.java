package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/7/12
 * Email:wangshizhan@supcom.com
 */
public class MainPosition extends BaseEntity {

    public Company company;
    public Department department;
    public String name;

    public Department getDepartment() {
        if (department == null) {
            department = new Department();
        }
        return department;
    }
}
