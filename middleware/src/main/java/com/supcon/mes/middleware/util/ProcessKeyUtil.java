package com.supcon.mes.middleware.util;

import com.supcon.mes.middleware.EamApplication;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/7/3
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ProcessKeyUtil {

    public static String WORK = EamApplication.getCid() == 1002 ? "work" : EamApplication.getCompanyCode() + "work"; // 工单
    public static String FAULT_INFO = EamApplication.getCid() == 1002 ? "faultInfoFW" : EamApplication.getCompanyCode() + "faultInfoFW"; // 隐患登记
    public static String EAM_INFO_EDIT = EamApplication.getCid() == 1002 ? "eaminfoEdit" : EamApplication.getCompanyCode() + "eaminfoEdit"; // 设备档案新增申请
    public static String EAM_INFO = EamApplication.getCid() == 1002 ? "eaminfo" : EamApplication.getCompanyCode() + "eaminfo"; // 设备档案申请修改
    public static String CHANGE_WF = EamApplication.getCid() == 1002 ? "changeWF" : EamApplication.getCompanyCode() + "changeWF"; // 设备状态变更
    public static String CHECK_APPLY_FW = EamApplication.getCid() == 1002 ? "checkApplyFW" : EamApplication.getCompanyCode() + "checkApplyFW"; // 验收申请
    public static String ENTRUST_REPAIR = EamApplication.getCid() == 1002 ? "entrustRepair" : EamApplication.getCompanyCode() + "entrustRepair"; // 委外维修单
    public static String INSTALL_NEW_WF = EamApplication.getCid() == 1002 ? "installNewWF" : EamApplication.getCompanyCode() + "installNewWF"; // 安装验收移交
    public static String RUN_STATE_WF = EamApplication.getCid() == 1002 ? "RunningStateWF" : EamApplication.getCompanyCode() + "RunningStateWF"; // 运行记录处理
    public static String SPARE_PART_APPLY = EamApplication.getCid() == 1002 ? "sparePartApply" : EamApplication.getCompanyCode() + "sparePartApply"; // 备件领用申请
    public static String WORK_ALLOT_NEW_WF = EamApplication.getCid() == 1002 ? "workAllotNewWF" : EamApplication.getCompanyCode() + "workAllotNewWF"; // 设备调拨
    public static String TEMP_WF = EamApplication.getCid() == 1002 ? "tempWF" : EamApplication.getCompanyCode() + "tempWF"; // 临时任务
    public static String POTROL_TASK_WF = EamApplication.getCid() == 1002 ? "potrolTaskWF" : EamApplication.getCompanyCode() + "potrolTaskWF"; // 点巡检任务
    public static String ELE_OFF = EamApplication.getCid() == 1002 ? "EleOnWorkFlow" : EamApplication.getCompanyCode() + "EleOnWorkFlow"; // 停电
    public static String ELE_ON = EamApplication.getCid() == 1002 ? "EleOn" : EamApplication.getCompanyCode() + "EleOn"; // 送电
    public static String WORK_TICKET = /*EamApplication.getCid() == 1002 ? "workTicketFW" : EamApplication.getCompanyCode() + */"workTicketFW"; // 检修作业票

    public static void updateProcessKey(){
        WORK = EamApplication.getCid() == 1002 ? "work" : EamApplication.getCompanyCode() + "work"; // 工单
        FAULT_INFO = EamApplication.getCid() == 1002 ? "faultInfoFW" : EamApplication.getCompanyCode() + "faultInfoFW"; // 隐患登记
        EAM_INFO_EDIT = EamApplication.getCid() == 1002 ? "eaminfoEdit" : EamApplication.getCompanyCode() + "eaminfoEdit"; // 设备档案新增申请
        EAM_INFO = EamApplication.getCid() == 1002 ? "eaminfo" : EamApplication.getCompanyCode() + "eaminfo"; // 设备档案申请修改
        CHANGE_WF = EamApplication.getCid() == 1002 ? "changeWF" : EamApplication.getCompanyCode() + "changeWF"; // 设备状态变更
        CHECK_APPLY_FW = EamApplication.getCid() == 1002 ? "checkApplyFW" : EamApplication.getCompanyCode() + "checkApplyFW"; // 验收申请
        ENTRUST_REPAIR = EamApplication.getCid() == 1002 ? "entrustRepair" : EamApplication.getCompanyCode() + "entrustRepair"; // 委外维修单
        INSTALL_NEW_WF = EamApplication.getCid() == 1002 ? "installNewWF" : EamApplication.getCompanyCode() + "installNewWF"; // 安装验收移交
        RUN_STATE_WF = EamApplication.getCid() == 1002 ? "RunningStateWF" : EamApplication.getCompanyCode() + "RunningStateWF"; // 运行记录处理
        SPARE_PART_APPLY = EamApplication.getCid() == 1002 ? "sparePartApply" : EamApplication.getCompanyCode() + "sparePartApply"; // 备件领用申请
        WORK_ALLOT_NEW_WF = EamApplication.getCid() == 1002 ? "workAllotNewWF" : EamApplication.getCompanyCode() + "workAllotNewWF"; // 设备调拨
        TEMP_WF = EamApplication.getCid() == 1002 ? "tempWF" : EamApplication.getCompanyCode() + "tempWF"; // 临时任务
        POTROL_TASK_WF = EamApplication.getCid() == 1002 ? "potrolTaskWF" : EamApplication.getCompanyCode() + "potrolTaskWF"; // 点巡检任务
        ELE_OFF = EamApplication.getCid() == 1002 ? "EleOnWorkFlow" : EamApplication.getCompanyCode() + "EleOnWorkFlow"; // 停电
        ELE_ON = EamApplication.getCid() == 1002 ? "EleOn" : EamApplication.getCompanyCode() + "EleOn"; // 送电
        WORK_TICKET = /*EamApplication.getCid() == 1002 ? "workTicketFW" : EamApplication.getCompanyCode() + */"workTicketFW"; // 检修作业票
    }
}
