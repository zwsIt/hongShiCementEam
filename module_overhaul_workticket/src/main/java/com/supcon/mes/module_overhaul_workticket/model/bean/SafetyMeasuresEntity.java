package com.supcon.mes.module_overhaul_workticket.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/10
 * Email zhangwenshuai1@supcon.com
 * Desc 安全措施(检修工作票PT)
 */
public class SafetyMeasuresEntity extends BaseEntity {

    /**
     * attachFileMultiFileIds: "1685"
     * attachFileMultiFileNames: "检修作业票.jpg"
     * headId : {"id":1000}
     * id : 1000
     * isExecuted : false
     * safetyMeasure : 已按照规定召开检修例会
     * sort : 0
     * version : 0
     */
    private WorkTicketEntity headId; // 表头检修作业票
    private Long id;
    private boolean isExecuted; // 是否执行
    private String safetyMeasure; // 安全措施
    private int sort;
    private int version;

    //附件
    private List<Long> attachFileMultiFileIds;
    private List<String> attachFileMultiFileNames;
    private List<String> attachFileFileAddPaths; // 存储路径
    private List<Long> attachFileFileDeleteIds; // 附件删除ids

    private int operateType/* = OperateType.CONFIRM.getType()*/; // 操作类型：视频、拍照、nfc、确认; 默认确认

    //停电照片附件
    private List<AttachmentEntity> eleOffAttachmentEntityList;

    //照片/视频附件
    private List<AttachmentEntity> attachmentEntityList;

    public WorkTicketEntity getHeadId() {
        return headId;
    }

    public void setHeadId(WorkTicketEntity headId) {
        this.headId = headId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isIsExecuted() {
        return isExecuted;
    }

    public void setIsExecuted(boolean isExecuted) {
        this.isExecuted = isExecuted;
    }

    public String getSafetyMeasure() {
        return safetyMeasure;
    }

    public void setSafetyMeasure(String safetyMeasure) {
        this.safetyMeasure = safetyMeasure;
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

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public List<Long> getAttachFileMultiFileIds() {
        if (attachFileMultiFileIds == null){
            attachFileMultiFileIds = new ArrayList<>();
        }
        return attachFileMultiFileIds;
    }

    public void setAttachFileMultiFileIds(List<Long> attachFileMultiFileIds) {
        this.attachFileMultiFileIds = attachFileMultiFileIds;
    }

    public List<String> getAttachFileMultiFileNames() {
        return attachFileMultiFileNames;
    }

    public void setAttachFileMultiFileNames(List<String> attachFileMultiFileNames) {
        this.attachFileMultiFileNames = attachFileMultiFileNames;
    }

    public List<String> getAttachFileFileAddPaths() {
        if (attachFileFileAddPaths == null){
            attachFileFileAddPaths = new ArrayList<>();
        }
        return attachFileFileAddPaths;
    }

    public void setAttachFileFileAddPaths(List<String> attachFileFileAddPaths) {
        this.attachFileFileAddPaths = attachFileFileAddPaths;
    }

    public List<Long> getAttachFileFileDeleteIds() {
        if (attachFileFileDeleteIds == null){
            attachFileFileDeleteIds = new ArrayList<>();
        }
        return attachFileFileDeleteIds;
    }

    public void setAttachFileFileDeleteIds(List<Long> attachFileFileDeleteIds) {
        this.attachFileFileDeleteIds = attachFileFileDeleteIds;
    }

    public List<AttachmentEntity> getEleOffAttachmentEntityList() {
        return eleOffAttachmentEntityList;
    }

    public void setEleOffAttachmentEntityList(List<AttachmentEntity> eleOffAttachmentEntityList) {
        this.eleOffAttachmentEntityList = eleOffAttachmentEntityList;
    }

    public List<AttachmentEntity> getAttachmentEntityList() {
        return attachmentEntityList;
    }

    public void setAttachmentEntityList(List<AttachmentEntity> attachmentEntityList) {
        this.attachmentEntityList = attachmentEntityList;
    }
}
