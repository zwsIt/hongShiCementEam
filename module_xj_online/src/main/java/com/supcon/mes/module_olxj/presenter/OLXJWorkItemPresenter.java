package com.supcon.mes.module_olxj.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntityDao.Properties;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkListEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJWorkListContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */

public class OLXJWorkItemPresenter extends OLXJWorkListContract.Presenter {

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

        OLXJWorkListEntity OLXJWorkListEntity = new OLXJWorkListEntity();
        OLXJWorkListEntity.result = xjWorkItemEntities;
        OLXJWorkListEntity.success = true;

        getView().getXJWorkItemListSuccess(OLXJWorkListEntity);

    }

    @Override
    public void getWorkItemList(long taskId, int pageNum) {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("potrolTaskWF.id", taskId);
        queryParams.put("dgPage.pageNo", pageNum);
        queryParams.put("dgPage.pageSize", 500);
        queryParams.put("dgPage.maxPageSize", 500);
        mCompositeSubscription.add(
                OLXJClient.queryWorkList(queryParams)
                        .onErrorReturn(new Function<Throwable, CommonBAPListEntity<OLXJWorkItemEntity>>() {
                            @Override
                            public CommonBAPListEntity<OLXJWorkItemEntity> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                                commonBAPListEntity.success = false;
                                commonBAPListEntity.errMsg = throwable.toString();
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<OLXJWorkItemEntity>>() {
                            @Override
                            public void accept(CommonBAPListEntity<OLXJWorkItemEntity> olxjWorkItemEntityCommonBAPListEntity) throws Exception {
                                if (olxjWorkItemEntityCommonBAPListEntity.result != null) {
                                    Objects.requireNonNull(getView()).getWorkItemListSuccess(olxjWorkItemEntityCommonBAPListEntity);
                                } else {
                                    Objects.requireNonNull(getView()).getWorkItemListFailed(olxjWorkItemEntityCommonBAPListEntity.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Objects.requireNonNull(getView()).getWorkItemListFailed(throwable.toString());
                            }
                        }));


    }

    @Override
    public void getWorkItemListRef(long groupId, int pageNum) {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("workGroupID", groupId);
        queryParams.put("page.pageNo", pageNum);
        queryParams.put("page.pageSize", 50);
        queryParams.put("page.maxPageSize", 500);
        mCompositeSubscription.add(
                OLXJClient.queryWorkListRef(queryParams)
                        .onErrorReturn(new Function<Throwable, CommonBAPListEntity<OLXJWorkItemEntity>>() {
                            @Override
                            public CommonBAPListEntity<OLXJWorkItemEntity> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                                commonBAPListEntity.success = false;
                                commonBAPListEntity.errMsg = throwable.toString();
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<OLXJWorkItemEntity>>() {
                            @Override
                            public void accept(CommonBAPListEntity<OLXJWorkItemEntity> olxjWorkItemEntityCommonBAPListEntity) throws Exception {
                                if (olxjWorkItemEntityCommonBAPListEntity.result != null) {
                                    Objects.requireNonNull(getView()).getWorkItemListRefSuccess(olxjWorkItemEntityCommonBAPListEntity);
                                } else {
                                    Objects.requireNonNull(getView()).getWorkItemListRefFailed(olxjWorkItemEntityCommonBAPListEntity.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Objects.requireNonNull(getView()).getWorkItemListFailed(throwable.toString());
                            }
                        }));
    }

    private WhereCondition createEmptyCondition() {
        return new WhereCondition.StringCondition("1=1");
    }

}
