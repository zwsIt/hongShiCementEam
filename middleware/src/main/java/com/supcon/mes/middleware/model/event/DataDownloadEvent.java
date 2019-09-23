package com.supcon.mes.middleware.model.event;

/**
 * Created by wangshizhan on 2018/3/24.
 * Email:wangshizhan@supcon.com
 */

public class DataDownloadEvent extends BaseEvent{

    public DataDownloadEvent(boolean isSuccess){
        super(isSuccess);
    }

    public DataDownloadEvent(boolean isSuccess, String msg){
        super(isSuccess, msg);
    }

    private String moduleName;

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }
}
