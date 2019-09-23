package com.supcon.mes.module_yhgl.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_yhgl.model.bean.YHListEntity;

import java.util.Map;

/**
 * Created by wangshizhan on 2018/8/14
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = YHListEntity.class)
public interface YHListAPI {

    void queryYHList(int pageNum, Map<String, Object> queryParams);

}
