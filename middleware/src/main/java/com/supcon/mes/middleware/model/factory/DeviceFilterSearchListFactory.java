package com.supcon.mes.middleware.model.factory;

import android.annotation.SuppressLint;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntityDao;
import com.supcon.mes.middleware.model.bean.CommonLabelEntity;
import com.supcon.mes.middleware.model.bean.TagEntity;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2310:15
 */
public final class DeviceFilterSearchListFactory implements BaseFilterSearchListFactory {
    private DeviceFilterSearchListFactory() {

    }

    private static class SingleHolder {
        static DeviceFilterSearchListFactory SINGLETON = new DeviceFilterSearchListFactory();
    }

    @SuppressLint("CheckResult")
    @Override
    public List getFilterSearchList(int pageIndex, Map<String, Object> param) {
        List<TagEntity> result = new ArrayList<>();
        Flowable.fromIterable(EamApplication.dao().getCommonDeviceEntityDao()
                .queryBuilder()
                .where(null == param.get(Constant.FilterSearchParam.DEVICE_BLUR) ?
                        nil() : CommonDeviceEntityDao.Properties.EamName
                        .like("%" + param.get(Constant.FilterSearchParam.DEVICE_BLUR) + "%"))
                .orderAsc(CommonDeviceEntityDao.Properties.EamName)
                .offset(pageIndex < 0 ? 0 : (pageIndex - 1) * 10).limit(pageIndex < 0 ? Integer.MAX_VALUE : 10).list())
                .map((Function<CommonDeviceEntity, TagEntity>) commonDeviceEntity ->
                        CommonLabelEntity.nil().name(commonDeviceEntity.eamName).id(commonDeviceEntity.eamId + "")
                        .code(commonDeviceEntity.eamCode))
                .subscribe(result::add);
        return result;
    }

    private static WhereCondition nil() {
        return new WhereCondition.StringCondition("1=1");
    }

    public static BaseFilterSearchListFactory singleton() {
        return SingleHolder.SINGLETON;
    }
}
