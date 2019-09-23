package com.supcon.mes.module_olxj.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.common.com_http.NullEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;

/**
 * Created by wangshizhan on 2019/4/4
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {NullEntity.class})
public interface OLXJWorkSubmitAPI {

    void uploadOLXJAreaData(OLXJAreaEntity areaEntity);

}
