package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.UserInfoListEntity;

/**
 * Created by wangshizhan on 2018/9/19
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = UserInfoListEntity.class)
public interface UserListQueryAPI {

    void queryUserInfoList(String staffName, int pageNo);


}
