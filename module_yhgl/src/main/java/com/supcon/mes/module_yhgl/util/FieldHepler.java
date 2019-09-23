package com.supcon.mes.module_yhgl.util;

import java.lang.reflect.Field;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */
public class FieldHepler {

    public static String getFieldValue(Class<?> clazz, Object obj, String fieldName){
        String result = "";

        try {
            for(Field field : clazz.getFields()){
                if(field.getName().equals(fieldName)){
                    return String.valueOf(field.get(obj));
                }
            }
//            Field field = clazz.getField(fieldName);
//            result = field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }


}
