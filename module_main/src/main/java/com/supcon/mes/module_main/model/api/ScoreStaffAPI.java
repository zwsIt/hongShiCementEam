package com.supcon.mes.module_main.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/29
 * ------------- Description -------------
 */
@ContractFactory(entites = {CommonEntity.class})
public interface ScoreStaffAPI {
    void getPersonScore(String staffID);
}
