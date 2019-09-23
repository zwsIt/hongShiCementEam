package com.supcon.mes.module_main.presenter;

import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.module_main.model.bean.ScoreEntity;
import com.supcon.mes.module_main.model.contract.ScoreStaffContract;
import com.supcon.mes.module_main.model.network.MainClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/29
 * ------------- Description -------------
 * 人员评分
 */
public class ScoreStaffPresenter extends ScoreStaffContract.Presenter {
    @Override
    public void getPersonScore(String staffID) {
        mCompositeSubscription.add(MainClient.getPersonScore(staffID)
                .onErrorReturn(new Function<Throwable, CommonEntity<ScoreEntity>>() {
                    @Override
                    public CommonEntity<ScoreEntity> apply(Throwable throwable) throws Exception {
                        CommonEntity<ScoreEntity> waitDealtEntity = new CommonEntity<>();
                        waitDealtEntity.errMsg = throwable.toString();
                        return waitDealtEntity;
                    }
                }).subscribe(new Consumer<CommonEntity<ScoreEntity>>() {
                    @Override
                    public void accept(CommonEntity<ScoreEntity> scoreEntity) throws Exception {
                        if (scoreEntity.result != null) {
                            getView().getPersonScoreSuccess(scoreEntity);
                        } else {
                            getView().getPersonScoreFailed(scoreEntity.errMsg);
                        }
                    }
                }));
    }
}
