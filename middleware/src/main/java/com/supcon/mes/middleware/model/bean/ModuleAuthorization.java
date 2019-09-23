package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * ModuleAuthorization
 * Created by zhangwenshuai1 on 2019/3/5
 * Eamil zhangwenshuai1@supcon.com
 * Desc   模块权限是否授权存储
 */
@Entity
public class ModuleAuthorization extends BaseEntity {

    @Id(autoincrement = true)
    public Long id;

    @Unique
    public String moduleCode;

    public boolean isAuthorized;

    @Generated(hash = 1564037843)
    public ModuleAuthorization(Long id, String moduleCode, boolean isAuthorized) {
        this.id = id;
        this.moduleCode = moduleCode;
        this.isAuthorized = isAuthorized;
    }

    @Generated(hash = 314713623)
    public ModuleAuthorization() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleCode() {
        return this.moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public boolean getIsAuthorized() {
        return this.isAuthorized;
    }

    public void setIsAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

}
