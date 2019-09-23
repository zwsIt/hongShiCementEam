package com.supcon.mes.module_sbda.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sbda.model.bean.SBDAViewEntity;

import java.util.Map;

/**
 * Created by Xushiyun on 2018/6/14.
 * Email:ciruy_victory@gmail.com
 */
@ContractFactory(entites = {SBDAViewEntity.class})
public interface SBDAViewAPI {
    void getSBDAItem(Long eamId);
}
