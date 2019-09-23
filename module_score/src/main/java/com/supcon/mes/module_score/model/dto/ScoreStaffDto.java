package com.supcon.mes.module_score.model.dto;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;

public class ScoreStaffDto extends BaseEntity {

    public String id;
    public String category;
    public String project;
    public String score;
    public String grade;
    public String item;
    public String itemScore;
    public String result;
    public String fraction;
    public String isItemValue;
    public String noItemValue;

    public String defaultNumVal;
    public ValueEntity defaultValueType;
}
