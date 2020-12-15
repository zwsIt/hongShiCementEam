package com.supcon.mes.module_score.model.dto;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;

import java.util.List;

public class ScoreEamDto extends BaseEntity {

    public String id;
    public String item;
    public String result;
    public String score;
    public String itemDetail;
    public String isItemValue;
    public String noItemValue;
    public String scoreItem;
    public String scoreStandard;
    public String defaultTotalScore;

    public String resultValue;
    public String accidentStopTime;
    public String totalRunTime;
    public String category;
    public String categoryScore;
    public String defaultNumVal;
    public ValueEntity defaultValueType;
    public ValueEntity scoreType;
    public String subScore;

    //附件
    public List<Long> attachFileMultiFileIds;
    public List<String> attachFileMultiFileNames;
    public List<String> attachFileFileAddPaths; // 存储路径
    public List<Long> attachFileFileDeleteIds; // 附件删除ids
}
