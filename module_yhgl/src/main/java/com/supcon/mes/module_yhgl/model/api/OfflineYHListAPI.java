package com.supcon.mes.module_yhgl.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_yhgl.model.bean.YHDtoListEntity;
import com.supcon.mes.module_yhgl.model.bean.YHListEntity;

import java.util.Map;

/**
 * Created by xushiyun on 2018/8/7
 * Email:ciruy.victory@gmail.com
 * @author xushiyun
 */
@ContractFactory(entites = YHDtoListEntity.class)
public interface OfflineYHListAPI {

    void queryYHList(int pageNum, Map<String, Object> queryParams);

}
