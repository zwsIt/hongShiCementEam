package com.supcon.mes.middleware.model.bean;

import android.text.TextUtils;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by wangshizhan on 2018/7/12
 * Email:wangshizhan@supcom.com
 */
@Entity
public class Staff extends BaseEntity implements CheckNil {

    public String code;
    public String name;

    @Transient
    public MainPosition mainPosition;

    @Override
    public boolean checkNil() {
        return TextUtils.isEmpty(code) && TextUtils.isEmpty(name);
    }

    @Id
    public Long id;

    @Generated(hash = 330618913)
    public Staff(String code, String name, Long id) {
        this.code = code;
        this.name = name;
        this.id = id;
    }

    @Generated(hash = 1774984890)
    public Staff() {
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MainPosition getMainPosition() {
        if (mainPosition == null) {
            mainPosition = new MainPosition();
        }
        return mainPosition;
    }
}
