package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;

import java.util.Map;

/**
 * WXGDListAPI
 * created by zhangwenshuai1 2018/8/8
 */

@ContractFactory(entites = {WXGDListEntity.class})
public interface WXGDListAPI {

    void listWxgds(int pageNum, Map<String,Object> queryParam);
}
