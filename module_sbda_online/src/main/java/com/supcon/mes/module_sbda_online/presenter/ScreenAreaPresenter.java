package com.supcon.mes.module_sbda_online.presenter;

import android.annotation.SuppressLint;

import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.middleware.util.ChannelUtil;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.middleware.model.bean.ScreenEntity;
import com.supcon.mes.module_sbda_online.model.bean.ScreenListEntity;
import com.supcon.mes.module_sbda_online.model.contract.ScreenAreaContract;
import com.supcon.mes.module_sbda_online.model.network.SBDAOnlineHttpClient;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class ScreenAreaPresenter extends ScreenAreaContract.Presenter {

    private String url;

    @SuppressLint("CheckResult")
    @Override
    public void screenPart(CustomFilterView customFilterView) {
        ScreenEntity screenEntity = new ScreenEntity();
        screenEntity.name = "区域不限";

        List<ScreenEntity> screenEntities = new LinkedList<>();
        screenEntities.add(screenEntity);
        url = "/BEAM/area/area/areaList-query.action?page.pageSize=500";
        mCompositeSubscription.add(SBDAOnlineHttpClient.screenPart(url)
                .onErrorReturn(throwable -> {
                    ScreenListEntity screenListEntity = new ScreenListEntity();
                    screenListEntity.errMsg = throwable.toString();
                    return screenListEntity;
                }).subscribe(screenListEntity -> {
                    if (screenListEntity.result != null && screenListEntity.result.size() > 0) {
                        Flowable.fromIterable(screenListEntity.result)
                                .filter(screenEntity12 -> {
                                    if (Util.countStr(screenEntity12.layRec, "-") > 1) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                })
                                .subscribe(screenEntity1 -> {
                                    if (!screenEntities.contains(screenEntity1)) {
                                        screenEntities.add(screenEntity1);
                                    }
                                }, throwable -> {
                                }, () -> {
                                    customFilterView.setData(screenEntities);
                                    customFilterView.setCurrentItem(screenEntity);
                                });
                    } else {
                        customFilterView.setData(screenEntities);
                    }
                }));
    }
}
