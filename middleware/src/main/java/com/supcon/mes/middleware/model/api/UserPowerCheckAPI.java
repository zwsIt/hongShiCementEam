package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.LongResultEntity;

import org.json.JSONObject;

/**
 * Created by wangshizhan on 2018/9/21
 * Email:wangshizhan@supcom.com
 * 获取菜单权限
 */
@ContractFactory(entites =Object.class)
public interface UserPowerCheckAPI {

    void checkUserPower(long companyId, String menuOperateCodes);
}
