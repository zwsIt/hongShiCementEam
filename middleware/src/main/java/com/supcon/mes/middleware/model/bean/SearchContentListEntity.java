package com.supcon.mes.middleware.model.bean;

import java.util.List;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2114:03
 */
public class SearchContentListEntity extends ResultEntity {
    List<TagEntity> mFilterSearchEntityList;

    public List getResult() {
        return mFilterSearchEntityList;
    }

    public SearchContentListEntity success(boolean aBoolean) {
        this.success = aBoolean;
        return this;
    }

    public SearchContentListEntity result(List result) {
        this.mFilterSearchEntityList = result;
        return this;
    }

    public static SearchContentListEntity nil() {
        return new SearchContentListEntity();
    }

    public static SearchContentListEntity success() {
        return nil().success(true);
    }
}
