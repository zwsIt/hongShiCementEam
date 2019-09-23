package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.common.com_http.NullEntity;

import java.util.Map;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {NullEntity.class, NullEntity.class, NullEntity.class})
public interface OLXJTaskStatusAPI {

    void updateStatus(long staffId, String taskId);

    void cancelTasks(String taskIDs, String changeState);

    void endTasks(String taskIDs, String endReason, boolean isFinish);
}
