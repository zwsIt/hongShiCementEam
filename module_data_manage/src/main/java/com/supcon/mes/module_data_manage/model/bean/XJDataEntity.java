package com.supcon.mes.module_data_manage.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamHistoryEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.XJAreaEntity;
import com.supcon.mes.middleware.model.bean.XJExemptionEntity;
import com.supcon.mes.middleware.model.bean.XJHistoryEntity;
import com.supcon.mes.middleware.model.bean.XJPathEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;

import java.util.List;

/**
 * Created by wangshizhan on 2018/3/24.
 * Email:wangshizhan@supcon.com
 */

public class XJDataEntity extends BaseEntity {

    public List<XJPathEntity> mobileWorkGroups;  //巡检任务

    public List<XJAreaEntity> mobileEamWorks;  //巡检区域

    public List<XJWorkItemEntity> mobileEamWorkItems;  //巡检项

    public List<SystemCodeEntity> systemCodes;

    public List<XJHistoryEntity> mobileEamHistory;

    public List<XJExemptionEntity> mobileEamItemConc;  //巡检免检项

}
