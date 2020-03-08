package com.supcon.mes.module_warn.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.PageParamUtil;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/8
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class DailyLubrRecordsFinishPresenter extends CommonListContract.Presenter {
    @Override
    public void listCommonObj(int pageNo, Map<String, Object> queryMap, boolean OffOn) {

        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();

        //中间层查询条件配置
        JoinSubcondEntity joinSubcondEntity = new JoinSubcondEntity();
        joinSubcondEntity.type = Constant.BAPQuery.TYPE_JOIN;
        joinSubcondEntity.joinInfo = "BEAM_JWXITEMS,ID,BEAM_DAILY_LUBRICATE_RECORS,JWX_ITEM_ID";
        joinSubcondEntity.subconds = new ArrayList<>();
        joinSubcondEntity.subconds.add(BAPQueryParamsHelper.createJoinSubcondEntity(queryMap,"EAM_BaseInfo,EAM_ID,BEAM_JWXITEMS,EAMID"));// 里间层查询条件配置;

        // 最外层查询条件
        fastQueryCondEntity.subconds = new ArrayList<>();
        fastQueryCondEntity.subconds.add(joinSubcondEntity);
        fastQueryCondEntity.modelAlias = "dailyLubricateRecor";

        Map<String,Object> pageParams = PageParamUtil.pageQueryParam(pageNo,20);
        mCompositeSubscription.add(
                EarlyWarnHttpClient.dailyLubricateTaskQuery(fastQueryCondEntity,pageParams)
        );
    }
}
