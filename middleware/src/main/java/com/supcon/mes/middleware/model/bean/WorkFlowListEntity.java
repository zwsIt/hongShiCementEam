package com.supcon.mes.middleware.model.bean;

import com.supcon.mes.mbap.beans.LinkEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/7/20
 * Email:wangshizhan@supcom.com
 */
public class WorkFlowListEntity extends ResultEntity{

    public List<LinkEntity> linkEntities = new ArrayList<>();

    public String result;
}
