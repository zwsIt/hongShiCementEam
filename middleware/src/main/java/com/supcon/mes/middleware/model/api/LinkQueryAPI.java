package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.LinkListEntity;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {LinkListEntity.class, LinkListEntity.class})
public interface LinkQueryAPI {
    void queryCurrentLink(long pendingId);
    void queryStartLink(String flowKey);
}
