package com.supcon.mes.module_score.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ScoreEamPerformanceEntity extends BaseEntity {

    public Long id;

    public String isItemValue;//是
    public String noItemValue;//否
    public String item;
    public String itemDetail;//评分详情
    public boolean result;//选中结果
    public float score;//分数
    public String scoreStandard;//标题
    public String scoreItem;//选项内容

    public Float accidentStopTime;//停机时长
    public Float totalRunTime;//累计运行时长
    public float resultValue;//结果

    @Expose
    public int viewType = 0;
    @Expose
    public int Index = 0;
    @Expose
    public Map<String, Float> marks = new LinkedHashMap<>();//多选项
    @Expose
    public Map<String, Boolean> marksState = new LinkedHashMap<>();//多选项状态
    @Expose
    public Map<String, ScoreEamPerformanceEntity> scorePerformanceEntityMap = new LinkedHashMap<>();//重复的项

    @Expose
    public ScoreEamPerformanceEntity scoreEamPerformanceEntity;
    @Expose
    public Float totalScore;//单项总分数

    public float defaultTotalScore;//默认总分数

    @Expose
    private Float totalHightScore;//单项最高总分数

     @Expose
    public Float scoreNum;//总分


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
}
