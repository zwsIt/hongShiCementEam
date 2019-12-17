package com.supcon.mes.module_sparepartapply_hl.constant;

/**
 * WXGDConstant
 * created by zhangwenshuai1 2019/10/30
 */
public interface SPAHLConstant {

    interface IntentKey{
        String IS_SEND_STATUS =  "IS_SEND_STATUS"; // 是否是备件领用发货环节
        String IS_WORK =  "IS_WORK"; // 是否来源工单
    }

    /**
     * 获取表头数据url
     */
    interface HeaderData{
        String SPAD_DATA_INCLUDES = "id,createStaffId,createTime,version,deploymentId,tableInfoId,tableNo,applyStaff.id,applyStaff.code,applyStaff.name," +
                "applyStaff.mainPosition.name,applyStaff.mainPosition.department,applyStaff.mainPosition.department.name,applyTime," +
                "explain,remark,repairWork.id,repairWork.tableNo,repairWork.content,repairWork.eamID.name,repairWork.eamID.code,eam.id,eam.code,eam.name,totalPrice";
    }

    interface URL{
        String PRE_URL = "/BEAM2/sparePart/apply/";
    }

}
