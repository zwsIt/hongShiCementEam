package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by zhangwenshuai1 on 2018/5/14.
 * 巡检项免检实体表
 */

@Entity
public class XJExemptionEntity extends BaseEntity {

    @Id
    public Long id;

    @Unique
    public Long exemptionId; //免检明细id

    public Long itemId; //巡检项id

    @SerializedName("nextItemId")
    public Long exemptionItemId; //免检项id

    @SerializedName("concluse")
    public String result; //巡检项结果

    public String ip = MBapApp.getIp();

    @Generated(hash = 17247614)
    public XJExemptionEntity(Long id, Long exemptionId, Long itemId,
            Long exemptionItemId, String result, String ip) {
        this.id = id;
        this.exemptionId = exemptionId;
        this.itemId = itemId;
        this.exemptionItemId = exemptionItemId;
        this.result = result;
        this.ip = ip;
    }

    @Generated(hash = 966801102)
    public XJExemptionEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExemptionId() {
        return this.exemptionId;
    }

    public void setExemptionId(Long exemptionId) {
        this.exemptionId = exemptionId;
    }

    public Long getItemId() {
        return this.itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getExemptionItemId() {
        return this.exemptionItemId;
    }

    public void setExemptionItemId(Long exemptionItemId) {
        this.exemptionItemId = exemptionItemId;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    

}
