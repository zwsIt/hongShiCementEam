package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;

import java.util.Map;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {BapResultEntity.class, BapResultEntity.class, BapResultEntity.class, BapResultEntity.class, BapResultEntity.class})
public interface WXGDSubmitAPI {

    void doReceiveSubmit(Map<String, Object> map);

    void doExecuteSubmit(Map<String, Object> map);

    /**
     * @param
     * @return
     * @description 派工提交（作废/驳回）
     * @author zhangwenshuai1 2018/9/4
     */
    void doDispatcherSubmit(Map<String, Object> map);

    /**
     * @param
     * @return
     * @description 预警派工提交（作废/驳回）
     * @author zhangwenshuai1 2018/9/4
     */
    void doDispatcherWarnSubmit(Map<String, Object> map);

    /**
     * @param
     * @return
     * @description 验收提交
     * @author zhangwenshuai1 2018/9/11
     */
    void doAcceptChkSubmit(Map<String, Object> map, Map<String, Object> attachmentMap);


}
