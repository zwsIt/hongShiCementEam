package com.supcon.mes.module_login.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.common.com_http.NullEntity;
import com.supcon.mes.module_login.model.bean.WorkInfo;

import java.util.List;

/**
 * Created by wangshizhan on 2017/12/27.
 * Email:wangshizhan@supcon.com
 */
@ContractFactory(entites = {NullEntity.class})
public interface WorkSettingAPI {
    void setWork(List<WorkInfo> workInfoList);  //设置功能打开
}
