package com.supcon.mes.module_score.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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

    public float fraction;//单项总分数
    public float score;//默认总分数

    public ValueEntity defaultValueType;//类型
    public int defaultNumVal;//默认数量

    @Expose
    public int viewType = 0;
    @Expose
    public int Index = 0;

    @Expose
    public ScoreStaffPerformanceEntity scoreEamPerformanceEntity;//标题

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

    public boolean isEdit() {
        if (defaultValueType != null && defaultValueType.id.equals("BEAM_066/01")) {
            return true;
        }
        return false;
    }
}
