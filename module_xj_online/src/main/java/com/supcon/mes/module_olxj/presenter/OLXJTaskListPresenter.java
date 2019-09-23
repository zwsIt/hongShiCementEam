package com.supcon.mes.module_olxj.presenter;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

public class OLXJTaskListPresenter extends OLXJTaskContract.Presenter {

    @Override
    public void getOJXJTaskList(Map<String, Object> queryParam) {

        queryParam.put(Constant.BAPQuery.LINK_STATE, "LinkState/01");//LinkState/01 未下发 LinkState/02 已下发
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createJoinFastQueryCond(queryParam);
        fastQueryCondEntity.modelAlias = "potrolTaskWF";

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", 1);
        pageQueryParams.put("page.pageSize", 100);
        pageQueryParams.put("page.maxPageSize", 500);
        LogUtil.d("fastQueryCondEntity:" + fastQueryCondEntity);

        mCompositeSubscription.add(
                OLXJClient.queryPotrolTaskList(pageQueryParams, fastQueryCondEntity)
                        .onErrorReturn(new Function<Throwable, CommonBAPListEntity<OLXJTaskEntity>>() {
                            @Override
                            public CommonBAPListEntity<OLXJTaskEntity> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                                commonBAPListEntity.success = false;
                                commonBAPListEntity.errMsg = throwable.toString();
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<OLXJTaskEntity>>() {
                            @Override
                            public void accept(CommonBAPListEntity<OLXJTaskEntity> olxjTaskEntityCommonBAPListEntity) throws Exception {
                                if (olxjTaskEntityCommonBAPListEntity.result != null) {
                                    List<OLXJTaskEntity> taskEntities = filterTask(olxjTaskEntityCommonBAPListEntity.result);
                                    Objects.requireNonNull(getView()).getOJXJTaskListSuccess(taskEntities);
                                } else {
                                    Objects.requireNonNull(getView()).getOJXJTaskListFailed(olxjTaskEntityCommonBAPListEntity.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Objects.requireNonNull(getView()).getOJXJTaskListFailed(throwable.toString());
                            }
                        }));
    }

    private List<OLXJTaskEntity> filterTask(List<OLXJTaskEntity> result) {
        List<OLXJTaskEntity> taskEntities = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        for (OLXJTaskEntity taskEntity : result) {
            if (taskEntity.starTime == null || taskEntity.endTime == null) {
                continue;
            }
            if (taskEntity.starTime <= currentTime && taskEntity.endTime >= currentTime) {
                taskEntities.add(taskEntity);
            }
        }
        return taskEntities;
    }

    @Override
    public void getOJXJLastTaskList(Map<String, Object> queryParam) {
        queryParam.put(Constant.BAPQuery.LINK_STATE, "LinkState/02");//LinkState/01 未下发 LinkState/02 已下发
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createJoinFastQueryCond(queryParam);
        fastQueryCondEntity.modelAlias = "potrolTaskWF";

        Map<String, Object> paramsName = new HashMap<>();
        paramsName.put(Constant.BAPQuery.NAME, EamApplication.getAccountInfo().staffName);
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(paramsName, "base_staff,ID,MOBILEEAM_POTROL_TASKWFS,RESSTAFFID");
        fastQueryCondEntity.subconds.add(joinSubcondEntity);


        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", 1);
        pageQueryParams.put("page.pageSize", 20);
        pageQueryParams.put("page.maxPageSize", 500);
        LogUtil.d("fastQueryCondEntity:" + fastQueryCondEntity);

        mCompositeSubscription.add(
                OLXJClient.queryPotrolTaskList(pageQueryParams, fastQueryCondEntity)
                        .onErrorReturn(new Function<Throwable, CommonBAPListEntity<OLXJTaskEntity>>() {
                            @Override
                            public CommonBAPListEntity<OLXJTaskEntity> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                                commonBAPListEntity.success = false;
                                commonBAPListEntity.errMsg = throwable.toString();
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<OLXJTaskEntity>>() {
                            @Override
                            public void accept(CommonBAPListEntity<OLXJTaskEntity> olxjTaskEntityCommonBAPListEntity) throws Exception {
                                if (olxjTaskEntityCommonBAPListEntity.result != null) {
                                    List<OLXJTaskEntity> taskEntities = new ArrayList<>();
                                    if (olxjTaskEntityCommonBAPListEntity.result.size() != 0) {
                                        taskEntities.add(olxjTaskEntityCommonBAPListEntity.result.get(0));
                                    }
                                    Objects.requireNonNull(getView()).getOJXJLastTaskListSuccess(taskEntities);
                                } else {
                                    Objects.requireNonNull(getView()).getOJXJLastTaskListFailed(olxjTaskEntityCommonBAPListEntity.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Objects.requireNonNull(getView()).getOJXJLastTaskListFailed(throwable.toString());
                            }
                        }));
    }
}
