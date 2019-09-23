package com.supcon.mes.module_sbda_online.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.module_sbda_online.model.bean.ScreenListEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
@ContractFactory(entites = ScreenListEntity.class)
public interface ScreenTypeAPI {
    void screenPart(CustomFilterView customFilterView);
}
