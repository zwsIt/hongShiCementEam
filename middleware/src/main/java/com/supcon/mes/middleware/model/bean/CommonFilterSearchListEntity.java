package com.supcon.mes.middleware.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2210:46
 */
public class CommonFilterSearchListEntity extends ResultEntity{
    public List<TagEntity> result;
    public static CommonFilterSearchListEntity nil() {
        return new CommonFilterSearchListEntity();
    }

    public static CommonFilterSearchListEntity success() {
        CommonFilterSearchListEntity result=  new CommonFilterSearchListEntity();
        result.success = true;
        result.result = new ArrayList<>();
        return result;
    }

    public void commonListEntity(List tagList) {
        result = tagList;
    }

    public CommonFilterSearchListEntity appendEntity(TagEntity entity) {
        if(null ==result) {
            result = new ArrayList<>();
        }
        result.add(entity);
        return this;
    }
}
