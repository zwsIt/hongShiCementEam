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
    public String resultValue; // 扣分值存储
    public String fraction;
    public String isItemValue;
    public String noItemValue;

    public String defaultNumVal;
    public ValueEntity defaultValueType;

    //附件
    public String attachFileMultiFileIds;
    public String attachFileMultiFileNames;
    public String attachFileFileAddPaths; // 存储路径
    public String attachFileFileDeleteIds; // 附件删除ids

    public String subScore; // 扣分

}
