package com.supcon.mes.module_overhaul_workticket.constant;

/**
 * WorkTicketConstant
 * created by zhangwenshuai1 2019/10/30
 */
public interface WorkTicketConstant {

    /**
     * 获取表头数据url
     */
    interface HeaderData{
        String HEADER_DATA_INCLUDES = "id,createStaffId,createPositionId,createTime,version,deploymentId,tableInfoId,tableNo,remark,chargeStaff.id,chargeStaff.code,chargeStaff.name," +
                "chargeStaff.mainPosition.name,chargeStaff.mainPosition.department.id,chargeStaff.mainPosition.department.name," +
                "workShop.id,workShop.name,workShop.code,workList.id,workList.tableNo,workList.content,workList.eamID.name,workList.eamID.code,eamId.id,eamId.code,eamId.name,eamId.eamAssetcode," +
                "hazardsourContrpoint,riskAssessment.id,riskAssessment.value,offApplyId,offApplyTableinfoid,offApplyTableno,flowStatus.id,flowStatus.value," +
                "centContRoom.id,centContRoom.code,centContRoom.name,securityStaff.id,securityStaff.code,securityStaff.name,securityChiefStaff.id,securityChiefStaff.code,securityChiefStaff.name," +
                "contrDirectorStaff.id,contrDirectorStaff.code,contrDirectorStaff.name,content";
    }

    interface URL{
        String PRE_URL = "/WorkTicket/workTicket/ohworkticket/";
    }

}
