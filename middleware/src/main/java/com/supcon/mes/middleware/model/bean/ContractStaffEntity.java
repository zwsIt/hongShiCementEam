package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.util.PinYinUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * ContractStaffEntity 人员
 * created by zhangwenshuai1 2018/8/13
 */
@Entity
public class ContractStaffEntity extends BaseEntity implements CommonSearchEntity {
    @Id
    @SerializedName("STAFFID")
    public Long id;
    @SerializedName("CODE")
    public String code;
    @SerializedName("NAME")
    public String name;
    @SerializedName("PICTURE")
    public String picture;
    public String pinyin;
    public String ip = MBapApp.getIp();

    @Generated(hash = 286401910)
    public ContractStaffEntity(Long id, String code, String name, String picture, String pinyin, String ip) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.picture = picture;
        this.pinyin = pinyin;
        this.ip = ip;
    }

    @Generated(hash = 33452468)
    public ContractStaffEntity() {
    }

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
//        char headLetter = pinyin==null?PinYinUtils.getHeaderLetter(name):pinyin.toUpperCase().charAt(0);
        return PinYinUtils.getHeaderLetter(pinyin==null?name:pinyin)+"";
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

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPinyin() {
        return this.pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
