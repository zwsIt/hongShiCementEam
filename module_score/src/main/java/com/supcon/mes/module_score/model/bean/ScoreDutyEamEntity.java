package com.supcon.mes.module_score.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;

public class ScoreDutyEamEntity extends BaseEntity {
    public float avgScore;
    public float score;
    public String name;
    public String code;

    @Expose
    public int viewType = 0;
}
