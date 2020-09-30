package com.supcon.mes.module_score.model.dto;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;

import java.util.List;

public class ScoreStaffDto extends BaseEntity {

    public String id;
    public String category;
    public String project;
    public String score;
    public String grade;
    public String item;
    public String itemScore;
    public String result;
    public String resultValue; // 扣分值存储
    public String fraction;
    public String isItemValue;
    public String noItemValue;

    public String defaultNumVal;
    public ValueEntity defaultValueType;
    public ValueEntity scoreType;

    //附件
    public List<Long> attachFileMultiFileIds;
    public List<String> attachFileMultiFileNames;
    public List<String> attachFileFileAddPaths; // 存储路径
    public List<Long> attachFileFileDeleteIds; // 附件删除ids

    public String subScore; // 扣分

}
