package com.supcon.mes.middleware.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.mes.middleware.model.bean.CommonFilterSearchListEntity;
import com.supcon.mes.middleware.model.contract.CommonFilterSearchContract;
import com.supcon.mes.middleware.model.factory.SearchContentFactory;
import com.supcon.mes.middleware.model.factory.SearchTagFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2210:55
 */
public class CommonFilterSearchPresenter extends CommonFilterSearchContract.Presenter {
    @Override
    public void getRecentTags(SearchContentFactory.FilterType type, Map<String,Object> param) {
        CommonFilterSearchListEntity result = SearchTagFactory.getRecentLabel(type, param);
        if (result.success) {
            getView().getRecentTagsSuccess(result);
        } else {
            getView().getRecentTagsFailed(result.errMsg);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAllRecommendTags(SearchContentFactory.FilterType type, Map<String,Object> param) {
        Flowable.timer(300, TimeUnit.MILLISECONDS)
                .map(object -> SearchTagFactory.getAllLabel(type, param))
                .compose(RxSchedulers.io_main())
                .subscribe(result -> {
                    if (result.success && result.result != null && result.result.size() > 0) {
                        getView().getAllRecommendTagsSuccess(result);
                    } else {
                        getView().getAllRecommendTagsFailed(result.errMsg);
                    }
                });

    }
}
