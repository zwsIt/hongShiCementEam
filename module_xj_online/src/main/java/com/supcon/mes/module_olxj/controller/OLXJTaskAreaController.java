package com.supcon.mes.module_olxj.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseDataController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_olxj.model.api.OLXJAreaAPI;
import com.supcon.mes.module_olxj.model.api.OLXJWorkListAPI;
import com.supcon.mes.module_olxj.model.bean.AbnormalEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkListEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJAreaContract;
import com.supcon.mes.module_olxj.model.contract.OLXJWorkListContract;
import com.supcon.mes.module_olxj.presenter.OLXJAreaListPresenter;
import com.supcon.mes.module_olxj.presenter.OLXJWorkItemPresenter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
@Presenter(value = {OLXJWorkItemPresenter.class, OLXJAreaListPresenter.class})
public class OLXJTaskAreaController extends BaseDataController implements OLXJWorkListContract.View, OLXJAreaContract.View {

    private int currentPage = 1, cuttentAreaPage = 1;
    private List<OLXJWorkItemEntity> mOLXJWorkItemEntities = new ArrayList<>();
    private List<OLXJAreaEntity> mAreaEntities = new ArrayList<>();
    private Map<Long, OLXJAreaEntity> mAreaEntityMap = new LinkedHashMap<>();
    private OnSuccessListener<Boolean> mOnSuccessListener;
    private int type;//0  计划巡检 1 临时巡检

    private long taskId;
    private long groupID;

    public OLXJTaskAreaController(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    public void onInit() {
        super.onInit();
        OLXJTaskEntity taskEntity = (OLXJTaskEntity) getIntent().getSerializableExtra(Constant.IntentKey.XJ_TASK_ENTITY);
        if (taskEntity == null) {
            throw new IllegalArgumentException("no mTaskEntity, please check it!");
        }
        taskId = taskEntity.id;
        groupID = taskEntity.workGroupID.id;
    }

    @Override
    public void initData() {
        super.initData();

        getWorkData();
        getAreaData();
    }

    public void getData(OLXJTaskEntity taskEntity, OnSuccessListener<Boolean> listener) {
        if (taskEntity == null) {
            throw new IllegalArgumentException("no mTaskEntity, please check it!");
        }
        taskId = taskEntity.id;
        groupID = taskEntity.workGroupID.id;
        mOnSuccessListener = listener;
        mOLXJWorkItemEntities.clear();
        mAreaEntities.clear();
        mAreaEntityMap.clear();
        currentPage = 1;
        cuttentAreaPage = 1;
        getAreaData();
    }

    public void getData(long taskId, long groupID, OnSuccessListener<Boolean> listener) {
        if (taskId == 0 || groupID == 0) {
            throw new IllegalArgumentException("no mTaskEntity, please check it!");
        }
        this.taskId = taskId;
        this.groupID = groupID;
        mOnSuccessListener = listener;
        mOLXJWorkItemEntities.clear();
        mAreaEntities.clear();
        mAreaEntityMap.clear();
        currentPage = 1;
        cuttentAreaPage = 1;
        getAreaData();
    }

    private void getWorkData() {

        presenterRouter.create(OLXJWorkListAPI.class).getWorkItemList(taskId, currentPage);
    }

    private void getAreaData() {
        presenterRouter.create(OLXJAreaAPI.class).getOJXJAreaList(groupID, cuttentAreaPage);
    }

    /**
     * 获取巡检路线隐患信息
     */
    private void getAbnormalInspectTaskPart() {
        presenterRouter.create(OLXJAreaAPI.class).getAbnormalInspectTaskPart(groupID, type);
    }


    @Override
    public void getXJWorkItemListSuccess(OLXJWorkListEntity entity) {

    }

    @Override
    public void getXJWorkItemListFailed(String errorMsg) {

    }

    @Override
    public void getWorkItemListSuccess(CommonBAPListEntity entity) {

        if (entity.result != null) {
            mOLXJWorkItemEntities.addAll(entity.result);
        }

        if (entity.hasNext) {
            currentPage++;
            getWorkData();
        } else {
            updateArea();
        }
    }

    @SuppressLint("CheckResult")
    private void updateArea() {
        Flowable.fromIterable(mOLXJWorkItemEntities)
                .subscribeOn(Schedulers.newThread())
                .map(new Function<OLXJWorkItemEntity, OLXJWorkItemEntity>() {
                    @Override
                    public OLXJWorkItemEntity apply(OLXJWorkItemEntity olxjWorkItemEntity) throws Exception {
                        OLXJAreaEntity areaEntity = mAreaEntityMap.get(olxjWorkItemEntity.workID.id);
                        if (areaEntity != null) {
                            areaEntity.workItemEntities.add(olxjWorkItemEntity);
                        }
                        return olxjWorkItemEntity;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OLXJWorkItemEntity>() {
                    @Override
                    public void accept(OLXJWorkItemEntity olxjWorkItemEntity) throws Exception {

                    }
                }, throwable -> {

                }, () -> {
                    for (int i = mAreaEntities.size() - 1; i >= 0; i--) {
                        if (mAreaEntities.get(i).workItemEntities.size() == 0) {
                            mAreaEntities.remove(i);
                        } else {
//                                Collections.sort(mAreaEntities.get(i).workItemEntities);
                        }
                    }
                    if (mOnSuccessListener != null) {
                        mOnSuccessListener.onSuccess(true);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void initArea(List<OLXJAreaEntity> areaEntities) {

        Flowable.fromIterable(areaEntities)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<OLXJAreaEntity>() {
                    @Override
                    public void accept(OLXJAreaEntity areaEntity) throws Exception {
                        mAreaEntityMap.put(areaEntity.id, areaEntity);
                    }
                });
    }

    @Override
    public void getWorkItemListFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
        if (mOnSuccessListener != null) {
            mOnSuccessListener.onSuccess(false);
        }
    }

    @Override
    public void getWorkItemListRefSuccess(CommonBAPListEntity entity) {

    }

    @Override
    public void getWorkItemListRefFailed(String errorMsg) {

    }

    @Override
    public void getOJXJAreaListSuccess(CommonBAPListEntity entity) {

        if (entity.result != null) {
            mAreaEntities.addAll(entity.result);
        }

        if (entity.hasNext) {
            cuttentAreaPage++;
            getAreaData();
        } else {
            initArea(mAreaEntities);
            getWorkData();
            getAbnormalInspectTaskPart();
        }
    }

    @Override
    public void getOJXJAreaListFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void getAbnormalInspectTaskPartSuccess(CommonListEntity entity) {
        updateAreaAbnormal(entity.result);
    }

    @Override
    public void getAbnormalInspectTaskPartFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(!TextUtils.isEmpty(errorMsg) ? errorMsg : "获取巡检隐患信息失败!"));
    }

    @SuppressLint("CheckResult")
    private void updateAreaAbnormal(List<AbnormalEntity> abnormalEntities) {
        Flowable.fromIterable(abnormalEntities)
                .subscribe(new Consumer<AbnormalEntity>() {
                    @Override
                    public void accept(AbnormalEntity abnormalEntity) throws Exception {
                        if (mAreaEntityMap.containsKey(abnormalEntity.workCode)) {
                            OLXJAreaEntity olxjAreaEntity = mAreaEntityMap.get(abnormalEntity.workCode);
                            olxjAreaEntity.oldfaultMsg = abnormalEntity.abnormalInfo;
                        }
                    }
                });
    }

    public List<OLXJAreaEntity> getAreaEntities() {
        return mAreaEntities;
    }

    public List<OLXJWorkItemEntity> getOLXJWorkItemEntities() {
        return mOLXJWorkItemEntities;
    }
}
