package com.supcon.mes.middleware.model.event;


import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;

/**
 * Created by wangshizhan on 2018/3/22.
 * Email:wangshizhan@supcon.com
 */

public class SynDeviceEevent extends BaseEntity {

    public SynDeviceEevent(CommonDeviceEntity commonDeviceEntity){
        mCommonDeviceEntity  = commonDeviceEntity;
    }

    private boolean isChecked;
    private CommonDeviceEntity mCommonDeviceEntity;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public CommonDeviceEntity getCommonDeviceEntity() {
        return mCommonDeviceEntity;
    }

    public void setCommonDeviceEntity(CommonDeviceEntity commonDeviceEntity) {
        mCommonDeviceEntity = commonDeviceEntity;
    }
}
