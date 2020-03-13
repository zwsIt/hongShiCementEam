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
public class Company extends BaseEntity{
    @Id
    public Long id;
    
    public String name;
    public String shortName;
    public String code;
    public String address;
    public String site;
    public String telephone;
    public String email;
    @Generated(hash = 378846159)
    public Company(Long id, String name, String shortName, String code,
            String address, String site, String telephone, String email) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.code = code;
        this.address = address;
        this.site = site;
        this.telephone = telephone;
        this.email = email;
    }
    @Generated(hash = 1096856789)
    public Company() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getShortName() {
        return this.shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getSite() {
        return this.site;
    }
    public void setSite(String site) {
        this.site = site;
    }
    public String getTelephone() {
        return this.telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
