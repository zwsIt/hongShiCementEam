package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;

import java.io.File;

/**
 * Created by wangshizhan on 2018/8/1
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = File.class)
public interface StaffPicDownloadAPI {
    void getStaffPic(long id);
}
