package com.supcon.mes.module_score.constant;

/**
 * @Description:
 * @Author: zhangwenshuai
 * @CreateDate: 2020/6/18 11:39
 */
public interface ScoreConstant {
    /**
     * 评分类别
     */
    interface ScoreType {
        String INSPECTION_STAFF = "BEAM_065/02"; // 巡检个人评分
        String EAM_MACHINE_STAFF = "BEAM_065/03"; // 设备科个人评

    }

    /**
     * 值类别
     */
    interface ValueType {
        String T1 = "BEAM_066/01"; // 数值
        String T2 = "BEAM_066/02"; // 布尔
        String T3 = "BEAM_066/03"; // 文本
    }

    /**
     * 绩效项目评分类型
     */
    interface ScoreItemType {
        String T1 = "BEAM_077/01"; // 自动
        String T2 = "BEAM_077/02"; // 手动
    }

    interface Permission {
        String add_inspection_staff = "patrolScore_add_add_BEAM_1.0.0_patrolWorkerScore_patrolScore"; // 巡检工评分新增权限
        String add_machine_staff = "repairerScoreList_add_add_BEAM_1.0.0_patrolWorkerScore_repairerScoreList"; // 设备科评分新增权限
    }

}
