package com.supcon.mes.module_main.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.Map;

/**
 * @Description: 预警提醒待办
 * @Author: zhangwenshuai
 * @CreateDate: 2020/6/2 17:44
 */
@ContractFactory(entites = {CommonBAPListEntity.class})
public interface WarnPendingListAPI {
     /**
      * @method
      * @description
      * @author: zhangwenshuai
      * @date: 2020/6/2 18:27
      * @param  * @param null
      * @return
      */
    void listWarnPending(Map<String, Object> queryParam, int page, int pageSize);
}
