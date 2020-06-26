package com.supcon.mes.module_score.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;

/**
 * 个人评分：设备责任到人
 */
public class ScoreDutyEamEntity extends BaseEntity {
    public float avgScore; // 设备平均分
    public float professInspScore; // 专业评分
    public float score;
    public String name;
    public String code;
    public String result;
    public String errMsg;
    public boolean success;

    @Expose
    public int viewType = 0;
}
