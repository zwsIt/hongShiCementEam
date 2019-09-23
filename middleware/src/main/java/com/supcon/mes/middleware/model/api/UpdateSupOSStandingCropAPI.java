package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

@ContractFactory(entites = {CommonListEntity.class})
public interface UpdateSupOSStandingCropAPI {

    /**
     * @param sparePartCodes 备件编码字符串
     * @return
     * @description 更新现存量
     * @author zhangwenshuai1 2018/10/10
     */
    void updateStandingCrop(String sparePartCodes);
}
