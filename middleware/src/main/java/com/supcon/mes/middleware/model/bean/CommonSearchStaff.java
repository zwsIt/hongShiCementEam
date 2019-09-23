package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.util.PinYinUtils;

/**
 * CommonSearchStaff 人员
 * created by zhangwenshuai1 2018/8/13
 */

public class CommonSearchStaff extends BaseEntity implements CommonSearchEntity {

    public Long id;
    public String code;
    public String name;
    public String pinyin;
    public String ip = MBapApp.getIp();

    public Long userId;

    @Override
    public String getSearchId() {
        return this.id == null ? null : this.id.toString();
    }

    @Override
    public String getSearchName() {
        return this.name;
    }

    @Override
    public String getSearchProperty() {
        return code;
    }

    @Override
    public String getSearchPinyin() {
        return PinYinUtils.getHeaderLetter(pinyin == null ? name : pinyin) + "";
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

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPinyin() {
        return this.pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

}
