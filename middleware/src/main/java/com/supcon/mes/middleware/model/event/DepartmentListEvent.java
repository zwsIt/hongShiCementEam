package com.supcon.mes.middleware.model.event;

import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.DepartmentInfoListEntity;

import java.util.List;

public class DepartmentListEvent extends BaseEvent {
    public List<DepartmentInfo> result;
    public DepartmentListEvent(String err, boolean success) {
        super(success, err);
    }
    public DepartmentListEvent(){

    }
}
