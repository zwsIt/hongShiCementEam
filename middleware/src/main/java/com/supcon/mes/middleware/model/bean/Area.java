package com.supcon.mes.middleware.model.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.util.PinYinUtils;
import com.supcon.mes.middleware.util.Util;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Area 区域位置
 * created by zhangwenshuai1 2018/8/13
 */
@Entity
public class Area extends BaseEntity implements CommonSearchEntity, Cloneable, Comparable<Area> {
    @Id
    public Long id;
    public String code;
    public String name;
    public String ip = MBapApp.getIp();
    public String pinyin;
    public String layRec; // 层次结构
    public String fullPathName; // 层级全路径
    public Integer layNo; // 层次
    public Long cid;

//    @Transient
    public long parentId;

    @Transient
    public EamEntity eamEntity;


    @Generated(hash = 594189475)
    public Area(Long id, String code, String name, String ip, String pinyin, String layRec,
            String fullPathName, Integer layNo, Long cid, long parentId) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.ip = ip;
        this.pinyin = pinyin;
        this.layRec = layRec;
        this.fullPathName = fullPathName;
        this.layNo = layNo;
        this.cid = cid;
        this.parentId = parentId;
    }


    @Generated(hash = 179626505)
    public Area() {
    }
    
    
    @Override
    public Area clone() throws CloneNotSupportedException {
        return (Area) super.clone();
    }


    public int getFullPathNameLen() {
        if (TextUtils.isEmpty(fullPathName)) {
            return 0;
        } else {
            return Util.countStr(fullPathName, "/");
        }
    }


    @Override
    public int compareTo(@NonNull Area o) {
        return this.layNo - o.layNo;
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
    public String getSearchCode() {
        return code;
    }

    @Override
    public String getSearchProperty() {
        return code;
    }

    public String getSearchPinyin() {
        return PinYinUtils.getHeaderLetter(pinyin == null? name:pinyin)+"";
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

    public String getLayRec() {
        return this.layRec;
    }

    public void setLayRec(String layRec) {
        this.layRec = layRec;
    }

    public String getFullPathName() {
        return this.fullPathName;
    }

    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }

    public Integer getLayNo() {
        return this.layNo;
    }

    public void setLayNo(Integer layNo) {
        this.layNo = layNo;
    }

    public Long getCid() {
        return this.cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public long getParentId() {
//        if (!layRec.contains("-")) {
//            return 0;
//        }
//        try {
//            String[] split = layRec.split("-");
//            parentId = Integer.parseInt(split[split.length - 2]);
//        } catch (Exception e) {
//
//        }
        return this.parentId;
    }


    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

}
