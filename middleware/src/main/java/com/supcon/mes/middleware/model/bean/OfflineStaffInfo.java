package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangshizhan on 2018/4/4.
 * Email:wangshizhan@supcon.com
 */
@Entity
public class OfflineStaffInfo extends BaseEntity implements CommonSearchEntity {

    @Id
    public Long id;
    public String name;
    public String code;
    public long departmentID;
    public String namePinyin;

    @Override
    public String getSearchId() {
        return id==null?"":id.toString();
    }

    @Override
    public String getSearchName() {
        return name;
    }

    @Override
    public String getSearchProperty() {
        return code;
    }

    @Override
    public String getSearchPinyin() {
        return namePinyin;
    }

    public String host = MBapApp.getIp(); //ip 或域名,在初始化时直接使用对应的当前IP进行操作

    @Generated(hash = 1784534272)
    public OfflineStaffInfo(Long id, String name, String code, long departmentID,
            String namePinyin, String host) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.departmentID = departmentID;
        this.namePinyin = namePinyin;
        this.host = host;
    }

    @Generated(hash = 183710187)
    public OfflineStaffInfo() {
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

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getDepartmentID() {
        return this.departmentID;
    }

    public void setDepartmentID(long departmentID) {
        this.departmentID = departmentID;
    }

    public String getNamePinyin() {
        return this.namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }


}
