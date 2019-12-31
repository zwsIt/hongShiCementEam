package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * EamFileEntity 设备文档
 * created by zhangwenshuai1 2019/12/29
 */
public class EamFileEntity extends BaseEntity {

    /**
     * code : 1
     * docAddress : null
     * docNameMultiFileIds : 1008
     * docNameMultiFileNames : 180120辊压机总图.png
     * docType : null
     * eamID : {"id":1075}
     * id : 1004
     * name : 辊压机总图
     * remark : null
     * sort : 0
     * sum : null
     * summary : null
     * version : 0
     */

    private String code; // 文档编号
    private String docAddress; // 存放地点
    private String docNameMultiFileIds; // 文档在baseDocument表id
    private String docNameMultiFileNames; // 文档在baseDocument表name
    private SystemCodeEntity docType; // 文档类型:BEAM008/01:设备资料; BEAM008/01:作业指导书; BEAM008/01:维修手册
    private EamEntity eamID;
    private Long id;
    private String name; // 文档名称
    private String remark;
    private int sort;
    private Integer sum; // 份数
    private String summary; // 摘要
    private int version;

    private String viewUrl; // 在线预览url

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDocAddress() {
        return docAddress;
    }

    public void setDocAddress(String docAddress) {
        this.docAddress = docAddress;
    }

    public String getDocNameMultiFileIds() {
        return docNameMultiFileIds;
    }

    public void setDocNameMultiFileIds(String docNameMultiFileIds) {
        this.docNameMultiFileIds = docNameMultiFileIds;
    }

    public String getDocNameMultiFileNames() {
        return docNameMultiFileNames;
    }

    public void setDocNameMultiFileNames(String docNameMultiFileNames) {
        this.docNameMultiFileNames = docNameMultiFileNames;
    }

    public SystemCodeEntity getDocType() {
        return docType;
    }

    public void setDocType(SystemCodeEntity docType) {
        this.docType = docType;
    }

    public EamEntity getEamID() {
        return eamID;
    }

    public void setEamID(EamEntity eamID) {
        this.eamID = eamID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }
}
