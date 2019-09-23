package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchListEntity;

/**
 * Created by Xushiyun on 2018/8/26.
 * Email:ciruy_victory@gmail.com
 */
@ContractFactory(entites = {CommonSearchListEntity.class})
public interface CommonSearchAPI {
    void getCommonSearchEntityList(String blurMes, String mode);
}
