package com.supcon.mes.module_contact.model.api;

import com.app.annotation.apt.ContractFactory;

import java.util.List;

/**
 * Created by wangshizhan on 2019/12/3
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {List.class})
public interface RecentContactAPI {

    void getRecentContactList(int pageNo, int pageSize, String content);


}
