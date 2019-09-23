package com.supcon.mes.module_xj.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntityDao.Properties;
import com.supcon.mes.module_xj.model.bean.XJWorkItemListEntity;
import com.supcon.mes.module_xj.model.contract.XJWorkItemContract;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */

public class XJWorkItemPresenter extends XJWorkItemContract.Presenter {

    @SuppressLint("CheckResult")
    @Override
    public void getXJWorkItemList(long areaId, long taskId, String deviceName, Boolean isFinish) {

//        List<XJWorkItemEntity> list = EamApplication.dao().getXJWorkItemEntityDao().queryBuilder().list();
//        for (XJWorkItemEntity xjWorkItemEntity:list){
//            if (!TextUtils.isEmpty(xjWorkItemEntity.candidateValue) && xjWorkItemEntity.candidateValue.contains("是,否")){
//                xjWorkItemEntity.defaultVal = "否";
//            }
//            if (!TextUtils.isEmpty(xjWorkItemEntity.candidateValue) && xjWorkItemEntity.candidateValue.contains("正常,不正常")){
//                xjWorkItemEntity.defaultVal = "正常";
//            }
//            EamApplication.dao().getXJWorkItemEntityDao().update(xjWorkItemEntity);
//        }


        List<XJWorkItemEntity> xjWorkItemEntities = EamApplication.dao().getXJWorkItemEntityDao().queryBuilder()
                .where(Properties.AreaId.eq(areaId), Properties.TaskId.eq(taskId))
                .where(Properties.Ip.eq(EamApplication.getIp()))
                .where(isFinish == null ? createEmptyCondition() : Properties.IsFinished.eq(isFinish))
                .where(Properties.EquipmentName.like("%" + deviceName + "%"))
                .orderAsc(Properties.EquipmentName, Properties.ItemOrder, Properties.Part)
                .list();

        XJWorkItemListEntity xjWorkItemListEntity = new XJWorkItemListEntity();
        xjWorkItemListEntity.result = xjWorkItemEntities;
        xjWorkItemListEntity.success = true;

        getView().getXJWorkItemListSuccess(xjWorkItemListEntity);

    }

    private WhereCondition createEmptyCondition() {
        return new WhereCondition.StringCondition("1=1");
    }

}
