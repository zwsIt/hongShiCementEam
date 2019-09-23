package com.supcon.mes.module_warn.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskListEntity;
import com.supcon.mes.module_warn.model.bean.LubricationWarnListEntity;

import java.util.List;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 润滑维保
 */
@ContractFactory(entites = {DailyLubricateTaskListEntity.class})
public interface DailyLubricationWarnAPI {

    void getLubrications(Map<String, Object> params,Map<String, Object> pageQueryParams);
}
