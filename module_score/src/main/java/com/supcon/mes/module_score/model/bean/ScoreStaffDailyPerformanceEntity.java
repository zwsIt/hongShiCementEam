package com.supcon.mes.module_score.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScoreStaffDailyPerformanceEntity extends BaseEntity {

    // "basicPerDetail": "定期检查超时次数",
//         "basicPerformance": "定期检查完成情况",
//         "checkStandard": "目标值＝100%，共5分；每误差1次，扣1分",
//         "define": "定期检查周期超过设定时间次数",
//         "dockPoints": "超时次数：2次",
//         "id": 1162,
//         "result": 3,
//         "scoreText": "20",
//         "version": 0,
//         "workScoreHead": {
//        "id": 1073
//    }

    public Long id;
    public String basicPerformance;//大标题
    public String basicPerDetail;//标题
    public String define;//定义公式
    public String dockPoints;//绩效执行情况
    public String scoreText;//分值
    public String result;//得分
    public String checkStandard;//考核标准

    @Expose
    public List<String> checkStandardList = new LinkedList<>();//考核标准
    @Expose
    public Map<String, String> dockPointsMap = new LinkedHashMap<>();//多绩效执行项
    @Expose
    public int viewType = 0;
    @Expose
    public int Index = 0;
    @Expose
    public Float scoreNum;//总分

}
