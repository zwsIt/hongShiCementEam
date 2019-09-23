package com.supcon.mes.module_login.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.common.com_http.NullEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.ModuleAuthorizationListEntity;
import com.supcon.mes.module_login.model.bean.LicenseEntity;
import com.supcon.mes.module_login.model.bean.LoginEntity;

/**
 * Created by wangshizhan on 2017/12/28.
 * Email:wangshizhan@supcon.com
 */
@ContractFactory(entites = {LoginEntity.class, LoginEntity.class, LoginEntity.class, ModuleAuthorizationListEntity.class, NullEntity.class})
public interface LoginAPI {
    void dologin(String username, String pwd);
    void dologinWithToken(String username, String pwd, String token1);
    void dologinWithSuposPW(String username, String supospwd);
    void getLicenseInfo(String moduleCodes);
    void getAccountInfo(String username);
}

