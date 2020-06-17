package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkListEntity;

/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

@ContractFactory(entites = {CommonBAPListEntity.class})
public interface OLXJTodayTaskItemListAPI {

     /**
      * @method
      * @description 获取任务明细
      * @author: zhangwenshuai
      * @date: 2020/6/12 19:34
      * @param  * @param null
      * @return
      */
    void getWorkItemList(long taskId, long workId, int pageIndex);
}
