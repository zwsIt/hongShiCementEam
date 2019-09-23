package com.supcon.mes.middleware.model.event;

/**
 * Created by wangshizhan on 2018/3/24.
 * Email:wangshizhan@supcon.com
 */

public class DataParseEvent extends BaseEvent{

    private String moduleName;
    private int size ;

    public DataParseEvent(boolean isSuccess){
        super(isSuccess);
    }

    public DataParseEvent(boolean isSuccess, String msg){
        super(isSuccess, msg);
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
