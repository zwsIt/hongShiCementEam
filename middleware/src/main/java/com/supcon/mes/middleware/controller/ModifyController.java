package com.supcon.mes.middleware.controller;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.GsonUtil;

/**
 * Created by wangshizhan on 2018/9/4
 * Email:wangshizhan@supcom.com
 */
public class ModifyController<T extends BaseEntity> extends BasePresenterController {

    private String originalEntity;  //刚初始化时的实体对象，用来判断是否修改

    public ModifyController(T entity){

        originalEntity = GsonUtil.gsonString(entity);

    }
    public boolean  isModifyed(T currentEntity){
        return !GsonUtil.gsonString(currentEntity).equals(originalEntity);

    }


}
