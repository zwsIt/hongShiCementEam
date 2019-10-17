package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonEntity;

import java.io.File;

/**
 * Created by wangshizhan on 2018/8/1
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {File.class, CommonEntity.class})
public interface StaffPicDownloadAPI {
    void getStaffPic(long id);

    void getDocIds(long linkId);
}
