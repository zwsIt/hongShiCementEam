package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangshizhan on 2018/3/24.
 * Email:wangshizhan@supcon.com
 */
@Entity
public class EamHistoryEntity extends BaseEntity {

    @SerializedName("WORK_ITEMID")
    public Long itemId;

    @SerializedName("CONCLUSE")
    public String conclusion;

    @SerializedName("CONCLUSE_TIME")
    public String conclusionDate;

    @SerializedName("REAL_VALUE")
    public String realValue;

    @Generated(hash = 2133110552)
    public EamHistoryEntity(Long itemId, String conclusion, String conclusionDate,
            String realValue) {
        this.itemId = itemId;
        this.conclusion = conclusion;
        this.conclusionDate = conclusionDate;
        this.realValue = realValue;
    }

    @Generated(hash = 1690881913)
    public EamHistoryEntity() {
    }

    public Long getItemId() {
        return this.itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getConclusion() {
        return this.conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getConclusionDate() {
        return this.conclusionDate;
    }

    public void setConclusionDate(String conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public String getRealValue() {
        return this.realValue;
    }

    public void setRealValue(String realValue) {
        this.realValue = realValue;
    }

    

}
