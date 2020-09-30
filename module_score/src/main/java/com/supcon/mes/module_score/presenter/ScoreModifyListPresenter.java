package com.supcon.mes.module_score.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.PageParamUtil;
import com.supcon.mes.module_score.model.bean.ScoreRecordEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.model.network.ScoreHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ScoreStaffRankListPresenter
 * created by zhangwenshuai1 2020/8/3
 * 人员评分排名列表
 */
public class ScoreModifyListPresenter extends CommonListContract.Presenter {
    @Override
    public void listCommonObj(int pageNo, Map<String, Object> queryMap, boolean OffOn) {
        Map<String, Object> pageQueryParam = PageParamUtil.pageQueryParam(pageNo, 1000);

        Map<String, Object> queryParam = new HashMap<>();
        if (queryMap.containsKey(Constant.BAPQuery.ID)){
            queryParam.put(Constant.BAPQuery.ID, queryMap.get(Constant.BAPQuery.ID));
            queryMap.remove(Constant.BAPQuery.ID);
        }
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryMap);
        // 评分人员
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(queryParam, "base_staff,ID,BEAM_SCORE_RECORDS,PATROL_WORKER");
        fastQueryCondEntity.subconds.add(joinSubcondEntity);
        fastQueryCondEntity.modelAlias = "scoreRecord";

        mCompositeSubscription.add(
                ScoreHttpClient.queryModifyRecord(fastQueryCondEntity, pageQueryParam)
                        .onErrorReturn(new Function<Throwable, CommonBAPListEntity<ScoreRecordEntity>>() {
                            @Override
                            public CommonBAPListEntity<ScoreRecordEntity> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity<ScoreRecordEntity> commonBAPListEntity = new CommonBAPListEntity<>();
                                commonBAPListEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<ScoreRecordEntity>>() {
                            @Override
                            public void accept(CommonBAPListEntity<ScoreRecordEntity> commonBAPListEntity) throws Exception {
                                if (TextUtils.isEmpty(commonBAPListEntity.errMsg) && commonBAPListEntity.result != null) {
                                    getView().listCommonObjSuccess(commonBAPListEntity);
                                } else {
                                    getView().listCommonObjFailed(commonBAPListEntity.errMsg);
                                }
                            }
                        })
        );
    }
}
