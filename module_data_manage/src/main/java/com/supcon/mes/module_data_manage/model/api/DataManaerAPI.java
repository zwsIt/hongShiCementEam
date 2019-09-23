package com.supcon.mes.module_data_manage.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.common.com_http.NullEntity;
import com.supcon.mes.middleware.constant.DataModule;
import com.supcon.mes.middleware.model.event.DataParseEvent;
import com.supcon.mes.middleware.model.event.DataUploadEvent;

/**
 * Created by wangshizhan on 2017/12/28.
 * Email:wangshizhan@supcon.com
 */

@ContractFactory(entites = {NullEntity.class, DataUploadEvent.class, DataParseEvent.class})
public interface DataManaerAPI {


    /**
     * 下载任务
     * @param module 模块名字 @See Module; 默认为空，即获取所有模块的任务；
     * @param url 下载文件存放地址
     */
    void download(DataModule module, String url);


    /**
     * 上传任务
     * @param module 模块名字 @See Module; 默认为空，即上传所有模块的任务
     */
    void upload(DataModule module);

    /**
     * 解析数据，保存本地
     * @param module 模块名字 @See Module; 默认为空，即获取所有模块的任务；
     * @param url 下载文件存放地址
     */
    void parseData(DataModule module, String url);

}
