package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.TxlListEntity;

/**
 * @Author xushiyun
 * @Create-time 7/23/19
 * @Pageage com.supcon.mes.middleware.model.api
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@ContractFactory(entites = TxlListEntity.class)
public interface OnlineStaffListAPI {
    void getOnlineStaffList();
}
