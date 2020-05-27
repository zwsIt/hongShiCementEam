package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;

import java.util.List;

/**
 * Created by zhangwenshuai on 2020/5/25
 * Email:zhangwenshuai1@supcom.com
 */
@ContractFactory(entites = {List.class})
public interface RecentEamSelectAPI {

    void getRecentEamList(int pageNo, int pageSize, String content);


}
