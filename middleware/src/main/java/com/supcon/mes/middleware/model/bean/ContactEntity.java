package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangshizhan on 2019/12/3
 * Email:wangshizhan@supcom.com
 */
@Entity
public class ContactEntity extends BaseEntity implements CommonSearchEntity{

    @Id
    private Long STAFFID;
    private String PICTURE;
    private String BIRTHDAY;
    private int POSITIONWORKID;
    private String NAME;//员工姓名
    private int LAYNO;
    private String COMPANYNAME;
    private String SECURITY_CLASS;
    private String FJHM;
    private String POSITIONNAME;//岗位
    private String ZKXNH;
    private String DEPARTMENTNAME;//部门名称
    private int SORT;
    private int VERSION;
    private String MOBILE;
    private String FULLPATHNAME;
    private String SEX;
    private String RZSJ;
    private String EMAIL;
    private String CODE;//员工编号

    public long updateTime;
    public String searchPinyin;

    @Generated(hash = 1590466773)
    public ContactEntity(Long STAFFID, String PICTURE, String BIRTHDAY,
            int POSITIONWORKID, String NAME, int LAYNO, String COMPANYNAME,
            String SECURITY_CLASS, String FJHM, String POSITIONNAME, String ZKXNH,
            String DEPARTMENTNAME, int SORT, int VERSION, String MOBILE,
            String FULLPATHNAME, String SEX, String RZSJ, String EMAIL, String CODE,
            long updateTime, String searchPinyin) {
        this.STAFFID = STAFFID;
        this.PICTURE = PICTURE;
        this.BIRTHDAY = BIRTHDAY;
        this.POSITIONWORKID = POSITIONWORKID;
        this.NAME = NAME;
        this.LAYNO = LAYNO;
        this.COMPANYNAME = COMPANYNAME;
        this.SECURITY_CLASS = SECURITY_CLASS;
        this.FJHM = FJHM;
        this.POSITIONNAME = POSITIONNAME;
        this.ZKXNH = ZKXNH;
        this.DEPARTMENTNAME = DEPARTMENTNAME;
        this.SORT = SORT;
        this.VERSION = VERSION;
        this.MOBILE = MOBILE;
        this.FULLPATHNAME = FULLPATHNAME;
        this.SEX = SEX;
        this.RZSJ = RZSJ;
        this.EMAIL = EMAIL;
        this.CODE = CODE;
        this.updateTime = updateTime;
        this.searchPinyin = searchPinyin;
    }

    @Generated(hash = 393979869)
    public ContactEntity() {
    }
    
    @Override
    public String getSearchId() {
        return String.valueOf(STAFFID);
    }

    @Override
    public String getSearchName() {
        return NAME;
    }

    @Override
    public String getSearchCode() {
        return CODE;
    }

    @Override
    public String getSearchProperty() {
        return DEPARTMENTNAME;
    }

    public Long getSTAFFID() {
        return this.STAFFID;
    }

    public void setSTAFFID(Long STAFFID) {
        this.STAFFID = STAFFID;
    }

    public String getPICTURE() {
        return this.PICTURE;
    }

    public void setPICTURE(String PICTURE) {
        this.PICTURE = PICTURE;
    }

    public String getBIRTHDAY() {
        return this.BIRTHDAY;
    }

    public void setBIRTHDAY(String BIRTHDAY) {
        this.BIRTHDAY = BIRTHDAY;
    }

    public int getPOSITIONWORKID() {
        return this.POSITIONWORKID;
    }

    public void setPOSITIONWORKID(int POSITIONWORKID) {
        this.POSITIONWORKID = POSITIONWORKID;
    }

    public String getNAME() {
        return this.NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getLAYNO() {
        return this.LAYNO;
    }

    public void setLAYNO(int LAYNO) {
        this.LAYNO = LAYNO;
    }

    public String getCOMPANYNAME() {
        return this.COMPANYNAME;
    }

    public void setCOMPANYNAME(String COMPANYNAME) {
        this.COMPANYNAME = COMPANYNAME;
    }

    public String getSECURITY_CLASS() {
        return this.SECURITY_CLASS;
    }

    public void setSECURITY_CLASS(String SECURITY_CLASS) {
        this.SECURITY_CLASS = SECURITY_CLASS;
    }

    public String getFJHM() {
        return this.FJHM;
    }

    public void setFJHM(String FJHM) {
        this.FJHM = FJHM;
    }

    public String getPOSITIONNAME() {
        return this.POSITIONNAME;
    }

    public void setPOSITIONNAME(String POSITIONNAME) {
        this.POSITIONNAME = POSITIONNAME;
    }

    public String getZKXNH() {
        return this.ZKXNH;
    }

    public void setZKXNH(String ZKXNH) {
        this.ZKXNH = ZKXNH;
    }

    public String getDEPARTMENTNAME() {
        return this.DEPARTMENTNAME;
    }

    public void setDEPARTMENTNAME(String DEPARTMENTNAME) {
        this.DEPARTMENTNAME = DEPARTMENTNAME;
    }

    public int getSORT() {
        return this.SORT;
    }

    public void setSORT(int SORT) {
        this.SORT = SORT;
    }

    public int getVERSION() {
        return this.VERSION;
    }

    public void setVERSION(int VERSION) {
        this.VERSION = VERSION;
    }

    public String getMOBILE() {
        return this.MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getFULLPATHNAME() {
        return this.FULLPATHNAME;
    }

    public void setFULLPATHNAME(String FULLPATHNAME) {
        this.FULLPATHNAME = FULLPATHNAME;
    }

    public String getSEX() {
        return this.SEX;
    }

    public void setSEX(String SEX) {
        this.SEX = SEX;
    }

    public String getRZSJ() {
        return this.RZSJ;
    }

    public void setRZSJ(String RZSJ) {
        this.RZSJ = RZSJ;
    }

    public String getEMAIL() {
        return this.EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getCODE() {
        return this.CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getSearchPinyin() {
        return this.searchPinyin;
    }

    public void setSearchPinyin(String searchPinyin) {
        this.searchPinyin = searchPinyin;
    }
}
