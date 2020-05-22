package com.supcon.mes.module_warn.constant;

public interface WarnConstant {
    /**
     * 操作编码
     */
     interface OperateCode{
         // 润滑预警：时间频率
         String T_DO_WORK = "lubricateWarnList_generateWork_add_BEAM_1.0.0_baseInfo_lubricateWarnList"; // 生成工单
         String T_DELAY_SET = "lubricateWarnList_delaySetting_add_BEAM_1.0.0_baseInfo_lubricateWarnList"; // 延期设置
         String T_DELAY_RECORDS = "lubricateWarnList_delayRecordsCheck_add_BEAM_1.0.0_baseInfo_lubricateWarnList"; // 延期记录
         // 润滑预警：运行时长
        String R_DO_WORK = "lubricateWarnList_generateWorkRun_add_BEAM_1.0.0_baseInfo_lubricateWarnList"; // 生成工单
        String R_DELAY_SET = "lubricateWarnList_delaySettingDuration_add_BEAM_1.0.0_baseInfo_lubricateWarnList"; // 延时设置
        String R_DELAY_RECORDS = "lubricateWarnList_delayRecordsDuration_add_BEAM_1.0.0_baseInfo_lubricateWarnList"; // 延时记录

        // 计划润滑(日常润滑预警)
        String FINISH = "dailyLubricateWarn_generateWork_add_BEAM_1.0.0_baseInfo_dailyLubricateWarn"; // 完成
        String PLAN_DELAY_SET = "dailyLubricateWarn_delaySetting_add_BEAM_1.0.0_baseInfo_dailyLubricateWarn"; // 延期设置
    }
}
