package com.supcon.mes.middleware.model.bean;

import java.util.List;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2114:18
 */
public class CommonLabelListEntity extends ResultEntity {
     List<CommonLabelEntity> mCommonLabelEntities;
    public static CommonLabelListEntity nil(){
        return new CommonLabelListEntity();
    }

    public static CommonLabelListEntity success() {
        return nil().success(true);
    }
    public  CommonLabelListEntity result(List result) {
        this.mCommonLabelEntities = result;
        return this;
    }
    public List<CommonLabelEntity>getResult() {
        return mCommonLabelEntities;
    }

    public CommonLabelListEntity success(boolean success) {
        this.success = success;
        return this;
    }
}
