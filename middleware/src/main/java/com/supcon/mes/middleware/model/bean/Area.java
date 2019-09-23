package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.util.PinYinUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Area 区域位置
 * created by zhangwenshuai1 2018/8/13
 */
@Entity
public class Area extends BaseEntity implements CommonSearchEntity {
    @Id
    public long id;
    public String code;
    public String name;
    public String ip = MBapApp.getIp();
    public String pinyin;


    @Generated(hash = 644338327)
    public Area(long id, String code, String name, String ip, String pinyin) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.ip = ip;
        this.pinyin = pinyin;
    }

    @Generated(hash = 179626505)
    public Area() {
    }


    @Override
    public String getSearchId() {
        return id+"";
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
        return PinYinUtils.getHeaderLetter(pinyin == null? name:pinyin)+"";
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
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
