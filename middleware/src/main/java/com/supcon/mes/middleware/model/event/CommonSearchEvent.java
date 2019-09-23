package com.supcon.mes.middleware.model.event;

import com.supcon.mes.middleware.model.bean.CommonSearchEntity;

import java.util.List;

/**
 * @author Xushiyun
 * @date 2018/8/29
 * Email:ciruy_victory@gmail.com
 */
public class CommonSearchEvent {

    public boolean IS_MULTI;

    public String flag;
    /**
     * 应对单选返回数据
     */
    public CommonSearchEntity commonSearchEntity;
    /**
     * 应对多选返回数据
     */
    public List<CommonSearchEntity> mCommonSearchEntityList;
}
