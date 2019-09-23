package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/7/30
 * Email:wangshizhan@supcom.com
 */
public class RoleEntity extends BaseEntity {

    public long id;
    public String roleSource;
    public UserInfo user;
    public Role role;


    public class Role extends BaseEntity{

        public String code;
        public long id;
        public String name;
        public String version;

    }
}
