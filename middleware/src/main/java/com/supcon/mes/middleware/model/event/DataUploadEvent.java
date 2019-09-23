package com.supcon.mes.middleware.model.event;

/**
 *
 * @author wangshizhan
 * @date 2018/3/24
 * Email:wangshizhan@supcon.com
 */

public class DataUploadEvent extends BaseEvent{

    public DataUploadEvent(boolean isSuccess){
        super(isSuccess);
    }

    public DataUploadEvent(boolean isSuccess, String msg){
        super(isSuccess, msg);
    }

    private String moduleName;
    private int size;


    public DataUploadEvent(boolean isSuccess,String moduleName, String msg){
        super(isSuccess, msg);
        this.setModuleName(moduleName);
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
