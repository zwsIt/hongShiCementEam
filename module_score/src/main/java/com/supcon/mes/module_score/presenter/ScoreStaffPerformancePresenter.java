package com.supcon.mes.module_score.presenter;

import android.text.TextUtils;

import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_score.constant.ScoreConstant;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
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
    private String category = "";//评分标题
    private ScoreStaffPerformanceEntity scorePerformanceTitleEntity;

    @Override
    public void getStaffScore(Long staffId, Long tableId) {
        Flowable<ScoreStaffPerformanceListEntity> flowable;
        if (staffId == null) {
            getView().getStaffScoreSuccess(new ScoreStaffPerformanceListEntity());
            return;
        }

        if (tableId == -1){
            flowable = ScoreHttpClient.getScoreTemplate(staffId);
        }else {
            String url = "/BEAM/patrolWorkerScore/workerScoreHead/data-dg1560224145331.action?datagridCode=BEAM_1.0.0_patrolWorkerScore_patrolScoreEditdg1560224145331&rt=json";
            flowable = ScoreHttpClient.getInspectorStaffScore(url,tableId);
        }

        mCompositeSubscription.add(
                flowable.onErrorReturn(throwable -> {
                    ScoreStaffPerformanceListEntity scoreStaffPerformanceListEntity = new ScoreStaffPerformanceListEntity();
                    scoreStaffPerformanceListEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    return scoreStaffPerformanceListEntity;
                })
                .subscribe(scoreStaffPerformanceListEntity -> {
                    if (scoreStaffPerformanceListEntity.result != null){
                        ScoreStaffPerformanceListEntity performanceListEntity = new ScoreStaffPerformanceListEntity();
                        performanceListEntity.result = new ArrayList<>();
                        Set<String> categorySet = new HashSet<>();  // 标题集合

                        for (ScoreStaffPerformanceEntity scoreStaffPerformanceEntity : scoreStaffPerformanceListEntity.result){
                            if (!TextUtils.isEmpty(scoreStaffPerformanceEntity.category) && !categorySet.contains(scoreStaffPerformanceEntity.category)) {
                                index = 0;
//                                category = scoreStaffPerformanceEntity.project;
                                categorySet.add(scoreStaffPerformanceEntity.category);
                                scorePerformanceTitleEntity = new ScoreStaffPerformanceEntity();
                                scorePerformanceTitleEntity.category = scoreStaffPerformanceEntity.category;
//                                scorePerformanceTitleEntity.categoryScore = scoreStaffPerformanceEntity.categoryScore;
                                scorePerformanceTitleEntity.fraction = scoreStaffPerformanceEntity.fraction;
                                scorePerformanceTitleEntity.viewType = ListType.TITLE.value();
//                                scoreMap.put(scorePerformanceTitleEntity.project, scorePerformanceTitleEntity);

                                performanceListEntity.result.add(scorePerformanceTitleEntity);
                            }
//                            position++;
                            scoreStaffPerformanceEntity.index = ++index;
                            scoreStaffPerformanceEntity.scoreEamPerformanceEntity = scorePerformanceTitleEntity; // 赋值父标题项
                            scoreStaffPerformanceEntity.viewType = ListType.CONTENT.value();
                            performanceListEntity.result.add(scoreStaffPerformanceEntity);
//                            if (scorePerformanceTitleEntity != null) {
//                                scorePerformanceTitleEntity.scorePerformanceEntities.add(scoreStaffPerformanceEntity);
//                                scoreStaffPerformanceEntity.scoreEamPerformanceEntity = scorePerformanceTitleEntity;
//                            }
//                            scoreMap.put(scoreStaffPerformanceEntity.item, scoreStaffPerformanceEntity);
                        }

                        getView().getStaffScoreSuccess(performanceListEntity);
                    }else {
                        getView().getStaffScoreFailed(scoreStaffPerformanceListEntity.errMsg);
                    }
                })
        );
    }

    @Override
    public void getDutyEam(Long staffId, String scoreType) {

    }
}
