package com.supcon.mes.module_xj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_xj.model.bean.XJPathListEntity;

import java.util.Map;

/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

@ContractFactory(entites = {XJPathListEntity.class})
public interface XJPathAPI {
    /**
     * 查询本地巡检路线列表
     * @param queryParam  state   已检/待检状态   startEndTime   起止时间
     */
    void getXJPathList(Map<String, Object> queryParam);
}
