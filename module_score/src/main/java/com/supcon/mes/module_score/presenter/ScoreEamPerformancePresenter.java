package com.supcon.mes.module_score.presenter;

import android.text.TextUtils;

import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceListEntity;
import com.supcon.mes.module_score.model.contract.ScoreEamPerformanceContract;
import com.supcon.mes.module_score.model.network.ScoreHttpClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class ScoreEamPerformancePresenter extends ScoreEamPerformanceContract.Presenter {

    private int position = 0;
    private String category = "";//评分标题
    private ScoreEamPerformanceEntity scorePerformanceTitleEntity;

    @Override
    public void getScorPerformance(int scoreId) {
        List<String> urls = new ArrayList<>();
        //设备运转率
        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559647635248.action");
        //高质量运行
        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559291491380.action");
        //安全防护
        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559560003995.action");
        //外观标识
        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559619724464.action");
        //设备卫生
        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559631064719.action");
        //档案管理
        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559636766020.action");
        LinkedHashMap<String, ScoreEamPerformanceEntity> scoreMap = new LinkedHashMap();
        mCompositeSubscription.add(Flowable.fromIterable(urls)
                .concatMap((Function<String, Flowable<ScoreEamPerformanceListEntity>>) url -> ScoreHttpClient.getScore(url, scoreId))
                .onErrorReturn(throwable -> {
                    ScoreEamPerformanceListEntity scoreEamPerformanceListEntity = new ScoreEamPerformanceListEntity();
                    scoreEamPerformanceListEntity.errMsg = throwable.toString();
                    return scoreEamPerformanceListEntity;
                })
                .concatMap((Function<ScoreEamPerformanceListEntity, Flowable<ScoreEamPerformanceEntity>>) scoreEamPerformanceListEntity -> {
                    if (scoreEamPerformanceListEntity.result != null) {
                        return Flowable.fromIterable(scoreEamPerformanceListEntity.result);
                    }
                    return Flowable.fromIterable(new ArrayList<>());
                })
                .subscribe(scorePerformanceEntity -> {
                    if (!TextUtils.isEmpty(scorePerformanceEntity.scoreStandard) && !scorePerformanceEntity.scoreStandard.equals(category)) {
                        position = 0;
                        category = scorePerformanceEntity.scoreStandard;
                        scorePerformanceTitleEntity = new ScoreEamPerformanceEntity();
                        scorePerformanceTitleEntity.scoreStandard = scorePerformanceEntity.scoreStandard;
                        scorePerformanceTitleEntity.defaultTotalScore = scorePerformanceEntity.defaultTotalScore;
                        scorePerformanceTitleEntity.viewType = ListType.TITLE.value();
                        scoreMap.put(scorePerformanceTitleEntity.scoreStandard, scorePerformanceTitleEntity);
                    }

                    ScoreEamPerformanceEntity scorePerformanceOldTitleEntity;
                    if (scoreMap.containsKey(scorePerformanceEntity.itemDetail)) {
                        scorePerformanceOldTitleEntity = scoreMap.get(scorePerformanceEntity.itemDetail);
                    } else {
                        position++;
                        scorePerformanceOldTitleEntity = scorePerformanceEntity;
                    }

                    if (TextUtils.isEmpty(scorePerformanceOldTitleEntity.isItemValue)) {
                        scorePerformanceOldTitleEntity.marks.put(scorePerformanceEntity.scoreItem + "(" + Util.big0(scorePerformanceEntity.score) + ")"
                                , scorePerformanceEntity.score);
                        scorePerformanceOldTitleEntity.marksState.put(scorePerformanceEntity.scoreItem + "(" +  Util.big0(scorePerformanceEntity.score) + ")"
                                , scorePerformanceEntity.result);
                        scorePerformanceOldTitleEntity.scorePerformanceEntityMap.put(scorePerformanceEntity.scoreItem + "(" +  Util.big0(scorePerformanceEntity.score) + ")"
                                , scorePerformanceEntity);
                    }
                    scorePerformanceOldTitleEntity.Index = position;

                    if (scorePerformanceOldTitleEntity.resultValue != 0) {
                        scorePerformanceOldTitleEntity.viewType = ListType.HEADER.value();
                    } else {
                        scorePerformanceOldTitleEntity.viewType = ListType.CONTENT.value();
                    }
                    if (scorePerformanceTitleEntity != null) {
                        scorePerformanceTitleEntity.scorePerformanceEntities.add(scorePerformanceOldTitleEntity);
                        scorePerformanceOldTitleEntity.scoreEamPerformanceEntity = scorePerformanceTitleEntity;
                    }
                    scoreMap.put(scorePerformanceOldTitleEntity.itemDetail, scorePerformanceOldTitleEntity);
                }, throwable -> {
                    getView().getScorPerformanceFailed(throwable.toString());
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        category = "";
                        getView().getScorPerformanceSuccess(new ArrayList<>(scoreMap.values()));
                    }
                }));
    }
}
