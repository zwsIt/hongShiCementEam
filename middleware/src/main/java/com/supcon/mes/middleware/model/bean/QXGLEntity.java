package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.beans.Transition;
import com.supcon.mes.middleware.EamApplication;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * Created by wangshizhan on 2017/11/17.
 * Email:wangshizhan@supcon.com
 */

@Entity
public class QXGLEntity extends BaseEntity {
    public static final List<QXGLEntity> genUploadList() {
        return EamApplication.dao().getQXGLEntityDao().queryBuilder()
                .where(QXGLEntityDao.Properties.IsLocalFinished.eq(true),
                        QXGLEntityDao.Properties.Host.eq(MBapApp.getIp()),
                        QXGLEntityDao.Properties.StaffId.eq(EamApplication
                                .getAccountInfo().getStaffId())).list();
    }

    @Id(autoincrement = true)
    public long localId = System.currentTimeMillis();
    public long id;

    public long tableId;  //单据id
    public long pendingId; //待办id
    public long findTime = System.currentTimeMillis();  //发现时间
    public String tableNo; //单据编号
    public long eamId;   //设备id
    public String eamCode; //设备编号
    public String eamName; //设备名称
    public String eamModel; //设备型号
    public String eamType; //设备类型

    public long findStaffId;   //发现人id
    public String findStaff; //发现人
    public long planDealTime; //计划消缺时间
    public Double stopTime;    //停机时长
    public Double stopLoss;    //停机损失
    public String remark;  //备注

    public String faultName;   //缺陷名称

    public String faultTypeId; //缺陷类型id
    public String faultType;   //缺陷类型名称

    public String dealTypeId;    //处理方式id
    public String dealTypeName;  //处理方式名称
    public String dealMethod;    //处理方式
    public long modifyTime;   //修改时间
    public String description;  //缺陷描述
    public String tableStatus = "草稿";     //单据状态
    public String comment;      //填写意见

    public String router = "QXGL_EDIT";  //跳转页面路由
    public String faultPicPaths;
    public String tempFaultPicPaths;
    public Boolean recallAble; //是否允许撤回
    public Boolean allowProxyAble;   //是否允许委托
    public int dealSet; //处理意见 0可空 / 1必填 2禁填
    public boolean isLocalFinished; //是否是离线状态编辑的

    public boolean isFromXJ = false;

    public long workId;
    public long workItemId;
    public long taskId;

    public long staffId; //责任人ID

    public long repairStaffId;//维修负责人id

    public String repairStaff;//维修负责人

    public String host = MBapApp.getIp(); //ip 或域名

    public static List<QXGLEntity> genLocalFinishedEntities() {
        return EamApplication.dao().getQXGLEntityDao().queryBuilder()
                .where(QXGLEntityDao.Properties.IsLocalFinished.eq(true),
                        QXGLEntityDao.Properties.Host.eq(MBapApp.getIp()),
                        QXGLEntityDao.Properties.StaffId.eq(EamApplication
                                .getAccountInfo().getStaffId())).list();
    }


    @Transient
    public List<Transition> transitions;    //操作的迁移线信息

    @Generated(hash = 531282134)
    public QXGLEntity(long localId, long id, long tableId, long pendingId,
                      long findTime, String tableNo, long eamId, String eamCode,
                      String eamName, String eamModel, String eamType, long findStaffId,
                      String findStaff, long planDealTime, Double stopTime, Double stopLoss,
                      String remark, String faultName, String faultTypeId, String faultType,
                      String dealTypeId, String dealTypeName, String dealMethod,
                      long modifyTime, String description, String tableStatus, String comment,
                      String router, String faultPicPaths, String tempFaultPicPaths,
                      Boolean recallAble, Boolean allowProxyAble, int dealSet,
                      boolean isLocalFinished, boolean isFromXJ, long workId, long workItemId,
                      long taskId, long staffId, long repairStaffId, String repairStaff,
                      String host) {
        this.localId = localId;
        this.id = id;
        this.tableId = tableId;
        this.pendingId = pendingId;
        this.findTime = findTime;
        this.tableNo = tableNo;
        this.eamId = eamId;
        this.eamCode = eamCode;
        this.eamName = eamName;
        this.eamModel = eamModel;
        this.eamType = eamType;
        this.findStaffId = findStaffId;
        this.findStaff = findStaff;
        this.planDealTime = planDealTime;
        this.stopTime = stopTime;
        this.stopLoss = stopLoss;
        this.remark = remark;
        this.faultName = faultName;
        this.faultTypeId = faultTypeId;
        this.faultType = faultType;
        this.dealTypeId = dealTypeId;
        this.dealTypeName = dealTypeName;
        this.dealMethod = dealMethod;
        this.modifyTime = modifyTime;
        this.description = description;
        this.tableStatus = tableStatus;
        this.comment = comment;
        this.router = router;
        this.faultPicPaths = faultPicPaths;
        this.tempFaultPicPaths = tempFaultPicPaths;
        this.recallAble = recallAble;
        this.allowProxyAble = allowProxyAble;
        this.dealSet = dealSet;
        this.isLocalFinished = isLocalFinished;
        this.isFromXJ = isFromXJ;
        this.workId = workId;
        this.workItemId = workItemId;
        this.taskId = taskId;
        this.staffId = staffId;
        this.repairStaffId = repairStaffId;
        this.repairStaff = repairStaff;
        this.host = host;
    }

    @Generated(hash = 1523836540)
    public QXGLEntity() {
    }

    public long getLocalId() {
        return this.localId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTableId() {
        return this.tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public long getPendingId() {
        return this.pendingId;
    }

    public void setPendingId(long pendingId) {
        this.pendingId = pendingId;
    }

    public long getFindTime() {
        return this.findTime;
    }

    public void setFindTime(long findTime) {
        this.findTime = findTime;
    }

    public String getTableNo() {
        return this.tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public long getEamId() {
        return this.eamId;
    }

    public void setEamId(long eamId) {
        this.eamId = eamId;
    }

    public String getEamCode() {
        return this.eamCode;
    }

    public void setEamCode(String eamCode) {
        this.eamCode = eamCode;
    }

    public String getEamName() {
        return this.eamName;
    }

    public void setEamName(String eamName) {
        this.eamName = eamName;
    }

    public String getEamModel() {
        return this.eamModel;
    }

    public void setEamModel(String eamModel) {
        this.eamModel = eamModel;
    }

    public String getEamType() {
        return this.eamType;
    }

    public void setEamType(String eamType) {
        this.eamType = eamType;
    }

    public long getFindStaffId() {
        return this.findStaffId;
    }

    public void setFindStaffId(long findStaffId) {
        this.findStaffId = findStaffId;
    }

    public String getFindStaff() {
        return this.findStaff;
    }

    public void setFindStaff(String findStaff) {
        this.findStaff = findStaff;
    }

    public long getPlanDealTime() {
        return this.planDealTime;
    }

    public void setPlanDealTime(long planDealTime) {
        this.planDealTime = planDealTime;
    }

    public Double getStopTime() {
        return this.stopTime;
    }

    public void setStopTime(Double stopTime) {
        this.stopTime = stopTime;
    }

    public Double getStopLoss() {
        return this.stopLoss;
    }

    public void setStopLoss(Double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFaultName() {
        return this.faultName;
    }

    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }

    public String getFaultTypeId() {
        return this.faultTypeId;
    }

    public void setFaultTypeId(String faultTypeId) {
        this.faultTypeId = faultTypeId;
    }

    public String getFaultType() {
        return this.faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getDealTypeId() {
        return this.dealTypeId;
    }

    public void setDealTypeId(String dealTypeId) {
        this.dealTypeId = dealTypeId;
    }

    public String getDealTypeName() {
        return this.dealTypeName;
    }

    public void setDealTypeName(String dealTypeName) {
        this.dealTypeName = dealTypeName;
    }

    public String getDealMethod() {
        return this.dealMethod;
    }

    public void setDealMethod(String dealMethod) {
        this.dealMethod = dealMethod;
    }

    public long getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTableStatus() {
        return this.tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRouter() {
        return this.router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getFaultPicPaths() {
        return this.faultPicPaths;
    }

    public void setFaultPicPaths(String faultPicPaths) {
        this.faultPicPaths = faultPicPaths;
    }

    public String getTempFaultPicPaths() {
        return this.tempFaultPicPaths;
    }

    public void setTempFaultPicPaths(String tempFaultPicPaths) {
        this.tempFaultPicPaths = tempFaultPicPaths;
    }

    public Boolean getRecallAble() {
        return this.recallAble;
    }

    public void setRecallAble(Boolean recallAble) {
        this.recallAble = recallAble;
    }

    public Boolean getAllowProxyAble() {
        return this.allowProxyAble;
    }

    public void setAllowProxyAble(Boolean allowProxyAble) {
        this.allowProxyAble = allowProxyAble;
    }

    public int getDealSet() {
        return this.dealSet;
    }

    public void setDealSet(int dealSet) {
        this.dealSet = dealSet;
    }

    public boolean getIsLocalFinished() {
        return this.isLocalFinished;
    }

    public void setIsLocalFinished(boolean isLocalFinished) {
        this.isLocalFinished = isLocalFinished;
    }

    public boolean getIsFromXJ() {
        return this.isFromXJ;
    }

    public void setIsFromXJ(boolean isFromXJ) {
        this.isFromXJ = isFromXJ;
    }

    public long getWorkId() {
        return this.workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }

    public long getWorkItemId() {
        return this.workItemId;
    }

    public void setWorkItemId(long workItemId) {
        this.workItemId = workItemId;
    }

    public long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getStaffId() {
        return this.staffId;
    }

    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }

    public long getRepairStaffId() {
        return this.repairStaffId;
    }

    public void setRepairStaffId(long repairStaffId) {
        this.repairStaffId = repairStaffId;
    }

    public String getRepairStaff() {
        return this.repairStaff;
    }

    public void setRepairStaff(String repairStaff) {
        this.repairStaff = repairStaff;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
