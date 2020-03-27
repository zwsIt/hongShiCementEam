package com.supcon.mes.module_warn.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.PageParamUtil;
import com.supcon.mes.module_warn.model.bean.DailyLubricateRecordEntity;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/8
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class DailyLubrRecordsFinishPresenter extends CommonListContract.Presenter {
    @Override
    public void listCommonObj(int pageNo, Map<String, Object> queryMap, boolean OffOn) {

        Map<String, Object> queryMapEAmCode = new HashMap<>();
        Map<String, Object> queryMapDealTime = new HashMap<>();
        for (String key : queryMap.keySet()){
            if (key.equals(Constant.BAPQuery.EAM_CODE)){
                queryMapEAmCode.put(key,queryMap.get(key));
            } else if (key.equals(Constant.BAPQuery.EAM_ID)){
                queryMapEAmCode.put(key,queryMap.get(key));
            }else if (key.equals(Constant.BAPQuery.DEAL_TIME_S)){
                queryMapDealTime.put(key,queryMap.get(key));
            }
        }
        // 最外层查询条件
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryMapDealTime);
        fastQueryCondEntity.modelAlias = "dailyLubricateRecor";
        //中间层查询条件配置
        JoinSubcondEntity joinSubcondEntity = new JoinSubcondEntity();
        joinSubcondEntity.type = Constant.BAPQuery.TYPE_JOIN;
        joinSubcondEntity.joinInfo = "BEAM_JWXITEMS,ID,BEAM_DAILY_LUBRICATE_RECORS,JWX_ITEM_ID";
        joinSubcondEntity.subconds = new ArrayList<>();
        joinSubcondEntity.subconds.add(BAPQueryParamsHelper.createJoinSubcondEntity(queryMapEAmCode,"EAM_BaseInfo,EAM_ID,BEAM_JWXITEMS,EAMID"));// 里间层查询条件配置;
        fastQueryCondEntity.subconds.add(joinSubcondEntity);

        Map<String,Object> pageParams = PageParamUtil.pageQueryParam(pageNo,500);
        mCompositeSubscription.add(
                EarlyWarnHttpClient.dailyLubricateTaskQuery(fastQueryCondEntity,pageParams)
                .onErrorReturn(throwable -> {
                    CommonBAPListEntity<DailyLubricateRecordEntity> commonBAPListEntity = new CommonBAPListEntity<>();
                    commonBAPListEntity.errMsg = throwable.toString();
                    return commonBAPListEntity;
                })
                .subscribe(dailyLubricateRecordEntityCommonBAPListEntity -> {
                    if (TextUtils.isEmpty(dailyLubricateRecordEntityCommonBAPListEntity.errMsg)){
                        getView().listCommonObjSuccess(dailyLubricateRecordEntityCommonBAPListEntity);
                    }else {
                        getView().listCommonObjFailed(dailyLubricateRecordEntityCommonBAPListEntity.errMsg);
                    }
                })
        );
    }
}
