package com.supcon.mes.module_score.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.Staff;

public class ScoreStaffEntity extends BaseEntity {


    public Long cid;
    public Long createTime;//创建时间
    public int id = -1;
    public Staff patrolWorker;//巡检工
    public float score = 100;//分数
    public Long scoreData;//评分时间

    public int rank; // 排名


    public Staff getPatrolWorker() {
        if (patrolWorker == null) {
            patrolWorker = new Staff();
        }
        return patrolWorker;
    }
}
