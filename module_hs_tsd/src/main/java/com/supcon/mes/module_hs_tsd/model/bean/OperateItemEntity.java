package com.supcon.mes.module_hs_tsd.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/10
 * Email zhangwenshuai1@supcon.com
 * Desc 操作事项(停/送电工作票PT)
 */
public class OperateItemEntity extends BaseEntity {

    /**
     * caution : 核对工作票内容，检查现场控制按钮钥匙在中控操作位置
     * cautionHeadId : {"id":1042}
     * id : 1448
     * sort : null
     * version : 0
     */

    private String caution; // 安全操作注意事项
    private ElectricityOffOnEntity cautionHeadId; // 停送电申请表头
    private Long id;
    private int sort;
    private int version;
    private String remark;

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public ElectricityOffOnEntity getCautionHeadId() {
        return cautionHeadId;
    }

    public void setCautionHeadId(ElectricityOffOnEntity cautionHeadId) {
        this.cautionHeadId = cautionHeadId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
