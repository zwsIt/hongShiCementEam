package com.supcon.mes.middleware.model.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.util.Util;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by wangshizhan on 2018/4/4.
 * Email:wangshizhan@supcon.com
 */
@Entity
public class DepartmentInfo extends BaseEntity implements CommonSearchEntity, Cloneable, Comparable<DepartmentInfo> {
    @Id
    public Long id;
    public String name;
    public String code;
    public String searchPinyin;
    public String layRec;
    public String fullPathName;
    public Integer layNo; // 层次
    public long useFre;
    public long cid;

    @Transient
    public long parentId;

    @Transient
    public ContactEntity userInfo;


    @Generated(hash = 886111425)
    public DepartmentInfo(Long id, String name, String code, String searchPinyin, String layRec, String fullPathName,
            Integer layNo, long useFre, long cid) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.searchPinyin = searchPinyin;
        this.layRec = layRec;
        this.fullPathName = fullPathName;
        this.layNo = layNo;
        this.useFre = useFre;
        this.cid = cid;
    }

    @Generated(hash = 1148802588)
    public DepartmentInfo() {
    }


    @Override
    public DepartmentInfo clone() throws CloneNotSupportedException {
        return (DepartmentInfo) super.clone();
    }


    public int getFullPathNameLen() {
        if (TextUtils.isEmpty(fullPathName)) {
            return 0;
        } else {
            return Util.countStr(fullPathName, "/");
        }
    }


    @Override
    public int compareTo(@NonNull DepartmentInfo o) {
        return this.getFullPathNameLen() - o.getFullPathNameLen();
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

    @Override
    public String getSearchId() {
        return null;
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
        return fullPathName;
    }

    public String getSearchPinyin() {
        return this.searchPinyin;
    }

    public void setSearchPinyin(String searchPinyin) {
        this.searchPinyin = searchPinyin;
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

    public long getUseFre() {
        return this.useFre;
    }

    public void setUseFre(long useFre) {
        this.useFre = useFre;
    }

    public long getCid() {
        return this.cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getParentId() {
        if (!layRec.contains("-")) {
            return 0;
        }
        try {
            String[] split = layRec.split("-");
            parentId = Integer.parseInt(layRec.split("-")[split.length - 2]);
        } catch (Exception e) {

        }
        return this.parentId;
    }

    public Integer getLayNo() {
        return this.layNo;
    }

    public void setLayNo(Integer layNo) {
        this.layNo = layNo;
    }

}
