package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.RefLubricateListEntity;
import com.supcon.mes.middleware.model.bean.RefMaintainListEntity;
import com.supcon.mes.middleware.model.contract.RefLubricateContract;
import com.supcon.mes.middleware.model.contract.RefMaintainContract;
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
public class RefMaintainPresenter extends RefMaintainContract.Presenter {
    @Override
    public void listRefMaintain(int pageNum, Long eamID, Map<String, Object> queryParam) {
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryParam);
        fastQueryCondEntity.modelAlias = "jWXItem";
        Map<String, Object> pageQueryParam = new HashMap<>();
        pageQueryParam.put("page.pageSize", 20);
        pageQueryParam.put("page.maxPageSize", 500);
        pageQueryParam.put("page.pageNo", pageNum);

        pageQueryParam.put("eamID", eamID);
        mCompositeSubscription.add(MiddlewareHttpClient.listRefMaintain(fastQueryCondEntity, pageQueryParam).onErrorReturn(throwable -> {
            RefMaintainListEntity maintainListEntity = new RefMaintainListEntity();
            maintainListEntity.errMsg = throwable.toString();
            return maintainListEntity;
        }).subscribe(maintainListEntity -> {
            if (maintainListEntity.errMsg == null) {
                getView().listRefMaintainSuccess(maintainListEntity);
            } else {
                getView().listRefMaintainFailed(maintainListEntity.errMsg);
            }
        }));
    }

}
