package com.supcon.mes.module_sbda.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda.model.bean.SBDAListEntity;

import java.util.Map;

/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/3/30.
 */
@ContractFactory(entites = {SBDAListEntity.class})
public interface SBDAListAPI {
//    void getAllSBDA();                                        //获取所有的设备档案信息
    void getSearchSBDA(String blurMes, Map<String, Object> params, int page, int pageNum);
}
