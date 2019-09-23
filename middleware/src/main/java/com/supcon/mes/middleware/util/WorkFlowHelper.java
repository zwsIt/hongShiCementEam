package com.supcon.mes.middleware.util;

import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;

import java.util.ArrayList;

/**
 * Created by wangshizhan on 2018/7/19
 * Email:wangshizhan@supcom.com
 */
public class WorkFlowHelper {

    public static WorkFlowVar generateWorkFlow(LinkEntity linkEntity, String comment){

        WorkFlowVar workFlowVar = new WorkFlowVar();
        workFlowVar.comment = comment;
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        workFlowEntity.type = "normal";
        workFlowEntity.outcome = linkEntity.name;
        workFlowEntity.dec = linkEntity.description;
        workFlowVar.outcomeMapJson = new ArrayList<>();
        workFlowVar.outcomeMapJson.add(workFlowEntity);

        return workFlowVar;
    }

}
