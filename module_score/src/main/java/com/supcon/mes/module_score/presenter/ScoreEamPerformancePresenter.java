package com.supcon.mes.module_score.presenter;

import android.annotation.SuppressLint;
import android.support.v4.app.CoreComponentFactory;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.constant.ScoreConstant;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceListEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceListEntity;
import com.supcon.mes.module_score.model.contract.ScoreEamPerformanceContract;
import com.supcon.mes.module_score.model.network.ScoreHttpClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ScoreEamPerformancePresenter extends ScoreEamPerformanceContract.Presenter {

//    private int position = 0;
//    private String category = "";//评分标题
    private int index = 0; // 标题
    private ScoreEamPerformanceEntity scorePerformanceTitleEntity;

    @Override
    public void getScorPerformance(Long eamId, Long scoreId) {
        if (eamId == -1 && scoreId == -1) {
            getView().getScorPerformanceSuccess(new ScoreEamPerformanceListEntity());
        } else if (eamId != -1 && scoreId == -1) {
            // 加载设备评分模板
            getScoreTemplate(eamId);

        } else if (eamId == -1) {
            // 加载pt列表
            getPtScoreTemplate(scoreId);
        }

//        List<String> urls = new ArrayList<>();
//        //设备运转率
//        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559647635248.action");
//        //高质量运行
//        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559291491380.action");
//        //安全防护
//        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559560003995.action");
//        //外观标识
//        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559619724464.action");
//        //设备卫生
//        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559631064719.action");
//        //档案管理
//        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559636766020.action");
//
//        LinkedHashMap<String, ScoreEamPerformanceEntity> scoreMap = new LinkedHashMap();
//        mCompositeSubscription.add(Flowable.fromIterable(urls)
//                .concatMap(new Function<String, Flowable<ScoreEamPerformanceListEntity>>() {
//                    @Override
//                    public Flowable<ScoreEamPerformanceListEntity> apply(String url) throws Exception {
//                        return ScoreHttpClient.getScore(url, scoreId);
//                    }
//                })
//                .onErrorReturn(throwable -> {
//                    ScoreEamPerformanceListEntity scoreEamPerformanceListEntity = new ScoreEamPerformanceListEntity();
//                    scoreEamPerformanceListEntity.errMsg = throwable.toString();
//                    return scoreEamPerformanceListEntity;
//                })
//                .concatMap((Function<ScoreEamPerformanceListEntity, Flowable<ScoreEamPerformanceEntity>>) scoreEamPerformanceListEntity -> {
//                    if (scoreEamPerformanceListEntity.result != null) {
//                        return Flowable.fromIterable(scoreEamPerformanceListEntity.result);
//                    }
//                    return Flowable.fromIterable(new ArrayList<>());
//                })
//                .subscribe(scorePerformanceEntity -> {
//                    if (!TextUtils.isEmpty(scorePerformanceEntity.scoreStandard) && !scorePerformanceEntity.scoreStandard.equals(category)) {
//                        position = 0;
//                        category = scorePerformanceEntity.scoreStandard;
//                        scorePerformanceTitleEntity = new ScoreEamPerformanceEntity();
//                        scorePerformanceTitleEntity.scoreStandard = scorePerformanceEntity.scoreStandard;
//                        scorePerformanceTitleEntity.defaultTotalScore = scorePerformanceEntity.defaultTotalScore;
//                        scorePerformanceTitleEntity.viewType = ListType.TITLE.value();
//                        scoreMap.put(scorePerformanceTitleEntity.scoreStandard, scorePerformanceTitleEntity);
//                    }
//
//                    ScoreEamPerformanceEntity scorePerformanceOldTitleEntity;
//                    if (scoreMap.containsKey(scorePerformanceEntity.itemDetail)) {
//                        scorePerformanceOldTitleEntity = scoreMap.get(scorePerformanceEntity.itemDetail);
//                    } else {
//                        position++;
//                        scorePerformanceOldTitleEntity = scorePerformanceEntity;
//                    }
//
//                    if (TextUtils.isEmpty(scorePerformanceOldTitleEntity.isItemValue)) {
//                        scorePerformanceOldTitleEntity.marks.put(scorePerformanceEntity.scoreItem + "(" + Util.big0(scorePerformanceEntity.score) + ")"
//                                , scorePerformanceEntity.score);
//                        scorePerformanceOldTitleEntity.marksState.put(scorePerformanceEntity.scoreItem + "(" +  Util.big0(scorePerformanceEntity.score) + ")"
//                                , scorePerformanceEntity.result);
//                        scorePerformanceOldTitleEntity.scorePerformanceEntityMap.put(scorePerformanceEntity.scoreItem + "(" +  Util.big0(scorePerformanceEntity.score) + ")"
//                                , scorePerformanceEntity);
//                    }
//                    scorePerformanceOldTitleEntity.index = position;
//
//                    if (scorePerformanceOldTitleEntity.resultValue != 0) {
//                        scorePerformanceOldTitleEntity.viewType = ListType.HEADER.value();
//                    } else {
//                        scorePerformanceOldTitleEntity.viewType = ListType.CONTENT.value();
//                    }
//                    if (scorePerformanceTitleEntity != null) {
//                        scorePerformanceTitleEntity.scorePerformanceEntities.add(scorePerformanceOldTitleEntity);
//                        scorePerformanceOldTitleEntity.scoreEamPerformanceEntity = scorePerformanceTitleEntity;
//                    }
//                    scoreMap.put(scorePerformanceOldTitleEntity.itemDetail, scorePerformanceOldTitleEntity);
//                }, throwable -> {
//                    getView().getScorPerformanceFailed(throwable.toString());
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        category = "";
//                        getView().getScorPerformanceSuccess(new ArrayList<>(scoreMap.values()));
//                    }
//                }));
    }

    @SuppressLint("CheckResult")
    private void getPtScoreTemplate(Long scoreId) {
        String url = "/BEAM/scorePerformance/scoreHead/data-dg1559291491380.action?datagridCode=BEAM_1.0.0_scorePerformance_beamPerformanceEditdg1559291491380&rt=json&dgPage.pageSize=65536&dgPage.maxPageSize=500";
        ScoreHttpClient.getScore(url, scoreId)
                .onErrorReturn(throwable -> {
                    ScoreEamPerformanceListEntity scoreEamPerformanceListEntity = new ScoreEamPerformanceListEntity();
                    scoreEamPerformanceListEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    return scoreEamPerformanceListEntity;
                })
                .subscribe(scoreEamPerformanceListEntity -> {
                    if (scoreEamPerformanceListEntity.result != null && scoreEamPerformanceListEntity.result.size() > 0) {
                        scoreSuccessCallBack(scoreEamPerformanceListEntity.result);
//                            getView().getScorPerformanceSuccess();
                    } else {
                        getView().getScorPerformanceFailed(scoreEamPerformanceListEntity.errMsg);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getScoreTemplate(Long eamId) {
        ScoreHttpClient.getScoreTemplate(eamId, "BEAM_065/01")
                .onErrorReturn(throwable -> {
                    CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                    commonBAPListEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    return commonBAPListEntity;
                })
                .subscribe(commonBAPListEntity -> {
                    if (commonBAPListEntity.result != null && commonBAPListEntity.result.size() > 0) {
                        scoreSuccessCallBack(GsonUtil.jsonToList(GsonUtil.gsonString((Object)commonBAPListEntity.result),ScoreEamPerformanceEntity.class));
//                            getView().getScorPerformanceSuccess();
                    } else {
                        getView().getScorPerformanceFailed(commonBAPListEntity.errMsg);
                    }
                });
    }

    private void scoreSuccessCallBack(List<ScoreEamPerformanceEntity> result) {
        ScoreEamPerformanceListEntity performanceListEntity = new ScoreEamPerformanceListEntity();
        performanceListEntity.result = new ArrayList<>();
        Set<String> categorySet = new HashSet<>();  // 标题集合
        ScoreEamPerformanceEntity scorePerformanceMutilTitleEntity; // 多选title

        for (ScoreEamPerformanceEntity scoreEamPerformanceEntity : result) {
            if (!TextUtils.isEmpty(scoreEamPerformanceEntity.category) && !categorySet.contains(scoreEamPerformanceEntity.category)) {
                index = 0;
//                                category = scoreStaffPerformanceEntity.project;
                categorySet.add(scoreEamPerformanceEntity.category);
                scorePerformanceTitleEntity = new ScoreEamPerformanceEntity();
                scorePerformanceTitleEntity.category = scoreEamPerformanceEntity.category;
//                                scorePerformanceTitleEntity.categoryScore = scoreStaffPerformanceEntity.categoryScore;
                scorePerformanceTitleEntity.categoryScore = scoreEamPerformanceEntity.categoryScore;
                scorePerformanceTitleEntity.scoringId = scoreEamPerformanceEntity.scoringId;
                scorePerformanceTitleEntity.viewType = ListType.TITLE.value();
//                                scoreMap.put(scorePerformanceTitleEntity.project, scorePerformanceTitleEntity);

                performanceListEntity.result.add(scorePerformanceTitleEntity);
            }

            // 多选 title
            if (scoreEamPerformanceEntity.defaultValueType != null && ScoreConstant.ValueType.T4.equals(scoreEamPerformanceEntity.defaultValueType.id) &&
                    !categorySet.contains(scoreEamPerformanceEntity.item) ){
                categorySet.add(scoreEamPerformanceEntity.item);
                scorePerformanceMutilTitleEntity = new ScoreEamPerformanceEntity();
                scorePerformanceMutilTitleEntity.item = scoreEamPerformanceEntity.item;
                scorePerformanceMutilTitleEntity.category = scoreEamPerformanceEntity.category;
                scorePerformanceMutilTitleEntity.categoryScore = scoreEamPerformanceEntity.categoryScore;
                scorePerformanceMutilTitleEntity.scoringId = scoreEamPerformanceEntity.scoringId;
                scorePerformanceMutilTitleEntity.viewType = ListType.HEADER.value();
                scorePerformanceMutilTitleEntity.index = ++index;

                performanceListEntity.result.add(scorePerformanceMutilTitleEntity);
            }

            if (scoreEamPerformanceEntity.defaultValueType != null && !ScoreConstant.ValueType.T4.equals(scoreEamPerformanceEntity.defaultValueType.id)){
                scoreEamPerformanceEntity.index = ++index;
            }
            scoreEamPerformanceEntity.scoreEamPerformanceEntity = scorePerformanceTitleEntity; // 赋值父标题项
            scoreEamPerformanceEntity.viewType = ListType.CONTENT.value();
            performanceListEntity.result.add(scoreEamPerformanceEntity);
        }

        getView().getScorPerformanceSuccess(performanceListEntity);
    }


}
