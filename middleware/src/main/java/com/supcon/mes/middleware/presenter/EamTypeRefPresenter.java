package com.supcon.mes.middleware.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.LubricatingPartEntity;
import com.supcon.mes.middleware.model.contract.EamTypeRefContract;
import com.supcon.mes.middleware.model.contract.LubricatePartRefContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/11/5
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class EamTypeRefPresenter extends EamTypeRefContract.Presenter {
    @Override
    public void listEamType(int pageNo, Map<String, Object> queryMap) {

        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryMap);
        fastQueryCondEntity.modelAlias = "eamType";

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page.pageSize", "20");
        pageMap.put("page.maxPageSize", "500");
        pageMap.put("page.pageNo", pageNo);
        mCompositeSubscription.add(
                MiddlewareHttpClient.listEamTypeRef(fastQueryCondEntity, pageMap)
                        .onErrorReturn(new Function<Throwable, CommonBAPListEntity<EamType>>() {
                            @Override
                            public CommonBAPListEntity<EamType> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                                commonBAPListEntity.errMsg = throwable.toString();
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<EamType>>() {
                            @Override
                            public void accept(CommonBAPListEntity<EamType> eamTypeCommonBAPListEntity) throws Exception {
                                if (TextUtils.isEmpty(eamTypeCommonBAPListEntity.errMsg) && eamTypeCommonBAPListEntity.result != null) {
                                    getView().listEamTypeSuccess(eamTypeCommonBAPListEntity);
                                } else {
                                    getView().listEamTypeFailed(eamTypeCommonBAPListEntity.errMsg);
                                }
                            }
                        })
        );
    }
}
