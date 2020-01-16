package com.supcon.mes.module_hs_tsd.constant;

/**
 * ElectricityConstant
 * created by zhangwenshuai1 2019/10/30
 */
public interface ElectricityConstant {

    /**
     * 获取表头数据url
     */
    interface HeaderData {
        String HEADER_DATA_INCLUDES = "id,createStaffId,createTime,version,deploymentId,tableInfoId,tableNo,remark,applyStaff.id,applyStaff.code,applyStaff.name," +
                "applyStaff.mainPosition.name,applyStaff.mainPosition.department.id,applyStaff.mainPosition.department.name," +
                "applyCurrentDept.id,applyCurrentDept.name,applyCurrentDept.code,eamID.id,eamID.code,eamID.name,workRecordId,workRecordTableno,applyDate,operateDate," +
                "operateStaff.id,operateStaff.name,operateStaff.code,workTask,eleTemplateId.id,eleTemplateId.code,applyType.id,applyType.value";
    }

    interface URL {
        String PRE_URL = "/BEAMEle/onOrOff/onoroff/";
    }

}
