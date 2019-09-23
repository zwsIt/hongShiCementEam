package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by zhangwenshuai1 on 2018/4/24.
 * 历史
 */

@Entity
public class XJHistoryEntity extends BaseEntity {

    @Id(autoincrement = true)
    public Long id;

    public long taskId;  //任务ID

    public long areaId; //区域ID

    public long workItemId;  //巡检项

    public String content; // 内容

    public String result; // 结果

    public String conclusion; //结论

    @Unique
    public String dateTime; //日期时间

    public Long eamId; //设备id

    public String eamName; //设备名称

    public String linkStateId;  //明细状态id

    public String ip = MBapApp.getIp();

    @Generated(hash = 350106227)
    public XJHistoryEntity(Long id, long taskId, long areaId, long workItemId,
            String content, String result, String conclusion, String dateTime,
            Long eamId, String eamName, String linkStateId, String ip) {
        this.id = id;
        this.taskId = taskId;
        this.areaId = areaId;
        this.workItemId = workItemId;
        this.content = content;
        this.result = result;
        this.conclusion = conclusion;
        this.dateTime = dateTime;
        this.eamId = eamId;
        this.eamName = eamName;
        this.linkStateId = linkStateId;
        this.ip = ip;
    }

    @Generated(hash = 539937445)
    public XJHistoryEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getAreaId() {
        return this.areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public long getWorkItemId() {
        return this.workItemId;
    }

    public void setWorkItemId(long workItemId) {
        this.workItemId = workItemId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getConclusion() {
        return this.conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Long getEamId() {
        return this.eamId;
    }

    public void setEamId(Long eamId) {
        this.eamId = eamId;
    }

    public String getEamName() {
        return this.eamName;
    }

    public void setEamName(String eamName) {
        this.eamName = eamName;
    }

    public String getLinkStateId() {
        return this.linkStateId;
    }

    public void setLinkStateId(String linkStateId) {
        this.linkStateId = linkStateId;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    



}