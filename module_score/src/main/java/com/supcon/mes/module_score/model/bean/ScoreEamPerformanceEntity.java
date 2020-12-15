package com.supcon.mes.module_score.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 设备评分绩效:评分项目
 */
public class ScoreEamPerformanceEntity extends BaseEntity {

    public Long id;

    public String isItemValue;//是
    public String noItemValue;//否
    public String item;
    public String itemDetail;//评分详情
    public boolean result;//选中结果
    public float score;//单个项目分数
    @Deprecated
    public String scoreStandard;//标题---------弃用
    public String scoreItem;//选项内容

    public Float accidentStopTime;//停机时长
    public Float totalRunTime;//累计运行时长
    public float resultValue;//结果

    public SystemCodeEntity defaultValueType;//值类型
    public SystemCodeEntity scoreType; // 评分类型:BEAM_077/02:手动评分;  BEAM_077/01:自动评分
    public String category;//标题
    public float defaultTotalScore;// 默认总分数
    public float categoryScore; // 单项总分数
    public Long scoringId; // 评分模板id(单据新增使用)
    public int defaultNumVal;//默认数量
    public String subScore; // 文本扣分数

    @Expose
    public int viewType = 0;
    @Expose
    public int index = 0;
    @Expose
    public Map<String, Float> marks = new LinkedHashMap<>();//多选项
    @Expose
    public Map<String, Boolean> marksState = new LinkedHashMap<>();//多选项状态
    @Expose
    public Map<String, ScoreEamPerformanceEntity> scorePerformanceEntityMap = new LinkedHashMap<>();//重复的项

    @Expose
    public ScoreEamPerformanceEntity scoreEamPerformanceEntity;
    @Expose
    @Deprecated
    public Float totalScore;//单项总分数

    @Expose
    private Float totalHightScore;//单项最高总分数

     @Expose
    public Float scoreNum;//总分

    @Expose
    public float theoreticalScore;//理论扣分数
    @Expose
    public float lastSubScore; // 上次文本扣分数


    //附件
    private List<Long> attachFileMultiFileIds;
    private List<String> attachFileMultiFileNames;
    private List<String> attachFileFileAddPaths; // 存储路径
    private List<Long> attachFileFileDeleteIds; // 附件删除ids

    //照片/视频附件
    private List<AttachmentEntity> attachmentEntityList;


    //子布局
    @Expose
    public Set<ScoreEamPerformanceEntity> scorePerformanceEntities = new HashSet<>();

    public Float getTotalScore() {
        if (totalScore == null) {
            totalScore = defaultTotalScore;
        }
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }

    public Float getTotalHightScore() {
        if (totalHightScore == null) {
            totalHightScore = getTotalScore();
        }
        return totalHightScore;
    }

    public void setTotalHightScore(Float totalHightScore) {
        this.totalHightScore = totalHightScore;
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
        if (attachFileMultiFileNames == null){
            attachFileMultiFileNames = new ArrayList<>();
        }
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
    //    public List<String> getAttachFileFileAddLocalPaths() {
//        return attachFileFileAddLocalPaths;
//    }
//
//    public void setAttachFileFileAddLocalPaths(List<String> attachFileFileAddLocalPaths) {
//        this.attachFileFileAddLocalPaths = attachFileFileAddLocalPaths;
//    }
    public List<Long> getAttachFileFileDeleteIds() {
        return attachFileFileDeleteIds;
    }

    public void setAttachFileFileDeleteIds(List<Long> attachFileFileDeleteIds) {
        this.attachFileFileDeleteIds = attachFileFileDeleteIds;
    }

    public List<AttachmentEntity> getAttachmentEntityList() {
        return attachmentEntityList;
    }

    public void setAttachmentEntityList(List<AttachmentEntity> attachmentEntityList) {
        this.attachmentEntityList = attachmentEntityList;
    }


}
