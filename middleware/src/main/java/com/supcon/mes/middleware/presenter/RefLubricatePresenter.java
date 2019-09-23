package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.model.bean.LubricateListEntity;
import com.supcon.mes.middleware.model.bean.RefLubricateListEntity;
import com.supcon.mes.middleware.model.contract.RefLubricateContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/5
 * ------------- Description -------------
 */
public class RefLubricatePresenter extends RefLubricateContract.Presenter {
    @Override
    public void listLubricate(int pageNum, Map<String, Object> queryParam) {
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryParam);
        fastQueryCondEntity.modelAlias = "lubricateOil";
        Map<String, Object> pageQueryParam = new HashMap<>();
        pageQueryParam.put("page.pageSize", 20);
        pageQueryParam.put("page.maxPageSize", 500);
        pageQueryParam.put("page.pageNo", pageNum);

        mCompositeSubscription.add(MiddlewareHttpClient.listLubricate(fastQueryCondEntity, pageQueryParam).onErrorReturn(throwable -> {
            LubricateListEntity lubricateListEntity = new LubricateListEntity();
            lubricateListEntity.errMsg = throwable.toString();
            return lubricateListEntity;
        }).subscribe(lubricateListEntity -> {
            if (lubricateListEntity.errMsg == null) {
                getView().listLubricateSuccess(lubricateListEntity);
            } else {
                getView().listLubricateFailed(lubricateListEntity.errMsg);
            }
        }));
    }

    @Override
    public void listRefLubricate(int pageNum, Long eamID, Map<String, Object> queryParam) {
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        if (queryParam.containsKey(Constant.BAPQuery.NAME)) {
            Map<String, Object> nameParam = new HashMap<>();
            nameParam.put(Constant.BAPQuery.NAME, queryParam.get(Constant.BAPQuery.NAME));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(nameParam, "BEAM_LUBRICATE_OILS,ID,BEAM_JWXITEMS,LUBRICATE_OIL");
            fastQueryCondEntity.subconds.add(joinSubcondEntity);
        }
        fastQueryCondEntity.modelAlias = "jWXItem";
        Map<String, Object> pageQueryParam = new HashMap<>();
        pageQueryParam.put("page.pageSize", 20);
        pageQueryParam.put("page.maxPageSize", 500);
        pageQueryParam.put("page.pageNo", pageNum);

        pageQueryParam.put("eamID", eamID);

        mCompositeSubscription.add(MiddlewareHttpClient.listRefLubricate(fastQueryCondEntity, pageQueryParam).onErrorReturn(throwable -> {
            RefLubricateListEntity lubricateListEntity = new RefLubricateListEntity();
            lubricateListEntity.errMsg = throwable.toString();
            return lubricateListEntity;
        }).subscribe(lubricateListEntity -> {
            if (lubricateListEntity.errMsg == null) {
                getView().listRefLubricateSuccess(lubricateListEntity);
            } else {
                getView().listRefLubricateFailed(lubricateListEntity.errMsg);
            }
        }));
    }
}
