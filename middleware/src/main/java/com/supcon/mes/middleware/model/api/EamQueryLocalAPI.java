package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;

import java.util.List;

/**
 * Created by zws on 2020/5/28
 * Email:zhangwenshuai1@supcom.com
 */
@ContractFactory(entites = {List.class})
public interface EamQueryLocalAPI {

    void listEamLocal(int pageNo, int pageSize, String search, String other, boolean isCard);

}
