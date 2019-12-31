package com.supcon.mes.module_hs_tsd.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.PageParamUtil;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnListEntity;
import com.supcon.mes.module_hs_tsd.model.contract.ElectricityOffListContract;
import com.supcon.mes.module_hs_tsd.model.network.HttpClient;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/27
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ElectricityOffListPresenter extends ElectricityOffListContract.Presenter {

    @Override
    public void listElectricityOff(int pageNo, Map<String, Object> queryParams, boolean pendingQuery) {
        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
        fastQueryCondEntity.modelAlias = "onoroff";
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(queryParams,"EAM_BaseInfo,EAM_ID,BEAMELE_ONOROFFS,EAMID");
        fastQueryCondEntity.subconds = new ArrayList<>();
        fastQueryCondEntity.subconds.add(joinSubcondEntity);
        Map<String, Object> pageQueryParam = PageParamUtil.pageQueryParam(pageNo,20);
        pageQueryParam.put("fastQueryCond",fastQueryCondEntity);
        String url;
        if (pendingQuery){
            url = "/BEAMEle/onOrOff/onoroff/eleOffList-pending.action?1=1&permissionCode=BEAMEle_1.0.0_onOrOff_eleOffList";
        }else {
            url = "/BEAMEle/onOrOff/onoroff/eleOffList-query.action?1=1&permissionCode=BEAMEle_1.0.0_onOrOff_eleOffList";
        }

        mCompositeSubscription.add(
                HttpClient.eleOffList(url,pageQueryParam)
                        .onErrorReturn(new Function<Throwable, ElectricityOffOnListEntity>() {
                            @Override
                            public ElectricityOffOnListEntity apply(Throwable throwable) throws Exception {
                                ElectricityOffOnListEntity electricityOffOnListEntity = new ElectricityOffOnListEntity();
                                electricityOffOnListEntity.errMsg = throwable.toString();
                                return electricityOffOnListEntity;
                            }
                        })
                        .subscribe(new Consumer<ElectricityOffOnListEntity>() {
                            @Override
                            public void accept(ElectricityOffOnListEntity electricityOffOnListEntity) throws Exception {
                                if (TextUtils.isEmpty(electricityOffOnListEntity.errMsg)){
                                    getView().listElectricityOffSuccess(electricityOffOnListEntity);
                                }else {
                                    getView().listElectricityOffFailed(electricityOffOnListEntity.errMsg);
                                }
                            }
                        })
        );
    }
}
