package com.supcon.mes.module_score.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_score.constant.ScoreConstant;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceListEntity;
import com.supcon.mes.module_score.model.contract.ScoreInspectorStaffPerformanceContract;
import com.supcon.mes.module_score.model.contract.ScoreStaffPerformanceContract;
import com.supcon.mes.module_score.model.network.ScoreHttpClient;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ScoreStaffPerformancePresenter extends ScoreStaffPerformanceContract.Presenter {
    private int index = 0; // 标题
    private ScoreStaffPerformanceEntity scorePerformanceTitleEntity;

    @Override
    public void getStaffScore(Long staffId, Long tableId) {
        if (staffId == null) {
            getView().getStaffScoreSuccess(new ScoreStaffPerformanceListEntity());
            return;
        }

        if (tableId == -1) {
            // 加载人员评分模板
            getScoreTemplate(staffId);
        } else {
            // 加载pt列表
            getPtScoreTemplate(tableId);
        }

//        mCompositeSubscription.add(
//                flowable.onErrorReturn(throwable -> {
//                    ScoreStaffPerformanceListEntity scoreStaffPerformanceListEntity = new ScoreStaffPerformanceListEntity();
//                    scoreStaffPerformanceListEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
//                    return scoreStaffPerformanceListEntity;
//                })
//                        .subscribe(scoreStaffPerformanceListEntity -> {
//                            if (scoreStaffPerformanceListEntity.result != null && scoreStaffPerformanceListEntity.result.size() > 0) {
//                                ScoreStaffPerformanceListEntity performanceListEntity = new ScoreStaffPerformanceListEntity();
//                                performanceListEntity.result = new ArrayList<>();
//                                Set<String> categorySet = new HashSet<>();  // 标题集合
//
//                                for (ScoreStaffPerformanceEntity scoreStaffPerformanceEntity : scoreStaffPerformanceListEntity.result) {
//                                    if (!TextUtils.isEmpty(scoreStaffPerformanceEntity.category) && !categorySet.contains(scoreStaffPerformanceEntity.category)) {
//                                        index = 0;
////                                category = scoreStaffPerformanceEntity.project;
//                                        categorySet.add(scoreStaffPerformanceEntity.category);
//                                        scorePerformanceTitleEntity = new ScoreStaffPerformanceEntity();
//                                        scorePerformanceTitleEntity.category = scoreStaffPerformanceEntity.category;
////                                scorePerformanceTitleEntity.categoryScore = scoreStaffPerformanceEntity.categoryScore;
//                                        scorePerformanceTitleEntity.fraction = scoreStaffPerformanceEntity.fraction;
//                                        scorePerformanceTitleEntity.scoringId = scoreStaffPerformanceEntity.scoringId;
//                                        scorePerformanceTitleEntity.viewType = ListType.TITLE.value();
////                                scoreMap.put(scorePerformanceTitleEntity.project, scorePerformanceTitleEntity);
//
//                                        performanceListEntity.result.add(scorePerformanceTitleEntity);
//                                    }
////                            position++;
//                                    scoreStaffPerformanceEntity.index = ++index;
//                                    scoreStaffPerformanceEntity.scoreEamPerformanceEntity = scorePerformanceTitleEntity; // 赋值父标题项
//                                    scoreStaffPerformanceEntity.viewType = ListType.CONTENT.value();
//                                    performanceListEntity.result.add(scoreStaffPerformanceEntity);
////                            if (scorePerformanceTitleEntity != null) {
////                                scorePerformanceTitleEntity.scorePerformanceEntities.add(scoreStaffPerformanceEntity);
////                                scoreStaffPerformanceEntity.scoreEamPerformanceEntity = scorePerformanceTitleEntity;
////                            }
////                            scoreMap.put(scoreStaffPerformanceEntity.item, scoreStaffPerformanceEntity);
//                                }
//
//                                getView().getStaffScoreSuccess(performanceListEntity);
//                            } else {
//                                getView().getStaffScoreFailed(scoreStaffPerformanceListEntity.errMsg);
//                            }
//                        })
//        );
    }

    @SuppressLint("CheckResult")
    private void getPtScoreTemplate(Long tableId) {
        String url = "/BEAM/patrolWorkerScore/workerScoreHead/data-dg1560224145331.action?datagridCode=BEAM_1.0.0_patrolWorkerScore_patrolScoreEditdg1560224145331&rt=json&dgPage.pageSize=65536&dgPage.maxPageSize=500";
        mCompositeSubscription.add(
                ScoreHttpClient.getInspectorStaffScore(url, tableId)
                        .onErrorReturn(throwable -> {
                            ScoreStaffPerformanceListEntity scoreStaffPerformanceListEntity = new ScoreStaffPerformanceListEntity();
                            scoreStaffPerformanceListEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return scoreStaffPerformanceListEntity;
                        })
                        .subscribe(new Consumer<ScoreStaffPerformanceListEntity>() {
                            @Override
                            public void accept(ScoreStaffPerformanceListEntity scoreStaffPerformanceListEntity) throws Exception {
                                if (scoreStaffPerformanceListEntity.result != null && scoreStaffPerformanceListEntity.result.size() > 0) {

                                    scoreSuccessCallBack(scoreStaffPerformanceListEntity.result);

//                            getView().getStaffScoreSuccess();
                                } else {
                                    getView().getStaffScoreFailed(scoreStaffPerformanceListEntity.errMsg);
                                }
                            }
                        })
        );

    }

    @SuppressLint("CheckResult")
    private void getScoreTemplate(Long staffId) {
        mCompositeSubscription.add(
                ScoreHttpClient.getScoreTemplate(staffId, "BEAM_065/02")
                        .onErrorReturn(throwable -> {
                            CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                            commonBAPListEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return commonBAPListEntity;
                        })
                        .subscribe(commonBAPListEntity -> {
                            if (commonBAPListEntity.result != null && commonBAPListEntity.result.size() > 0) {
                                scoreSuccessCallBack(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result),ScoreStaffPerformanceEntity.class));
//                            getView().getStaffScoreSuccess();
                            } else {
                                getView().getStaffScoreFailed(commonBAPListEntity.errMsg);
                            }
                        })
        );
    }

    private void scoreSuccessCallBack(List<ScoreStaffPerformanceEntity> result) {
        ScoreStaffPerformanceListEntity performanceListEntity = new ScoreStaffPerformanceListEntity();
        performanceListEntity.result = new ArrayList<>();
        Set<String> categorySet = new HashSet<>();  // 标题集合
        ScoreStaffPerformanceEntity scorePerformanceMultiTitleEntity; // 多选title

        for (ScoreStaffPerformanceEntity scoreStaffPerformanceEntity : result) {
            if (!TextUtils.isEmpty(scoreStaffPerformanceEntity.category) && !categorySet.contains(scoreStaffPerformanceEntity.category)) {
                index = 0;
//                                category = scoreStaffPerformanceEntity.project;
                categorySet.add(scoreStaffPerformanceEntity.category);
                scorePerformanceTitleEntity = new ScoreStaffPerformanceEntity();
                scorePerformanceTitleEntity.category = scoreStaffPerformanceEntity.category;
//                                scorePerformanceTitleEntity.categoryScore = scoreStaffPerformanceEntity.categoryScore;
                scorePerformanceTitleEntity.fraction = scoreStaffPerformanceEntity.fraction;
                scorePerformanceTitleEntity.scoringId = scoreStaffPerformanceEntity.scoringId;
                scorePerformanceTitleEntity.viewType = ListType.TITLE.value();
//                                scoreMap.put(scorePerformanceTitleEntity.project, scorePerformanceTitleEntity);

                performanceListEntity.result.add(scorePerformanceTitleEntity);
            }

            // 多选 title
            if (scoreStaffPerformanceEntity.defaultValueType != null && ScoreConstant.ValueType.T4.equals(scoreStaffPerformanceEntity.defaultValueType.id) &&
                    !categorySet.contains(scoreStaffPerformanceEntity.item) ){
                categorySet.add(scoreStaffPerformanceEntity.item);
                scorePerformanceMultiTitleEntity = new ScoreStaffPerformanceEntity();
                scorePerformanceMultiTitleEntity.item = scoreStaffPerformanceEntity.item;
                scorePerformanceMultiTitleEntity.category = scoreStaffPerformanceEntity.category;
                scorePerformanceMultiTitleEntity.fraction = scoreStaffPerformanceEntity.fraction;
                scorePerformanceMultiTitleEntity.scoringId = scoreStaffPerformanceEntity.scoringId;
                scorePerformanceMultiTitleEntity.viewType = ListType.HEADER.value();
                scorePerformanceMultiTitleEntity.index = ++index;

                performanceListEntity.result.add(scorePerformanceMultiTitleEntity);
            }

            if (scoreStaffPerformanceEntity.defaultValueType != null && !ScoreConstant.ValueType.T4.equals(scoreStaffPerformanceEntity.defaultValueType.id)){
                scoreStaffPerformanceEntity.index = ++index;
            }
            scoreStaffPerformanceEntity.scoreEamPerformanceEntity = scorePerformanceTitleEntity; // 赋值父标题项
            scoreStaffPerformanceEntity.viewType = ListType.CONTENT.value();
            performanceListEntity.result.add(scoreStaffPerformanceEntity);
        }

        getView().getStaffScoreSuccess(performanceListEntity);
    }


    @Override
    public void getDutyEam(Long staffId, String scoreType) {

    }
}
