package com.supcon.mes.module_score.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/9/14
 * Email zhangwenshuai1@supcon.com
 * Desc 评分记录实体
 */
public class ScoreRecordEntity extends BaseEntity {

    /**
     * afterScore : 93
     * attrMap : null
     * beforeScore : 100
     * cid : 1000
     * eamId : {"id":null,"name":null}
     * id : 1032
     * operateStaffId : {"id":1003,"name":"mxq"}
     * operateTime : 1599811940695
     * patrolWorker : {"id":1008,"name":"赵六"}
     * scoreData : 1599753600000
     * scoreType : {"id":"BEAM_065/02","value":"巡检个人评分"}
     * status : null
     * tableInfoId : null
     * tableNo : null
     * version : 0
     */

    private BigDecimal afterScore; // 修改后得分
    private Object attrMap;
    private BigDecimal beforeScore; // 修改前得分
    private Long cid;
    private EamEntity eamId;
    private Long id;
    private Staff operateStaffId; // 修改操作人
    private Long operateTime; // 操作时间
    private Staff patrolWorker;
    private Long scoreData; // 评分日期
    private SystemCodeEntity scoreType; // 评分类别
    private Long tableInfoId;
    private String tableNo;
    private int version;

    public BigDecimal getAfterScore() {
        return afterScore;
    }

    public void setAfterScore(BigDecimal afterScore) {
        this.afterScore = afterScore;
    }

    public Object getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Object attrMap) {
        this.attrMap = attrMap;
    }

    public BigDecimal getBeforeScore() {
        return beforeScore;
    }

    public void setBeforeScore(BigDecimal beforeScore) {
        this.beforeScore = beforeScore;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public EamEntity getEamId() {
        return eamId;
    }

    public void setEamId(EamEntity eamId) {
        this.eamId = eamId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff getOperateStaffId() {
        return operateStaffId;
    }

    public void setOperateStaffId(Staff operateStaffId) {
        this.operateStaffId = operateStaffId;
    }

    public Long getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Long operateTime) {
        this.operateTime = operateTime;
    }

    public Staff getPatrolWorker() {
        return patrolWorker;
    }

    public void setPatrolWorker(Staff patrolWorker) {
        this.patrolWorker = patrolWorker;
    }

    public Long getScoreData() {
        return scoreData;
    }

    public void setScoreData(Long scoreData) {
        this.scoreData = scoreData;
    }

    public SystemCodeEntity getScoreType() {
        return scoreType;
    }

    public void setScoreType(SystemCodeEntity scoreType) {
        this.scoreType = scoreType;
    }

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
