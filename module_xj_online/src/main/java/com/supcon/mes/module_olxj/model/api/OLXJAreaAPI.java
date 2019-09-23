package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

/**
 * Created by wangshizhan on 2019/4/2
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {CommonBAPListEntity.class, CommonListEntity.class})
public interface OLXJAreaAPI {

    /**
     * 查询在线巡检区域列表
     */
    void getOJXJAreaList(long groupId, int pageNo);


    /**
     * 查询旧的巡检隐患
     *
     * @param groupId
     * @param isTemp
     */
    void getAbnormalInspectTaskPart(long groupId, int isTemp);

}
