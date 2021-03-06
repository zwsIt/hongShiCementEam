package com.supcon.mes.module_score.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;
import com.supcon.mes.module_score.constant.ScoreConstant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 个人绩效:评分项目
 */
public class ScoreStaffPerformanceEntity extends BaseEntity {

    public Long id;

    public String category;//标题
    public String project;//大标题
    public String grade;//评分标准
    public String item;//事项

    public String isItemValue;
    public String noItemValue;
    public float itemScore;//单项分
    public boolean result;//选中结果
    public String resultValue; // 扣分值存储

    public float fraction;//单项总分数
    public float score;//分数

    public float defaultTotalScore; // 单类总分
    public SystemCodeEntity defaultValueType;//值类型
    public int defaultNumVal;//默认数量
    public String subScore; // 文本扣分数
    public SystemCodeEntity scoreType; // 评分类型:BEAM_077/02:手动评分;  BEAM_077/01:自动评分

    public Long scoringId; // 评分模板id(单据新增使用)

    @Expose
    public float lastSubScore; // 上次文本扣分数
    public String itemDetail;

    //附件
    private List<Long> attachFileMultiFileIds;
    private List<String> attachFileMultiFileNames;
    private List<String> attachFileFileAddPaths; // 存储路径
    private List<Long> attachFileFileDeleteIds; // 附件删除ids

//    private List<String> attachFileFileAddLocalPaths = new ArrayList<>(); // 本地存储路径
    //照片/视频附件
    private List<AttachmentEntity> attachmentEntityList;

    @Expose
    public int viewType = 0;
    @Expose
    public int index = 0;

    @Expose
    public ScoreStaffPerformanceEntity scoreEamPerformanceEntity;//标题
    @Expose
    public float theoreticalScore;//理论扣分数

    @Expose
    private Float totalHightScore;//单项最高总分数
    @Expose
    public Float scoreNum;//总分

    //子布局
    @Expose
    public Set<ScoreStaffPerformanceEntity> scorePerformanceEntities = new HashSet<>();

    public Float getTotalHightScore() {
        if (totalHightScore == null) {
            totalHightScore = fraction;
        }
        return totalHightScore;
    }

    public void setTotalHightScore(Float totalHightScore) {
        this.totalHightScore = totalHightScore;
    }

//    public boolean isEdit() {
//        if (defaultValueType != null && ScoreConstant.ValueType.T1.equals(defaultValueType.id)) {
//            return true;
//        }
//        return false;
//    }

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
