package com.supcon.mes.middleware.constant;

/**
 * Created by wangshizhan on 2018/3/24.
 * Email:wangshizhan@supcon.com
 */

public enum DataModule {

    EAM_BASE("设备基础信息", "", ""),
    EAM_DEVICE("设备信息", "", ""),
    XJ_BASE("巡检基础信息", "", ""),
    XJ("巡检任务", "xj_download.zip", "xj_upload.json"),
    QX("缺陷", "qx_download.zip", "qx_upload.json"),
    YH("隐患单据", "yh_download.zip", "yh_upload.json");

    private String moduleName;
    private String downloadFileName;
    private String uploadFileName;

    DataModule(String name, String downloadFileName, String uploadFileName){
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
    public static DataModule getModule(String moduleName){

        for(DataModule dataModule : DataModule.values()){
            if(dataModule.getModuelName().equals(moduleName)){
                return dataModule;
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

        for(DataModule dataModule : DataModule.values()){
            if(dataModule.name().equals(name)){
                return dataModule.getModuelName();
            }
        }

        return null;
    }
}
