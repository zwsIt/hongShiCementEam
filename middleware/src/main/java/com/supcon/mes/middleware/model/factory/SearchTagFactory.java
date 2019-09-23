package com.supcon.mes.middleware.model.factory;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonLabelEntity;
import com.supcon.mes.middleware.model.bean.TagEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntityDao;
import com.supcon.mes.middleware.model.bean.CommonFilterSearchListEntity;
import com.supcon.mes.middleware.util.ObjectUtil;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2114:16
 */
public final class SearchTagFactory {
    /**
     * 搜索最近搜索过的数据信息
     */
    public static CommonFilterSearchListEntity getRecentLabel(SearchContentFactory.FilterType type, Map<String, Object> param) {
        return CommonFilterSearchListEntity.nil();
    }

    /**
     * 推荐的标签数据信息
     */
    @SuppressLint("CheckResult")
    public static CommonFilterSearchListEntity getAllLabel(SearchContentFactory.FilterType type, Map<String, Object> param) {
        CommonFilterSearchListEntity resultListEntity = CommonFilterSearchListEntity.success()
                .appendEntity(CommonLabelEntity
                        .createEntityWithName("不限"));
        @SuppressLint("UseSparseArrays") Map<String, TagEntity> entityHashMap = new HashMap<>(0);
//        Flowable.fromIterable(EamApplication.dao().getCommonDeviceEntityDao().loadAll())
//                .filter(commonDeviceEntity -> !entityHashMap.keySet().contains(commonDeviceEntity.eamId))
//                .map(commonDeviceEntity -> {
//                    CommonLabelEntity commonLabelEntity = CommonLabelEntity.nil()
//                            .id(commonDeviceEntity.eamId + "")
//                            .name(commonDeviceEntity.eamName)
//                            .code(commonDeviceEntity.eamCode);
//                    entityHashMap.put(commonDeviceEntity.eamId, commonLabelEntity);
//                    return commonLabelEntity;
//                })
//                .subscribe(commonLabelEntity -> resultListEntity.result.add(commonLabelEntity));
        Flowable.fromIterable(EamApplication.dao().getXJWorkItemEntityDao().queryBuilder()
                .where(null==param.get(Constant.FilterSearchParam.AREA_ID)?nil():XJWorkItemEntityDao.Properties.AreaId.eq(param.get(Constant.FilterSearchParam.AREA_ID)))
                .orderAsc(XJWorkItemEntityDao.Properties.EquipmentName).list())
                .filter(xjWorkItemEntity -> (!entityHashMap.keySet().contains(xjWorkItemEntity.equipmentId)) && !TextUtils.isEmpty(xjWorkItemEntity.equipmentId))
                .map(xjWorkItemEntity -> {
                    CommonLabelEntity commonLabelEntity = CommonLabelEntity.nil()
                            .id(xjWorkItemEntity.equipmentId).name(xjWorkItemEntity.equipmentName);
                    entityHashMap.put(xjWorkItemEntity.equipmentId, commonLabelEntity);
                    return commonLabelEntity;
                }).subscribe(commonLabelEntity -> resultListEntity.result.add(commonLabelEntity));
        return resultListEntity;
    }
    private  static WhereCondition nil() {
        return new WhereCondition.StringCondition("1=1");
    }
}
