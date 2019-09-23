package com.supcon.mes.module_data_manage.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.QXDealTypeEntity;
import com.supcon.mes.middleware.model.bean.QXTypeEntity;
import com.supcon.mes.middleware.model.bean.RunStateEntity;

import java.util.List;

/**
 * Created by shenrong on 2017/12/12.
 */

public class EamInfo extends BaseEntity {

    public AccountInfo accountInfo ;
    public List<QXTypeEntity> faultType;    //缺陷类型
    public List<QXDealTypeEntity> dealType; //处理方式
    public List<RunStateEntity> runStateParams;
    public List<CommonDeviceEntity> devices;//弃用

    public List<StateType> stateType;//设备状态
    public boolean isAutoRepair;
}
