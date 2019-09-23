package com.supcon.mes.module_login.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;

/**
 * Created by wangshizhan on 2019/8/6
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {BapResultEntity.class, BapResultEntity.class})
public interface PasswordAPI {

    void checkPwd(String oldPassword);

    void saveNewPwd(String oldPassword, String newPassword);
}
