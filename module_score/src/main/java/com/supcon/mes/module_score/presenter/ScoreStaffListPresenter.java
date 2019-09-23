package com.supcon.mes.module_score.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_score.model.bean.ScoreEamListEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.model.contract.ScoreEamListContract;
import com.supcon.mes.module_score.model.contract.ScoreStaffListContract;
import com.supcon.mes.module_score.model.network.ScoreHttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 人员评分列表
 */
public class ScoreStaffListPresenter extends ScoreStaffListContract.Presenter {
    @Override
    public void patrolScore(String url, Map<String, Object> param, int page) {
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        if (param.containsKey(Constant.BAPQuery.SCORE_DATA_START) || param.containsKey(Constant.BAPQuery.SCORE_DATA_STOP)) {
            Map<String, Object> timeParam = new HashMap<>();
            timeParam.put(Constant.BAPQuery.SCORE_DATA_START, param.get(Constant.BAPQuery.SCORE_DATA_START));
            timeParam.put(Constant.BAPQuery.SCORE_DATA_STOP, param.get(Constant.BAPQuery.SCORE_DATA_STOP));
            List<BaseSubcondEntity> subcondEntities = BAPQueryParamsHelper.crateSubcondEntity(timeParam);
            fastQuery.subconds.addAll(subcondEntities);
        }
        if (param.containsKey(Constant.BAPQuery.NAME)) {
            Map<String, Object> eamParam = new HashMap<>();
            eamParam.put(Constant.BAPQuery.NAME, param.get(Constant.BAPQuery.NAME));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(eamParam, "base_staff,ID,BEAM_WORKER_SCORE_HEADS,PATROL_WORKER");
            fastQuery.subconds.add(joinSubcondEntity);
        }
        fastQuery.modelAlias = "workerScoreHead";

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", 500);
        pageQueryParams.put("page.maxPageSize", 500);
        mCompositeSubscription.add(ScoreHttpClient.patrolScore(url, fastQuery, pageQueryParams)
                .onErrorReturn(throwable -> {
                    CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                    commonBAPListEntity.errMsg = throwable.toString();
                    return commonBAPListEntity;
                }).subscribe(new Consumer<CommonBAPListEntity<ScoreStaffEntity>>() {
                    @Override
                    public void accept(CommonBAPListEntity<ScoreStaffEntity> scoreStaffEntity) throws Exception {
                        if (TextUtils.isEmpty(scoreStaffEntity.errMsg)) {
                            getView().patrolScoreSuccess(scoreStaffEntity);
                        } else {
                            getView().patrolScoreFailed(scoreStaffEntity.errMsg);
                        }
                    }
                }));
    }
}
