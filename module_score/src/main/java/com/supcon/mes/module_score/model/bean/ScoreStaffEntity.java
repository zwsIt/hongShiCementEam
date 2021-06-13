package com.supcon.mes.module_score.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Department;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.ValueEntity;

public class ScoreStaffEntity extends BaseEntity {

    public Long cid;
    public Long createTime;//创建时间
    public Long id = -1L;
    public Staff patrolWorker;// 巡检工
    public float score = 100;// 分数
    public float beforeScore;// 历史得分
    public Long scoreData;//评分时间

    public int rank; // 排名

    public Department departmentId;
    public IdEntity soringId; // 评分模板

    public Staff scoreStaff;//评分人

    public Staff getPatrolWorker() {
        if (patrolWorker == null) {
            patrolWorker = new Staff();
        }
        return patrolWorker;
    }

    public static class IdEntity extends BaseEntity {
        public Long id;
    }
}
