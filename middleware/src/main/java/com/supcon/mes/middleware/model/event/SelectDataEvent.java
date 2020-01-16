package com.supcon.mes.middleware.model.event;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/12/17
 * ------------- Description -------------
 */
public class SelectDataEvent<T> extends BaseEvent {
    private T data;

    public SelectDataEvent(T data){
        this.data = data;
    }


    public T getEntity() {
        return data;
    }
}
