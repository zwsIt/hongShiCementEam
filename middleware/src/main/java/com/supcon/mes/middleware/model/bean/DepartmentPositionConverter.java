package com.supcon.mes.middleware.model.bean;

import com.supcon.mes.mbap.utils.GsonUtil;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/12/20
 * ------------- Description -------------
 */
public class DepartmentPositionConverter implements PropertyConverter<DepartmentPosition, String> {
    @Override
    public DepartmentPosition convertToEntityProperty(String databaseValue) {
        return GsonUtil.gsonToBean(databaseValue, DepartmentPosition.class);
    }

    @Override
    public String convertToDatabaseValue(DepartmentPosition entityProperty) {
        return GsonUtil.gsonString(entityProperty);
    }
}
