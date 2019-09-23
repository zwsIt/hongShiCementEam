package com.supcon.mes.module_txl.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.TxlListEntity;

/**
 * @Author xushiyun
 * @Create-time 7/11/19
 * @Pageage com.supcon.mes.module_txl.model.api
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@ContractFactory(entites = {TxlListEntity.class})
public interface TxlListAPI {
    void getTxlList(int pageNum,String p1,String p2,String p3);
}
