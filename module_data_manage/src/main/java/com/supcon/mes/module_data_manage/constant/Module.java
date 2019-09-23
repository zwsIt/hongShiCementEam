package com.supcon.mes.module_data_manage.constant;

/**
 * Created by wangshizhan on 2018/3/24.
 * Email:wangshizhan@supcon.com
 */

public enum Module {

    BASE("用户基础信息", "", ""),
    XJ_BASE("巡检基础信息", "", ""),
    EAM_BASE("设备信息", "", ""),
    XJ("巡检任务", "xj_download.zip", "xj_upload.json"),
    YH("隐患复查", "yh_download.zip", "yh_upload.json");

    private String moduleName;
    private String downloadFileName;
    private String uploadFileName;

    Module(String name, String downloadFileName, String uploadFileName){
        this.moduleName = name;
        this.downloadFileName = downloadFileName;
        this.uploadFileName = uploadFileName;
    }

    public String getModuelName() {
        return moduleName;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }


    public String getUploadFileName() {
        return uploadFileName;
    }


    /**
     * 根据模块名寻找模块
     * @param moduleName
     * @return
     */
    public static Module getModule(String moduleName){

        for(Module module: Module.values()){
            if(module.getModuelName().equals(moduleName)){
                return module;
            }
        }

        return null;
    }

    /**
     * 根据枚举值寻找模块名
     * @param name
     * @return
     */
    public static String getModuleName(String name){

        for(Module module: Module.values()){
            if(module.name().equals(name)){
                return module.getModuelName();
            }
        }

        return null;
    }
}
