package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

public class MaintainEntity extends BaseEntity {

    public Long id;
    public String basicJwx;//是否历史数据
    public JWXItem jwxItemID; //业务规则
    public String remark;//备注

    public JWXItem getJwxItem() {
        if (jwxItemID == null) {
            jwxItemID = new JWXItem();
        }
        return jwxItemID;
    }

}
