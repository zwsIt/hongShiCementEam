package com.supcon.mes.middleware.model.bean;

/**
 * @Author xushiyun
 * @Create-time 6/14/19
 * @Pageage com.supcon.mes.middleware.model.bean
 * @Project eam
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public interface IDataInjector<T> {
    /**
     * 数据注入方法
     * @param data 需要注入到对应目标的数据
     */
    void inject(T data);
    
    default void inject(T data, int type){
    
    }
}
