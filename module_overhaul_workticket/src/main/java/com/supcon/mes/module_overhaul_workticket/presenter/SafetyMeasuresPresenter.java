package com.supcon.mes.module_overhaul_workticket.presenter;

import android.text.TextUtils;

import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresList;
import com.supcon.mes.module_overhaul_workticket.model.contract.SafetyMeasuresContract;
import com.supcon.mes.module_overhaul_workticket.model.network.HttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class SafetyMeasuresPresenter extends SafetyMeasuresContract.Presenter {
    @Override
    public void listSafetyMeasures(Long workTicketId) {
        mCompositeSubscription.add(
                HttpClient.listSafetyMeasures(workTicketId)
                .onErrorReturn(new Function<Throwable, SafetyMeasuresList>() {
                    @Override
                    public SafetyMeasuresList apply(Throwable throwable) throws Exception {
                        SafetyMeasuresList safetyMeasuresList = new SafetyMeasuresList();
                        safetyMeasuresList.errMsg = throwable.toString();
                        return safetyMeasuresList;
                    }
                })
                .subscribe(new Consumer<SafetyMeasuresList>() {
                    @Override
                    public void accept(SafetyMeasuresList safetyMeasuresList) throws Exception {
                        if (TextUtils.isEmpty(safetyMeasuresList.errMsg)){
                            getView().listSafetyMeasuresSuccess(safetyMeasuresList);
                        }else {
                            getView().listSafetyMeasuresFailed(safetyMeasuresList.errMsg);
                        }
                    }
                })
        );
    }
}
