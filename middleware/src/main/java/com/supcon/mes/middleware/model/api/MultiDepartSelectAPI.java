package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.AreaMultiStageEntity;
import com.supcon.mes.middleware.presenter.MultiDepartSelectPresenter;

/**
 * @Author xushiyun
 * @Create-time 7/22/19
 * @Pageage com.supcon.mes.middleware.model.api
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@ContractFactory(entites = MultiDepartSelectPresenter.AreaMultiStageEntity.class)
public interface MultiDepartSelectAPI {
    void getDepartmentInfoList(String id);
}
