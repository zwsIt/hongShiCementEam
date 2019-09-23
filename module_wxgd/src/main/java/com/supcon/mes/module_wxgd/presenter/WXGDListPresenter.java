package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;
import com.supcon.mes.module_wxgd.model.contract.WXGDListContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;
import com.supcon.mes.module_wxgd.util.WXGDFastQueryCondHelper;

import java.util.HashMap;
import java.util.Map;

public class WXGDListPresenter extends WXGDListContract.Presenter {
    @Override
    public void listWxgds(int pageNum, Map<String, Object> queryParam) {
        FastQueryCondEntity fastQueryCondEntity = WXGDFastQueryCondHelper.createFastQueryCond(queryParam);
        fastQueryCondEntity.modelAlias = "workRecord";

        Map<String, Object> pageQueryParam = new HashMap<>();
        pageQueryParam.put("page.pageSize", 20);
        pageQueryParam.put("page.maxPageSize", 500);
        pageQueryParam.put("page.pageNo", pageNum);

        String url = "/BEAM2/workList/workRecord/workList-pending.action?1=1&permissionCode=BEAM2_1.0.0_workList_workList";
        if (queryParam.containsKey(Constant.BAPQuery.WORK_STATE)
                && queryParam.get(Constant.BAPQuery.WORK_STATE).equals(Constant.WxgdStatus.COMPLETE)) {
            url = "/BEAM2/workList/workRecord/workList-query.action?1=1&permissionCode=BEAM2_1.0.0_workList_workList";
        }
        mCompositeSubscription.add(
                HttpClient.listWxgds(url, fastQueryCondEntity, pageQueryParam)
                        .onErrorReturn(throwable -> {
                            WXGDListEntity wxgdListEntity = new WXGDListEntity();
                            wxgdListEntity.success = false;
                            wxgdListEntity.errMsg = throwable.toString();
                            return wxgdListEntity;
                        })
                        .subscribe(wxgdListEntity -> {
                            if (wxgdListEntity.errMsg == null) {
                                getView().listWxgdsSuccess(wxgdListEntity);
                            } else {
                                getView().listWxgdsFailed(wxgdListEntity.errMsg);
                            }
                        })
        );
    }
}
