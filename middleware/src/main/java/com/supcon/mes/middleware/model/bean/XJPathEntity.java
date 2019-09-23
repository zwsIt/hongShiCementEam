package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

@Entity(indexes = {@Index(value = "id DESC,ip DESC", unique = true)})
public class XJPathEntity extends BaseEntity {

    @Id(autoincrement = true)
    public Long index;  //序号

    public Long id;//任务ID

    public String tableNo;  //任务单据编号

    @SerializedName("workGroupId")
    public long pathId; //（巡检计划、路线、任务组）ID

    @SerializedName("workGroups")
    public String pathName;//（巡检计划、路线、任务组）名字

    @SerializedName("resstaff")
    public String responsibler;  //负责人

    public String startTime;  //开始时间

    public long startDelay;  //开始时间延迟量  整数  单位小时

    public long startAdv;  //开始时间提前量  整数  单位小时

    public String endTime;  //结束时间

    public long endDelay;  //结束时间延迟量  整数  单位小时

    public String state = "待检";  //任务完成状态，默认待检

    public String startTimeActual; //实际开始时间

    public String endTimeActual; //实际结束时间

    public boolean isStart = false; //是否开始标识

    public long resstaffId; //责任人ID

    public String host = MBapApp.getIp(); //ip 或 域名

    public String ip = MBapApp.getIp(); //ip 或 域名



    @Generated(hash = 553419342)
    public XJPathEntity(Long index, Long id, String tableNo, long pathId,
            String pathName, String responsibler, String startTime, long startDelay,
            long startAdv, String endTime, long endDelay, String state,
            String startTimeActual, String endTimeActual, boolean isStart,
            long resstaffId, String host, String ip) {
        this.index = index;
        this.id = id;
        this.tableNo = tableNo;
        this.pathId = pathId;
        this.pathName = pathName;
        this.responsibler = responsibler;
        this.startTime = startTime;
        this.startDelay = startDelay;
        this.startAdv = startAdv;
        this.endTime = endTime;
        this.endDelay = endDelay;
        this.state = state;
        this.startTimeActual = startTimeActual;
        this.endTimeActual = endTimeActual;
        this.isStart = isStart;
        this.resstaffId = resstaffId;
        this.host = host;
        this.ip = ip;
    }

    @Generated(hash = 552064286)
    public XJPathEntity() {
    }



    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableNo() {
        return this.tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public long getPathId() {
        return this.pathId;
    }

    public void setPathId(long pathId) {
        this.pathId = pathId;
    }

    public String getPathName() {
        return this.pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getResponsibler() {
        return this.responsibler;
    }

    public void setResponsibler(String responsibler) {
        this.responsibler = responsibler;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public long getStartDelay() {
        return this.startDelay;
    }

    public void setStartDelay(long startDelay) {
        this.startDelay = startDelay;
    }

    public long getStartAdv() {
        return this.startAdv;
    }

    public void setStartAdv(long startAdv) {
        this.startAdv = startAdv;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getEndDelay() {
        return this.endDelay;
    }

    public void setEndDelay(long endDelay) {
        this.endDelay = endDelay;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartTimeActual() {
        return this.startTimeActual;
    }

    public void setStartTimeActual(String startTimeActual) {
        this.startTimeActual = startTimeActual;
    }

    public String getEndTimeActual() {
        return this.endTimeActual;
    }

    public void setEndTimeActual(String endTimeActual) {
        this.endTimeActual = endTimeActual;
    }

    public boolean getIsStart() {
        return this.isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public long getResstaffId() {
        return this.resstaffId;
    }

    public void setResstaffId(long resstaffId) {
        this.resstaffId = resstaffId;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Long getIndex() {
        return this.index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
