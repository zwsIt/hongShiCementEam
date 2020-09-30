package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.LongResultEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/6/24
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@ContractFactory(entites = {CommonEntity.class, LongResultEntity.class,CommonEntity.class})
public interface WorkFlowKeyAPI {
    /**
     * entityCode:实体编码
     * type（用于停送电，区别是停电还是送电，非停送电传null即可）：BEAMEle_1.0.0_onOrOff_eleOffList（停电）、BEAMEle_1.0.0_onOrOff_eleOnList（送电）
     * @param entityCode
     * @param type
     */
    void queryWorkFlowKeyOnly(String entityCode, String type);

    /**
     * 获取模块实体的工作流key，同时根据key 获取是否有单据制定权限
     * @param entityCode 同上
     * @param type 同上
     */
    void queryWorkFlowKeyAndPermission(String entityCode, String type);

    /**
     * 获取模块实体的工作流key，同时根据key 获取单据__pc__
     * @param operateCode 操作编码
     * @param entityCode 实体编码
     * @param type 同上
     */
    void queryWorkFlowKeyToPc(String operateCode,String entityCode, String type);
}
