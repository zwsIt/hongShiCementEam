package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.inter.ITxlEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @Author xushiyun
 * @Create-time 7/8/19
 * @Pageage com.supcon.mes.module_txl.model.bean
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Entity
public class TxlEntity extends BaseEntity implements ITxlEntity {
    /**
     * PICTURE : null
     * STAFFID : 1001
     * BIRTHDAY : null
     * POSITIONWORKID : 1000
     * NAME : 马晓倩
     * LAYNO : 1
     * COMPANYNAME : 默认公司
     * SECURITY_CLASS : null
     * FJHM : null
     * POSITIONNAME : 开发工程师
     * ZKXNH : null
     * DEPARTMENTNAME : 开发一部
     * SORT : 2147483647
     * VERSION : 2
     * MOBILE : null
     * FULLPATHNAME : 开发部/开发一部
     * SEX : SEX_NATURE/FEMALE
     * RZSJ : null
     * EMAIL : null
     * CODE : 101
     */
    @SerializedName("PICTURE")
    private String PICTURE;
    @Id
    @SerializedName("STAFFID")
    private Long STAFFID;
    @SerializedName("BIRTHDAY")
    private String BIRTHDAY;
    @SerializedName("POSITIONWORKID")
    private int POSITIONWORKID;
    @SerializedName("NAME")
    private String NAME;
    @SerializedName("LAYNO")
    private int LAYNO;
    @SerializedName("COMPANYNAME")
    private String COMPANYNAME;
    @SerializedName("SECURITY_CLASS")
    private String SECURITY_CLASS;
    @SerializedName("FJHM")
    private String FJHM;
    @SerializedName("POSITIONNAME")
    private String POSITIONNAME;
    @SerializedName("ZKXNH")
    private String ZKXNH;
    @SerializedName("DEPARTMENTNAME")
    private String DEPARTMENTNAME;
    @SerializedName("SORT")
    private int SORT;
    @SerializedName("VERSION")
    private int VERSION;
    @SerializedName("MOBILE")
    private String MOBILE;
    @SerializedName("FULLPATHNAME")
    private String FULLPATHNAME;
    @SerializedName("SEX")
    private String SEX;
    @SerializedName("RZSJ")
    private String RZSJ;
    @SerializedName("EMAIL")
    private String EMAIL;
    @SerializedName("CODE")
    private String CODE;



    @Generated(hash = 1823368555)
    public TxlEntity(String PICTURE, Long STAFFID, String BIRTHDAY,
            int POSITIONWORKID, String NAME, int LAYNO, String COMPANYNAME,
            String SECURITY_CLASS, String FJHM, String POSITIONNAME, String ZKXNH,
            String DEPARTMENTNAME, int SORT, int VERSION, String MOBILE,
            String FULLPATHNAME, String SEX, String RZSJ, String EMAIL,
            String CODE) {
        this.PICTURE = PICTURE;
        this.STAFFID = STAFFID;
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
    }

    @Generated(hash = 1313980679)
    public TxlEntity() {
    }

   
    
    @Override
    public String getStaffCode() {
        return CODE;
    }
    
    @Override
    public String getStaffName() {
        return NAME;
    }
    
    @Override
    public String getSex() {
        return SEX;
    }
    
    @Override
    public String getDepartmentName() {
        return DEPARTMENTNAME;
    }
    
    @Override
    public String getDepartmentFullPath() {
        return FULLPATHNAME;
    }
    
    @Override
    public String getPositionName() {
        return POSITIONNAME;
    }
    
    @Override
    public String getStaffBirthday() {
        return BIRTHDAY+"";
    }
    
    @Override
    public String getDeploymentTime() {
        return "";
    }
    
    @Override
    public Long getStaffId() {
        return Long.valueOf(STAFFID);
    }
    
    @Override
    public String getCompanyName() {
        return COMPANYNAME;
    }

    public String getPICTURE() {
        return this.PICTURE;
    }

    public void setPICTURE(String PICTURE) {
        this.PICTURE = PICTURE;
    }

    public Long getSTAFFID() {
        return this.STAFFID;
    }

    public void setSTAFFID(Long STAFFID) {
        this.STAFFID = STAFFID;
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
}
