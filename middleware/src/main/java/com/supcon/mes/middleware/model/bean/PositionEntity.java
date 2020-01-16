package com.supcon.mes.middleware.model.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.util.Util;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/12/19
 * ------------- Description -------------
 */
@Entity
public class PositionEntity extends BaseEntity implements CommonSearchEntity, Cloneable, Comparable<PositionEntity> {
    @Id
    public Long id;
    public String name;
    public String code;
    public String searchPinyin;
    public String layRec;
    public String fullPathName;
    public long cid;

    @Convert(converter = DepartmentPositionConverter.class, columnType = String.class)
    public DepartmentPosition department;

    @Transient
    public long parentId;

    @Transient
    public ContactEntity userInfo;

    @Generated(hash = 1150586148)
    public PositionEntity(Long id, String name, String code, String searchPinyin, String layRec, String fullPathName,
                          long cid, DepartmentPosition department) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.searchPinyin = searchPinyin;
        this.layRec = layRec;
        this.fullPathName = fullPathName;
        this.cid = cid;
        this.department = department;
    }

    @Generated(hash = 1547125250)
    public PositionEntity() {
    }

    @Override
    public PositionEntity clone() throws CloneNotSupportedException {
        return (PositionEntity) super.clone();
    }

    public int getFullPathNameLen() {
        if (TextUtils.isEmpty(fullPathName)) {
            return 0;
        } else {
            return Util.countStr(fullPathName, "/");
        }
    }

    @Override
    public int compareTo(@NonNull PositionEntity o) {
        return this.getFullPathNameLen() - o.getFullPathNameLen();
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

    public long getCid() {
        return this.cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public DepartmentPosition getDepartment() {
        return this.department;
    }

    public void setDepartment(DepartmentPosition department) {
        this.department = department;
    }


}
