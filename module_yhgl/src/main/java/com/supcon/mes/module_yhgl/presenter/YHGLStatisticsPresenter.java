package com.supcon.mes.module_yhgl.presenter;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_yhgl.model.bean.YHListEntity;
import com.supcon.mes.module_yhgl.model.contract.YHGLStatisticsContract;
import com.supcon.mes.module_yhgl.model.network.YHGLHttpClient;
import com.supcon.mes.module_yhgl.util.YHQueryParamsHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/14
 * Email:wangshizhan@supcom.com
 */
public class YHGLStatisticsPresenter extends YHGLStatisticsContract.Presenter {

    @Override
    public void queryYHList(int pageNum, Map<String, Object> queryParams) {
        FastQueryCondEntity fastQueryCondEntity = YHQueryParamsHelper.createFastCondEntity(queryParams);
        fastQueryCondEntity.modelAlias = "faultInfo";

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", pageNum);
        pageQueryParams.put("page.pageSize", 20);
        pageQueryParams.put("page.maxPageSize", 500);
        LogUtil.d("fastQueryCondEntity:"+fastQueryCondEntity);
        mCompositeSubscription.add(
                YHGLHttpClient.faultInfoList(pageQueryParams, fastQueryCondEntity)
                        .onErrorReturn(new Function<Throwable, YHListEntity>() {
                            @Override
                            public YHListEntity apply(Throwable throwable) throws Exception {
                                YHListEntity yhListEntity = new YHListEntity();
                                yhListEntity.success = false;
                                yhListEntity.errMsg = throwable.toString();
                                return yhListEntity;
                            }
                        })
                        .subscribe(new Consumer<YHListEntity>() {
                            @Override
                            public void accept(YHListEntity yhListEntity) throws Exception {
                                if (yhListEntity.errMsg != null && !yhListEntity.success) {
                                    getView().queryYHListFailed(yhListEntity.errMsg);
                                } else {
                                    getView().queryYHListSuccess(yhListEntity);

                                }
                            }
                        })
        );

    }

}
