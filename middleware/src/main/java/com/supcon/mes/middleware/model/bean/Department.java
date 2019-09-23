package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangshizhan on 2018/7/12
 * Email:wangshizhan@supcom.com
 */
@Entity
public class Department extends BaseEntity{

    @Id
    public Long id;
    public String code;
    public String name;
    @Generated(hash = 1822774779)
    public Department(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
    @Generated(hash = 355406289)
    public Department() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }


}
