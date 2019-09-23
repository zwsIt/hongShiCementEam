package com.supcon.mes.module_score.ui.util;

import android.annotation.SuppressLint;

import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;
import com.supcon.mes.module_score.model.dto.ScoreEamDto;
import com.supcon.mes.module_score.model.dto.ScoreStaffDto;

import org.reactivestreams.Publisher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class ScoreMapManager {
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static Map<String, Object> createMap(ScoreEamEntity scoreEamEntity) {
        Map<String, Object> map = new HashMap<>();
        map.put("viewselect", "beamPerformanceEdit");
        map.put("datagridKey", "BEAM_scorePerformance_scoreHead_beamPerformanceEdit_datagrids");
        map.put("viewCode", "BEAM_1.0.0_scorePerformance_beamPerformanceEdit");
        map.put("modelName", "ScoreHead");
        if (scoreEamEntity.id != -1) {
            map.put("id", scoreEamEntity.id);
        }
        map.put("scoreHead.beamId.id", Util.strFormat2(scoreEamEntity.getBeamId().id));
        map.put("scoreHead.scoreStaff.id", Util.strFormat2(scoreEamEntity.getScoreStaff().id));
        map.put("scoreHead.scoreTableNo", Util.strFormat2(scoreEamEntity.scoreTableNo));
        map.put("scoreHead.scoreTime", format.format(scoreEamEntity.scoreTime));

        map.put("scoreHead.scoreNum", Util.strFormat2(scoreEamEntity.scoreNum));
        map.put("scoreHead.operationRate", Util.strFormat2(scoreEamEntity.operationRate));
        map.put("scoreHead.highQualityOperation", Util.strFormat2(scoreEamEntity.highQualityOperation));
        map.put("scoreHead.security", Util.strFormat2(scoreEamEntity.security));
        map.put("scoreHead.appearanceLogo", Util.strFormat2(scoreEamEntity.appearanceLogo));
        map.put("scoreHead.beamHeath", Util.strFormat2(scoreEamEntity.beamHeath));
        map.put("scoreHead.beamEstimate", Util.strFormat2(scoreEamEntity.beamEstimate));

        map.put("bap_validate_user_id", EamApplication.getAccountInfo().userId);
        return map;
    }

    @SuppressLint("CheckResult")
    public static void dataChange(List<ScoreEamPerformanceEntity> scorePerformanceEntities, ScoreEamEntity scoreEamEntity) {
        Flowable.fromIterable(scorePerformanceEntities)
                .filter(scorePerformanceEntity -> {
                    if (scorePerformanceEntity.viewType == ListType.TITLE.value()) {
                        return true;
                    }
                    return false;
                })
                .subscribe(scorePerformanceEntity -> {
                    if (scorePerformanceEntity.scoreStandard.contains("设备运转率")) {
                        scoreEamEntity.operationRate = scorePerformanceEntity.getTotalScore();
                    } else if (scorePerformanceEntity.scoreStandard.contains("高质量运行")) {
                        scoreEamEntity.highQualityOperation = scorePerformanceEntity.getTotalScore();
                    } else if (scorePerformanceEntity.scoreStandard.contains("安全防护")) {
                        scoreEamEntity.security = scorePerformanceEntity.getTotalScore();
                    } else if (scorePerformanceEntity.scoreStandard.contains("外观标识")) {
                        scoreEamEntity.appearanceLogo = scorePerformanceEntity.getTotalScore();
                    } else if (scorePerformanceEntity.scoreStandard.contains("设备卫生")) {
                        scoreEamEntity.beamHeath = scorePerformanceEntity.getTotalScore();
                    } else if (scorePerformanceEntity.scoreStandard.contains("档案管理")) {
                        scoreEamEntity.beamEstimate = scorePerformanceEntity.getTotalScore();
                    }
                });
    }

    @SuppressLint("CheckResult")
    public static List<ScoreEamDto> dataChange(List<ScoreEamPerformanceEntity> scorePerformanceEntities, String contains) {
        List<ScoreEamDto> scorePerformanceDto = new ArrayList<>();
        Flowable.fromIterable(scorePerformanceEntities)
                .filter(scorePerformanceEntity -> {
                    if (scorePerformanceEntity.viewType == ListType.CONTENT.value() || scorePerformanceEntity.viewType == ListType.HEADER.value()) {
                        if (scorePerformanceEntity.scoreStandard.contains(contains)) {
                            Map<String, ScoreEamPerformanceEntity> scorePerformanceEntityMap = scorePerformanceEntity.scorePerformanceEntityMap;
                            Map<String, Boolean> marksState = scorePerformanceEntity.marksState;
                            for (String key : scorePerformanceEntityMap.keySet()) {
                                ScoreEamPerformanceEntity scoreEntity = scorePerformanceEntityMap.get(key);
                                scoreEntity.result = marksState.get(key);
                            }
                            return true;
                        }
                    }
                    return false;
                })
                .flatMap((Function<ScoreEamPerformanceEntity, Publisher<ScoreEamPerformanceEntity>>) scorePerformanceEntity -> {
                    if (scorePerformanceEntity.scorePerformanceEntityMap.size() > 0) {
                        return Flowable.fromIterable(scorePerformanceEntity.scorePerformanceEntityMap.values());
                    } else {
                        return Flowable.just(scorePerformanceEntity);
                    }
                })
                .subscribe(scorePerformanceEntity -> {
                    ScoreEamDto scoreEamDto = new ScoreEamDto();
                    scoreEamDto.id = Util.strFormat2(scorePerformanceEntity.id);
                    scoreEamDto.item = scorePerformanceEntity.item;
                    scoreEamDto.result = Util.strFormat2(scorePerformanceEntity.result);
                    scoreEamDto.score = Util.big(scorePerformanceEntity.score);
                    scoreEamDto.itemDetail = scorePerformanceEntity.itemDetail;
                    scoreEamDto.isItemValue = scorePerformanceEntity.isItemValue;
                    scoreEamDto.noItemValue = scorePerformanceEntity.noItemValue;
                    scoreEamDto.scoreItem = scorePerformanceEntity.scoreItem;
                    scoreEamDto.scoreStandard = scorePerformanceEntity.scoreStandard;
                    scoreEamDto.defaultTotalScore = Util.big(scorePerformanceEntity.defaultTotalScore);

                    scoreEamDto.resultValue = Util.big(scorePerformanceEntity.resultValue);
                    scoreEamDto.accidentStopTime = Util.big(scorePerformanceEntity.accidentStopTime);
                    scoreEamDto.totalRunTime = Util.big(scorePerformanceEntity.totalRunTime);
                    scorePerformanceDto.add(scoreEamDto);
                });
        return scorePerformanceDto;
    }

    public static Map<String, Object> createMap(ScoreStaffEntity scoreStaffEntity) {
        Map<String, Object> map = new HashMap<>();

        map.put("modelName", "WorkerScoreHead");
        if (scoreStaffEntity.id != -1) {
            map.put("id", scoreStaffEntity.id);
        }
        map.put("workerScoreHead.patrolWorker.id", Util.strFormat2(scoreStaffEntity.getPatrolWorker().id));
        map.put("workerScoreHead.scoreData", format.format(scoreStaffEntity.scoreData));
        map.put("workerScoreHead.score", Util.big(scoreStaffEntity.score));
        map.put("workerScoreHead.siteScore", "30");
        map.put("bap_validate_user_id", EamApplication.getAccountInfo().userId);
        return map;
    }

    @SuppressLint("CheckResult")
    public static void addAvg(List<ScoreDutyEamEntity> scoreDutyEamEntities, Map<String, Object> map) {
        Flowable.fromIterable(scoreDutyEamEntities)
                .filter(scorePerformanceEntity -> {
                    if (scorePerformanceEntity.viewType == ListType.TITLE.value()) {
                        return true;
                    }
                    return false;
                })
                .subscribe(scorePerformanceEntity -> {
                    map.put("workerScoreHead.siteScore", Util.big(scorePerformanceEntity.avgScore));
                });
    }

    @SuppressLint("CheckResult")
    public static List<ScoreStaffDto> dataStaffChange(List<ScoreStaffPerformanceEntity> scorePerformanceEntities, String contains) {
        List<ScoreStaffDto> scorePerformanceDto = new ArrayList<>();
        Flowable.fromIterable(scorePerformanceEntities)
                .filter(scorePerformanceEntity -> {
                    if (scorePerformanceEntity.viewType == ListType.CONTENT.value()) {
                        if (scorePerformanceEntity.project.contains(contains)) {
                            return true;
                        }
                    }
                    return false;
                })
                .subscribe(scorePerformanceEntity -> {
                    ScoreStaffDto scoreEamDto = new ScoreStaffDto();
                    scoreEamDto.id = Util.strFormat2(scorePerformanceEntity.id);
                    scoreEamDto.category = scorePerformanceEntity.category;
                    scoreEamDto.project = scorePerformanceEntity.project;
                    scoreEamDto.score = Util.big(scorePerformanceEntity.score);
                    scoreEamDto.grade = scorePerformanceEntity.grade;
                    scoreEamDto.item = scorePerformanceEntity.item;
                    scoreEamDto.itemScore = Util.big(scorePerformanceEntity.itemScore);
                    scoreEamDto.result = Util.strFormat2(scorePerformanceEntity.result);
                    scoreEamDto.fraction = Util.big(scorePerformanceEntity.scoreEamPerformanceEntity.fraction);
                    scoreEamDto.isItemValue = scorePerformanceEntity.isItemValue;
                    scoreEamDto.noItemValue = scorePerformanceEntity.noItemValue;
                    scoreEamDto.defaultNumVal = scorePerformanceEntity.defaultNumVal > 0 ? Util.strFormat2(scorePerformanceEntity.defaultNumVal) : "";
                    scoreEamDto.defaultValueType = scorePerformanceEntity.defaultValueType;
                    scorePerformanceDto.add(scoreEamDto);
                });
        return scorePerformanceDto;
    }
}
