package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2019/12/26
 * Email:wangshizhan@supcom.com
 */
public class BuildVersionEntity extends BaseEntity {

    /**
     * {
     *     "name":"海螺移动设备",
     *     "version":"44",
     *     changelog: "增加检测更新",
     *     "updated_at":1577340882,
     *     "versionShort":"8.20.05.07",
     *     "build":"44",
     *     "installUrl":"http://download.fir.im/apps/5ddb955cb2eb46487e3ccee8/install?download_token=23387127b212dff5ef79c8415655afd1&source=update",
     *     "install_url":"http://download.fir.im/apps/5ddb955cb2eb46487e3ccee8/install?download_token=23387127b212dff5ef79c8415655afd1&source=update",
     *     "direct_install_url":"http://download.fir.im/apps/5ddb955cb2eb46487e3ccee8/install?download_token=23387127b212dff5ef79c8415655afd1&source=update",
     *     "update_url":"http://fir.im/conch",
     *     "binary":{
     *         "fsize":51547174
     *     }
     * }
     */

    public String name; // 应用名称
    public String version; // 版本
    public String changelog; // 更新日志
    public Long updated_at; //
    public String versionShort; // 版本编号(兼容旧版字段)
    public String build; // 编译号
    public String installUrl; // 安装地址（兼容旧版字段）
    public String install_url; // 安装地址(新增字段)
    public String direct_install_url;
    public String update_url; // 更新地址(新增字段)
    public BinaryInfo binary; // 更新文件的对象，仅有大小字段fsize
    public String msg;

    class BinaryInfo extends BaseEntity{
        public long fsize;
    }

}
