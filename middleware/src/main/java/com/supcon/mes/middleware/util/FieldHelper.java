package com.supcon.mes.middleware.util;

import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */
public class FieldHelper {

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

    public static List<String> getSystemCodeValue(List<SystemCodeEntity> systemCodeEntityList){
        List<String> list = new ArrayList<>();
        if (systemCodeEntityList == null || systemCodeEntityList.size() <= 0){
            return list;
        }
        for (SystemCodeEntity systemCodeEntity : systemCodeEntityList){
            list.add(systemCodeEntity.value);
        }
        return list;
    }


}
