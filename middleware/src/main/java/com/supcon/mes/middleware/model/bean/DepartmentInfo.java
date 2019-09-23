package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.util.PinYinUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by wangshizhan on 2018/4/4.
 * Email:wangshizhan@supcon.com
 */
@Entity
public class DepartmentInfo extends BaseEntity implements CommonSearchEntity, Cloneable {
    @Id
    public Long id;
    public String name;
    public String code;
    public String searchPinyin;
    public String layRec;
    public String fullPathName;
    public long useFre;
    @Transient
    public TxlEntity userInfo;
    
    @Override
    public DepartmentInfo clone() throws CloneNotSupportedException {
        return (DepartmentInfo) super.clone();
    }
    
    @Generated(hash = 1297516717)
    public DepartmentInfo(Long id, String name, String code, String searchPinyin,
                          String layRec, String fullPathName, long useFre) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.searchPinyin = searchPinyin;
        this.layRec = layRec;
        this.fullPathName = fullPathName;
        this.useFre = useFre;
    }
    
    @Generated(hash = 1148802588)
    public DepartmentInfo() {
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
    public String getSearchId() {
        return id + "";
    }
    
    @Override
    public String getSearchPinyin() {
        return searchPinyin == null ? PinYinUtils.getPinyin(name) : searchPinyin;
    }
    
    
    public void setSearchPinyin(String searchPinyin) {
        this.searchPinyin = searchPinyin;
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
    
    public long getUseFre() {
        return this.useFre;
    }
    
    public void setUseFre(long useFre) {
        this.useFre = useFre;
    }
    
    
}
