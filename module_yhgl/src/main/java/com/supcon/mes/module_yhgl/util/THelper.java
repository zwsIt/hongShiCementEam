package com.supcon.mes.module_yhgl.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */
public class THelper<T> {

    protected Class<?> clazz;

    public THelper(T t) {
        clazz = t.getClass();

//        while (clazz != Object.class) {
//            Type type = clazz.getGenericSuperclass();
//            if (type instanceof ParameterizedType) {
//                Type[] args = ((ParameterizedType) type).getActualTypeArguments();
//                if (args[0] instanceof Class) {
//                    this.clazz = (Class<T>) args[0];
//                    break;
//                }
//            }
//            clazz = clazz.getSuperclass();
//
//        }
    }

    public Class<?> getTClass() {
        return clazz;
    }
}
