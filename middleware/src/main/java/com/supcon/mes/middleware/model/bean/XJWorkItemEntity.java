package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by wangshizhan on 2018/3/24.
 * Email:wangshizhan@supcon.com
 */
@Entity(indexes = {@Index(value = "id DESC , ip DESC",unique = true)})
public class XJWorkItemEntity extends BaseEntity {

    @Transient
    public String title;

    @Transient
    public int viewType;

    @Transient
    public String headerPicPath;


    @Id(autoincrement = true)
    public Long index;

    public Long id; //任务明细Id

    public Long itemId;      //巡检项Id

    @SerializedName("content")
    public String itemContent;

    public int itemOrder;  //巡检项排序

    public Long taskId; //任务Id

    @SerializedName("workId")
    public Long areaId;       //巡检区域Id

    public String unitName;   //单位

    @SerializedName("autoJudge")
    public boolean isAutoJudge; //自动判断

    public String normalRange;  //正常值范围

    public String decimal;  //小数位

    public boolean isThermometric;  //是否要求测温

    public String editType;  //编辑方式：录入/单选/是否/多选

    public boolean isPhone;  //是否要求拍照

    public int realIsPhone;  //实际是否拍照

    public boolean isPass;  //是否允许跳过

    public int realIsPass;  //实际是否跳过

    public boolean isSeismic; //是否要求测震

    public String equipmentId;  // 设备可能为空

    public String equipmentName;

    //public String equipmentModel; // 设备型号

    public String workItem;  //巡检项名称（公用项目）

    public String valueType;  //值类型：数字/字符

    public String candidateValue;   //候选字段

    @SerializedName("limitValue")
    public String limitValue1;

    @SerializedName("llimitValue")
    public String limitValue2;

    public String signCode; //签到编码

    public String virtualOrder; //虚拟排序

    public boolean isFinished = false;

    public String result ;   //结果
    public String conclusionID;  //结论ID
    public String conclusionName;  //结论名称

    public String realRemark;  //备注
    public String endTime;  //结束时间
    public String skipReasonID;//跳过原因ID
    public String skipReasonName; //跳过原因名称
    public String linkState = "wiLinkState/01"; //状态,默认待检

    public Long staffId;

    public boolean control;  //结论修改

    public String xjImgUrl; //图片路径，逗号相隔

    public Long tableInfoId; // 注：该字段用于PC存储图片使用

    public String defaultVal; //默认值

    public String part; //部位

    public String eamAreaId; // 设备区域ID（注：PC可能设备为空）
    public String eamAreaName; // 设备区域名称

    public String claim; // 标准（PC为要求）
    public String ip = MBapApp.getIp();




    @Generated(hash = 705454518)
    public XJWorkItemEntity(Long index, Long id, Long itemId, String itemContent,
            int itemOrder, Long taskId, Long areaId, String unitName,
            boolean isAutoJudge, String normalRange, String decimal,
            boolean isThermometric, String editType, boolean isPhone,
            int realIsPhone, boolean isPass, int realIsPass, boolean isSeismic,
            String equipmentId, String equipmentName, String workItem,
            String valueType, String candidateValue, String limitValue1,
            String limitValue2, String signCode, String virtualOrder,
            boolean isFinished, String result, String conclusionID,
            String conclusionName, String realRemark, String endTime,
            String skipReasonID, String skipReasonName, String linkState,
            Long staffId, boolean control, String xjImgUrl, Long tableInfoId,
            String defaultVal, String part, String eamAreaId, String eamAreaName,
            String claim, String ip) {
        this.index = index;
        this.id = id;
        this.itemId = itemId;
        this.itemContent = itemContent;
        this.itemOrder = itemOrder;
        this.taskId = taskId;
        this.areaId = areaId;
        this.unitName = unitName;
        this.isAutoJudge = isAutoJudge;
        this.normalRange = normalRange;
        this.decimal = decimal;
        this.isThermometric = isThermometric;
        this.editType = editType;
        this.isPhone = isPhone;
        this.realIsPhone = realIsPhone;
        this.isPass = isPass;
        this.realIsPass = realIsPass;
        this.isSeismic = isSeismic;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.workItem = workItem;
        this.valueType = valueType;
        this.candidateValue = candidateValue;
        this.limitValue1 = limitValue1;
        this.limitValue2 = limitValue2;
        this.signCode = signCode;
        this.virtualOrder = virtualOrder;
        this.isFinished = isFinished;
        this.result = result;
        this.conclusionID = conclusionID;
        this.conclusionName = conclusionName;
        this.realRemark = realRemark;
        this.endTime = endTime;
        this.skipReasonID = skipReasonID;
        this.skipReasonName = skipReasonName;
        this.linkState = linkState;
        this.staffId = staffId;
        this.control = control;
        this.xjImgUrl = xjImgUrl;
        this.tableInfoId = tableInfoId;
        this.defaultVal = defaultVal;
        this.part = part;
        this.eamAreaId = eamAreaId;
        this.eamAreaName = eamAreaName;
        this.claim = claim;
        this.ip = ip;
    }

    @Generated(hash = 371993255)
    public XJWorkItemEntity() {
    }




    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return this.itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemContent() {
        return this.itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public int getItemOrder() {
        return this.itemOrder;
    }

    public void setItemOrder(int itemOrder) {
        this.itemOrder = itemOrder;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getAreaId() {
        return this.areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public boolean getIsAutoJudge() {
        return this.isAutoJudge;
    }

    public void setIsAutoJudge(boolean isAutoJudge) {
        this.isAutoJudge = isAutoJudge;
    }

    public String getNormalRange() {
        return this.normalRange;
    }

    public void setNormalRange(String normalRange) {
        this.normalRange = normalRange;
    }

    public String getDecimal() {
        return this.decimal;
    }

    public void setDecimal(String decimal) {
        this.decimal = decimal;
    }

    public boolean getIsThermometric() {
        return this.isThermometric;
    }

    public void setIsThermometric(boolean isThermometric) {
        this.isThermometric = isThermometric;
    }

    public String getEditType() {
        return this.editType;
    }

    public void setEditType(String editType) {
        this.editType = editType;
    }

    public boolean getIsPhone() {
        return this.isPhone;
    }

    public void setIsPhone(boolean isPhone) {
        this.isPhone = isPhone;
    }

    public int getRealIsPhone() {
        return this.realIsPhone;
    }

    public void setRealIsPhone(int realIsPhone) {
        this.realIsPhone = realIsPhone;
    }

    public boolean getIsPass() {
        return this.isPass;
    }

    public void setIsPass(boolean isPass) {
        this.isPass = isPass;
    }

    public int getRealIsPass() {
        return this.realIsPass;
    }

    public void setRealIsPass(int realIsPass) {
        this.realIsPass = realIsPass;
    }

    public boolean getIsSeismic() {
        return this.isSeismic;
    }

    public void setIsSeismic(boolean isSeismic) {
        this.isSeismic = isSeismic;
    }

    public String getEquipmentId() {
        return this.equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return this.equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getWorkItem() {
        return this.workItem;
    }

    public void setWorkItem(String workItem) {
        this.workItem = workItem;
    }

    public String getValueType() {
        return this.valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getCandidateValue() {
        return this.candidateValue;
    }

    public void setCandidateValue(String candidateValue) {
        this.candidateValue = candidateValue;
    }

    public String getLimitValue1() {
        return this.limitValue1;
    }

    public void setLimitValue1(String limitValue1) {
        this.limitValue1 = limitValue1;
    }

    public String getLimitValue2() {
        return this.limitValue2;
    }

    public void setLimitValue2(String limitValue2) {
        this.limitValue2 = limitValue2;
    }

    public String getSignCode() {
        return this.signCode;
    }

    public void setSignCode(String signCode) {
        this.signCode = signCode;
    }

    public String getVirtualOrder() {
        return this.virtualOrder;
    }

    public void setVirtualOrder(String virtualOrder) {
        this.virtualOrder = virtualOrder;
    }

    public boolean getIsFinished() {
        return this.isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getConclusionID() {
        return this.conclusionID;
    }

    public void setConclusionID(String conclusionID) {
        this.conclusionID = conclusionID;
    }

    public String getConclusionName() {
        return this.conclusionName;
    }

    public void setConclusionName(String conclusionName) {
        this.conclusionName = conclusionName;
    }

    public String getRealRemark() {
        return this.realRemark;
    }

    public void setRealRemark(String realRemark) {
        this.realRemark = realRemark;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSkipReasonID() {
        return this.skipReasonID;
    }

    public void setSkipReasonID(String skipReasonID) {
        this.skipReasonID = skipReasonID;
    }

    public String getSkipReasonName() {
        return this.skipReasonName;
    }

    public void setSkipReasonName(String skipReasonName) {
        this.skipReasonName = skipReasonName;
    }

    public String getLinkState() {
        return this.linkState;
    }

    public void setLinkState(String linkState) {
        this.linkState = linkState;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public boolean getControl() {
        return this.control;
    }

    public void setControl(boolean control) {
        this.control = control;
    }

    public String getXjImgUrl() {
        return this.xjImgUrl;
    }

    public void setXjImgUrl(String xjImgUrl) {
        this.xjImgUrl = xjImgUrl;
    }

    public Long getTableInfoId() {
        return this.tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public String getDefaultVal() {
        return this.defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getPart() {
        return this.part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getEamAreaId() {
        return this.eamAreaId;
    }

    public void setEamAreaId(String eamAreaId) {
        this.eamAreaId = eamAreaId;
    }

    public String getEamAreaName() {
        return this.eamAreaName;
    }

    public void setEamAreaName(String eamAreaName) {
        this.eamAreaName = eamAreaName;
    }

    public Long getIndex() {
        return this.index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getClaim() {
        return this.claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }





}
