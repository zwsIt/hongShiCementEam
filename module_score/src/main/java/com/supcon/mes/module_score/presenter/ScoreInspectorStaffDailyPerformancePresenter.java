package com.supcon.mes.module_score.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffDailyPerformanceEntity;
import com.supcon.mes.module_score.model.contract.ScoreInspectorStaffDailyPerformanceContract;
import com.supcon.mes.module_score.model.network.ScoreHttpClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ScoreInspectorStaffDailyPerformancePresenter extends ScoreInspectorStaffDailyPerformanceContract.Presenter {

    private int position = 0;
    private String category = "";//评分标题
    private ScoreStaffDailyPerformanceEntity scorePerformanceTitleEntity;

    @SuppressLint("CheckResult")
    @Override
    public void getInspectorStaffDailyScore(int scoreId) {

        LinkedHashMap<String, ScoreStaffDailyPerformanceEntity> scoreMap = new LinkedHashMap();
        String url = "/BEAM/patrolWorkerScore/workerScoreHead/data-dg1564126989486.action";
        mCompositeSubscription.add(ScoreHttpClient.getInspectorStaffDailyScore(url, scoreId)
                .onErrorReturn(new Function<Throwable, CommonBAPListEntity<ScoreStaffDailyPerformanceEntity>>() {
                    @Override
                    public CommonBAPListEntity<ScoreStaffDailyPerformanceEntity> apply(Throwable throwable) throws Exception {
                        CommonBAPListEntity<ScoreStaffDailyPerformanceEntity> staffPerformanceEntity = new CommonBAPListEntity<>();
                        staffPerformanceEntity.errMsg = throwable.toString();
                        return staffPerformanceEntity;
                    }
                })
                .concatMap((Function<CommonBAPListEntity<ScoreStaffDailyPerformanceEntity>, Flowable<ScoreStaffDailyPerformanceEntity>>) scoreStaffPerformanceListEntity -> {
                    if (TextUtils.isEmpty(scoreStaffPerformanceListEntity.errMsg)) {
                        return Flowable.fromIterable(scoreStaffPerformanceListEntity.result);
                    } else {
                        getView().getInspectorStaffDailyScoreFailed(scoreStaffPerformanceListEntity.toString());
                    }
                    return Flowable.fromIterable(new ArrayList<>());
                })
                .subscribe(new Consumer<ScoreStaffDailyPerformanceEntity>() {
                    @Override
                    public void accept(ScoreStaffDailyPerformanceEntity scoreStaffDailyPerformanceEntity) throws Exception {
                        if (!TextUtils.isEmpty(scoreStaffDailyPerformanceEntity.basicPerformance) && !scoreStaffDailyPerformanceEntity.basicPerformance.equals(category)) {
                            position = 0;
                            category = scoreStaffDailyPerformanceEntity.basicPerformance;
                            scorePerformanceTitleEntity = new ScoreStaffDailyPerformanceEntity();
                            scorePerformanceTitleEntity.basicPerformance = scoreStaffDailyPerformanceEntity.basicPerformance;
                            scorePerformanceTitleEntity.scoreText = scoreStaffDailyPerformanceEntity.scoreText;
                            scorePerformanceTitleEntity.viewType = ListType.TITLE.value();
                            scoreMap.put(scorePerformanceTitleEntity.basicPerformance, scorePerformanceTitleEntity);
                        }

                        if (scoreMap.containsKey(scoreStaffDailyPerformanceEntity.basicPerDetail + scoreStaffDailyPerformanceEntity.define)) {
                            ScoreStaffDailyPerformanceEntity scoreStaffDailyPerformanceOld = scoreMap.get(scoreStaffDailyPerformanceEntity.basicPerDetail + scoreStaffDailyPerformanceEntity.define);
                            scoreStaffDailyPerformanceOld.dockPointsMap.put(scoreStaffDailyPerformanceEntity.dockPoints, scoreStaffDailyPerformanceEntity.result);
                            scoreStaffDailyPerformanceOld.checkStandardList.add(scoreStaffDailyPerformanceEntity.checkStandard);
                            scoreMap.put(scoreStaffDailyPerformanceEntity.basicPerDetail + scoreStaffDailyPerformanceEntity.define, scoreStaffDailyPerformanceOld);
                        } else {
                            position++;
                            scoreStaffDailyPerformanceEntity.Index = position;
                            scoreStaffDailyPerformanceEntity.viewType = ListType.CONTENT.value();
                            scoreStaffDailyPerformanceEntity.dockPointsMap.put(scoreStaffDailyPerformanceEntity.dockPoints, scoreStaffDailyPerformanceEntity.result);
                            scoreStaffDailyPerformanceEntity.checkStandardList.add(scoreStaffDailyPerformanceEntity.checkStandard);
                            scoreMap.put(scoreStaffDailyPerformanceEntity.basicPerDetail + scoreStaffDailyPerformanceEntity.define, scoreStaffDailyPerformanceEntity);
                        }
                    }
                }, throwable -> {
                    getView().getInspectorStaffDailyScoreFailed(throwable.toString());
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        if (scoreMap.values().size() > 0) {
                            getView().getInspectorStaffDailyScoreSuccess(new ArrayList<>(scoreMap.values()));
                        }
                    }
                }));
    }
}
