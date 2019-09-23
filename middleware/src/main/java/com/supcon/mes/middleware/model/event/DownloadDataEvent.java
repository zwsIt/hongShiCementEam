package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/5/3.
 * Email:wangshizhan@supcon.com
 */

public class DownloadDataEvent extends BaseEntity {

    List<String> mModules = new ArrayList<>();

    public DownloadDataEvent(List<String> modules){
        if(modules != null && modules.size()!=0){
            this.mModules.addAll(modules);
        }
    }

    public List<String> getModules() {
        return mModules;
    }
}
