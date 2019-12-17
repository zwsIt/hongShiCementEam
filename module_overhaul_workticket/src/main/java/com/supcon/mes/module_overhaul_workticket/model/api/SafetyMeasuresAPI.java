package com.supcon.mes.module_overhaul_workticket.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresList;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc 安全措施list获取
 */
@ContractFactory(entites = {SafetyMeasuresList.class})
public interface SafetyMeasuresAPI {
    void listSafetyMeasures(Long workTicketId);
}
