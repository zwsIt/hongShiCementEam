package com.supcon.mes.module_overhaul_workticket.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/24
 * Email zhangwenshuai1@supcon.com
 * Desc 检修作业票submit
 */
@ContractFactory(entites = {BapResultEntity.class, CommonEntity.class})
public interface WorkTicketSubmitAPI {
    void submit(String view,Map<String, Object> queryParams, Map<String, Object> attachmentMap, String __pc__);

    /**
     * 停电弃审
     * @param offApplyTableNo
     */
    void retrial(String offApplyTableNo);
}
