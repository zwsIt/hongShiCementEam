package com.supcon.mes.module_olxj.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.PageParamUtil;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskRecordsContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TYPE_JOIN;

/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

public class OLXJTaskRecordsPresenter extends OLXJTaskRecordsContract.Presenter {

    @Override
    public void getOJXJTaskList(int pageIndex, Map<String, Object> queryParam) {

//        queryParam.put(Constant.BAPQuery.LINK_STATE, "LinkState/01");//LinkState/01 未下发 LinkState/02 已下发

        JoinSubcondEntity joinSubcondEntity = null;
        if (queryParam.containsKey(Constant.BAPQuery.NAME)) { // 负责人
            Map<String, Object> staffParam = new HashMap<>();
            staffParam.put(Constant.BAPQuery.NAME, queryParam.get(Constant.BAPQuery.NAME));
            joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(staffParam, "base_staff,ID,MOBILEEAM_POTROL_TASKWFS,RESSTAFFID");
            queryParam.remove(Constant.BAPQuery.NAME);
        }

        if (queryParam.containsKey(Constant.BAPQuery.ID)) { // 部门
            JoinSubcondEntity joinSubcondEntityMainPosition = new JoinSubcondEntity();
            joinSubcondEntityMainPosition.type = TYPE_JOIN;
            joinSubcondEntityMainPosition.joinInfo = "BASE_POSITION,ID,base_staff,MAIN_POSITION_ID";
            joinSubcondEntityMainPosition.subconds = new LinkedList<>();

            Map<String, Object> departmentParam = new HashMap<>();
            departmentParam.put(Constant.BAPQuery.ID, queryParam.get(Constant.BAPQuery.ID));
            joinSubcondEntityMainPosition.subconds.add(BAPQueryParamsHelper.createJoinSubcondEntity(departmentParam, "BASE_DEPARTMENT,ID,BASE_POSITION,DEPARTMENT_ID"));
            queryParam.remove(Constant.BAPQuery.ID);
            if (joinSubcondEntity != null)
                joinSubcondEntity.subconds.add(joinSubcondEntityMainPosition);
        }

        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryParam);
        if (joinSubcondEntity != null) {
            fastQueryCondEntity.subconds.add(joinSubcondEntity);
        }
        fastQueryCondEntity.modelAlias = "potrolTaskWF";

        Map<String, Object> pageQueryParams = PageParamUtil.pageQueryParam(pageIndex, 20);

        mCompositeSubscription.add(
                OLXJClient.queryPotrolTaskRecordsList(pageQueryParams, fastQueryCondEntity)
                        .onErrorReturn(throwable -> {
                            CommonBAPListEntity<OLXJTaskEntity> commonBAPListEntity = new CommonBAPListEntity<>();
                            commonBAPListEntity.success = false;
                            commonBAPListEntity.errMsg = throwable.toString();
                            return commonBAPListEntity;
                        })
                        .subscribe(olxjTaskEntityCommonBAPListEntity -> {
                            if (olxjTaskEntityCommonBAPListEntity.result != null) {
//                                    List<OLXJTaskEntity> taskEntities = filterTask(olxjTaskEntityCommonBAPListEntity.result);
                                Objects.requireNonNull(getView()).getOJXJTaskListSuccess(olxjTaskEntityCommonBAPListEntity);
                            } else {
                                Objects.requireNonNull(getView()).getOJXJTaskListFailed(olxjTaskEntityCommonBAPListEntity.errMsg);
                            }
                        }));
    }

    @Override
    public void getOJXJAreaList(Long taskIDs) {
        Map<String, Object> pageQueryParams = PageParamUtil.pageQueryParam(1, 100);
        mCompositeSubscription.add(
                OLXJClient.queryPotrolTaskRecordsAreaList(taskIDs, pageQueryParams)
                        .onErrorReturn(new Function<Throwable, CommonBAPListEntity<OLXJWorkItemEntity>>() {
                            @Override
                            public CommonBAPListEntity<OLXJWorkItemEntity> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity<OLXJWorkItemEntity> commonBAPListEntity = new CommonBAPListEntity<>();
                                commonBAPListEntity.success = false;
                                commonBAPListEntity.errMsg = throwable.toString();
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<OLXJWorkItemEntity>>() {
                            @Override
                            public void accept(CommonBAPListEntity<OLXJWorkItemEntity> olxjAreaRecordsEntityCommonBAPListEntity) throws Exception {
                                if (olxjAreaRecordsEntityCommonBAPListEntity.result != null) {
                                    Objects.requireNonNull(OLXJTaskRecordsPresenter.this.getView()).getOJXJAreaListSuccess(olxjAreaRecordsEntityCommonBAPListEntity);
                                } else {
                                    Objects.requireNonNull(OLXJTaskRecordsPresenter.this.getView()).getOJXJAreaListFailed(olxjAreaRecordsEntityCommonBAPListEntity.errMsg);
                                }
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
}
