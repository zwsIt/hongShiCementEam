package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;
import java.util.Map;

/**
 * Created by xushiyun on 2018/8/14
 * Email:ciruy.victory@gmail.com
 *
 * @author xushiyun
 */
@Entity
public class YHEntityVo extends BaseEntity {

    /**
     * 单据ID,按照当前的系统时间对其进行赋值 itemTableNoTv
     */
    @Id
    private Long tableId = System.currentTimeMillis();
    /**
     * 隐患编号
     */
    private String faultInfoNo;
    /**
     * 区域位置ID
     */
    private Long areaInstallId;
    private String areaInstallName;
    /**
     * 设备ID itemYHDeviceName itemYHDeviceCode
     */
    private Long eamId;
    private String eamName;
    private String eamModel;
    /**
     * 发现人 itemYHPersonTv
     */
    private Long findStaffId;
    public static YHEntityVo createEntity() {
        YHEntityVo yhEntityVo = new YHEntityVo();
        yhEntityVo.setFindStaffId(EamApplication.getAccountInfo().staffId);
        return yhEntityVo;
    }
    private String findStaffName;
    /**
     * 隐患类型 itemYHType
     */
    private String faultType;
    /**
     * 优先级 itemTablePriority
     */
    private String priority;
    /**
     * 发现时间 itemYHDateTv
     */
    private String findDate;
    /**
     * 要求完成时间
     */
    private String endTime;
    private String ip = EamApplication.getIp();
    /**
     * 维修组ID
     */
    private Long repiarGroupId;
    private String repairGroupName;
    /**
     * 维修类型 itemWXType
     */
    private String repairType;
    /**
     * 隐患现象 itemYHDescription
     */
    private String description;
    /**
     * 备注
     */
    private String remark;
    /**
     * 隐患来源
     */
    private String sourceType;
    /**
     * 上游单据Id (巡检任务ID)
     */
    private Long srcID;
    /**
     * 上游单据类型
     */
    private String srcType;
    /**
     * 任务明细ID (巡检项ID)
     */
    private Long taskId;
    /**
     * 需要上传的文件的路径
     */
    private String faultPicPaths;
    /**
     * 需要删除文件的路径
     */
    private String deletePicPaths;
    /**
     * 临时图片文件
     */
    private String tempFaultPicPaths;
    /**
     * 本地图片路径
     */
    private String localPicPaths;
    private String localImgNames;
    /**
     * 以下为工作流相关
     */
    private String outCome;
    private String outComeType;
    private String pendingId;
    private String transitionDesc;

    /**
     * itemTableStatus
     */
    private Boolean status;

    public void setSourceTypeManual() {
        this.setSourceType("BEAM2006/02");
    }

    public void setSourceTypeFunc() {
        this.setSourceType("BEAM2006/01");
    }

    @Generated(hash = 2109304081)
    public YHEntityVo(Long tableId, String faultInfoNo, Long areaInstallId, String areaInstallName, Long eamId, String eamName, String eamModel,
            Long findStaffId, String findStaffName, String faultType, String priority, String findDate, String endTime, String ip, Long repiarGroupId,
            String repairGroupName, String repairType, String description, String remark, String sourceType, Long srcID, String srcType, Long taskId,
            String faultPicPaths, String deletePicPaths, String tempFaultPicPaths, String localPicPaths, String localImgNames, String outCome,
            String outComeType, String pendingId, String transitionDesc, Boolean status) {
        this.tableId = tableId;
        this.faultInfoNo = faultInfoNo;
        this.areaInstallId = areaInstallId;
        this.areaInstallName = areaInstallName;
        this.eamId = eamId;
        this.eamName = eamName;
        this.eamModel = eamModel;
        this.findStaffId = findStaffId;
        this.findStaffName = findStaffName;
        this.faultType = faultType;
        this.priority = priority;
        this.findDate = findDate;
        this.endTime = endTime;
        this.ip = ip;
        this.repiarGroupId = repiarGroupId;
        this.repairGroupName = repairGroupName;
        this.repairType = repairType;
        this.description = description;
        this.remark = remark;
        this.sourceType = sourceType;
        this.srcID = srcID;
        this.srcType = srcType;
        this.taskId = taskId;
        this.faultPicPaths = faultPicPaths;
        this.deletePicPaths = deletePicPaths;
        this.tempFaultPicPaths = tempFaultPicPaths;
        this.localPicPaths = localPicPaths;
        this.localImgNames = localImgNames;
        this.outCome = outCome;
        this.outComeType = outComeType;
        this.pendingId = pendingId;
        this.transitionDesc = transitionDesc;
        this.status = status;
    }

    @Generated(hash = 1816123373)
    public YHEntityVo() {
    }


    public static final List<YHEntityVo> genFilterResult(int pageNum, Map<String, Object> filterParam) {
        return EamApplication.dao().getYHEntityVoDao().queryBuilder()
                .where(YHEntityVoDao.Properties.Ip.eq(EamApplication.getIp()))
                .where(filterParam.containsKey(Constant.BAPQuery.YH_DATE_START) ?
                        YHEntityVoDao.Properties.FindDate.ge(filterParam.get(Constant.BAPQuery.YH_DATE_START)) : emptyStringCondition())
                .where(filterParam.containsKey(Constant.BAPQuery.YH_DATE_END) ?
                        YHEntityVoDao.Properties.FindDate.le(filterParam.get(Constant.BAPQuery.YH_DATE_END)) : emptyStringCondition())
                .where(filterParam.containsKey(Constant.BAPQuery.REPAIR_TYPE) ?
                        YHEntityVoDao.Properties.RepairType.eq(filterParam.get(Constant.BAPQuery.REPAIR_TYPE)) : emptyStringCondition())
                .where(filterParam.containsKey(Constant.BAPQuery.PRIORITY) ?
                        YHEntityVoDao.Properties.Priority.eq(filterParam.get(Constant.BAPQuery.PRIORITY)) : emptyStringCondition())
                .where(filterParam.containsKey(Constant.BAPQuery.STATUS) ?
                        YHEntityVoDao.Properties.Status.eq(filterParam.get(Constant.BAPQuery.STATUS)) : emptyStringCondition())
                .where(filterParam.containsKey(Constant.BAPQuery.EAM_NAME) ?
                        YHEntityVoDao.Properties.EamName.like("%"+filterParam.get(Constant.BAPQuery.EAM_NAME)+"%") : emptyStringCondition())
                .offset((pageNum - 1) * 5) 
                .limit(5)
                .orderDesc(YHEntityVoDao.Properties.FindDate)
                .list();
    }

    private static final WhereCondition.StringCondition emptyStringCondition() {
        return new WhereCondition.StringCondition("1=1");
    }


    public Long getTableId() {
        return this.tableId;
    }


    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }


    public String getFaultInfoNo() {
        return this.faultInfoNo;
    }


    public void setFaultInfoNo(String faultInfoNo) {
        this.faultInfoNo = faultInfoNo;
    }


    public Long getAreaInstallId() {
        return this.areaInstallId;
    }


    public void setAreaInstallId(Long areaInstallId) {
        this.areaInstallId = areaInstallId;
    }


    public Long getEamId() {
        return this.eamId;
    }


    public void setEamId(Long eamId) {
        this.eamId = eamId;
    }


    public Long getFindStaffId() {
        return this.findStaffId;
    }


    public void setFindStaffId(Long findStaffId) {
        this.findStaffId = findStaffId;
    }


    public String getFaultType() {
        return this.faultType;
    }


    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }


    public String getPriority() {
        return this.priority;
    }


    public void setPriority(String priority) {
        this.priority = priority;
    }


    public String getFindDate() {
        return this.findDate;
    }


    public void setFindDate(String findDate) {
        this.findDate = findDate;
    }


    public String getEndTime() {
        return this.endTime;
    }


    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public Long getRepiarGroupId() {
        return this.repiarGroupId;
    }


    public void setRepiarGroupId(Long repiarGroupId) {
        this.repiarGroupId = repiarGroupId;
    }


    public String getRepairType() {
        return this.repairType;
    }


    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }


    public String getDescription() {
        return this.description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getRemark() {
        return this.remark;
    }


    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getSourceType() {
        return this.sourceType;
    }


    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }


    public Long getSrcID() {
        return this.srcID;
    }


    public void setSrcID(Long srcID) {
        this.srcID = srcID;
    }


    public String getSrcType() {
        return this.srcType;
    }


    public void setSrcType(String srcType) {
        this.srcType = srcType;
    }


    public Long getTaskId() {
        return this.taskId;
    }


    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }


    public String getFaultPicPaths() {
        return this.faultPicPaths;
    }


    public void setFaultPicPaths(String faultPicPaths) {
        this.faultPicPaths = faultPicPaths;
    }


    public String getDeletePicPaths() {
        return this.deletePicPaths;
    }


    public void setDeletePicPaths(String deletePicPaths) {
        this.deletePicPaths = deletePicPaths;
    }


    public String getTempFaultPicPaths() {
        return this.tempFaultPicPaths;
    }


    public void setTempFaultPicPaths(String tempFaultPicPaths) {
        this.tempFaultPicPaths = tempFaultPicPaths;
    }


    public String getLocalPicPaths() {
        return this.localPicPaths;
    }


    public void setLocalPicPaths(String localPicPaths) {
        this.localPicPaths = localPicPaths;
    }


    public String getLocalImgNames() {
        return this.localImgNames;
    }


    public void setLocalImgNames(String localImgNames) {
        this.localImgNames = localImgNames;
    }


    public String getOutCome() {
        return this.outCome;
    }


    public void setOutCome(String outCome) {
        this.outCome = outCome;
    }


    public String getOutComeType() {
        return this.outComeType;
    }


    public void setOutComeType(String outComeType) {
        this.outComeType = outComeType;
    }


    public String getPendingId() {
        return this.pendingId;
    }


    public void setPendingId(String pendingId) {
        this.pendingId = pendingId;
    }


    public String getTransitionDesc() {
        return this.transitionDesc;
    }


    public void setTransitionDesc(String transitionDesc) {
        this.transitionDesc = transitionDesc;
    }


    public Boolean getStatus() {
        return null == this.status ? false : this.status;
    }


    public void setStatus(Boolean status) {
        this.status = status;
    }


    public String getEamName() {
        return this.eamName;
    }


    public void setEamName(String eamName) {
        this.eamName = eamName;
    }


    public String getFindStaffName() {
        return this.findStaffName;
    }


    public void setFindStaffName(String findStaffName) {
        this.findStaffName = findStaffName;
    }


    public String getAreaInstallName() {
        return this.areaInstallName;
    }


    public void setAreaInstallName(String areaInstallName) {
        this.areaInstallName = areaInstallName;
    }


    public String getRepairGroupName() {
        return this.repairGroupName;
    }


    public void setRepairGroupName(String repairGroupName) {
        this.repairGroupName = repairGroupName;
    }


    public String getEamModel() {
        return this.eamModel;
    }


    public void setEamModel(String eamModel) {
        this.eamModel = eamModel;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


}