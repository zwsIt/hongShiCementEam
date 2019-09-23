package com.supcon.mes.module_yhgl.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.YHEntityVo;


/**
 * Created by xushiyun on 2018/8/22
 * Email:ciruy.victory@gmail.com
 */
@ContractFactory(entites = {BapResultEntity.class})
public interface OfflineYHSubmitAPI {
    void doSubmit(YHEntityVo yhEntityDto);
    void doDelete(YHEntityVo yhEntityDto);
}
