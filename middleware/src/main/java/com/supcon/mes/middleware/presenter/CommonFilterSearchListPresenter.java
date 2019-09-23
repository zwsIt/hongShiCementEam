package com.supcon.mes.middleware.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.mes.middleware.model.bean.CommonLabelListEntity;
import com.supcon.mes.middleware.model.contract.CommonFilterSearchListContract;
import com.supcon.mes.middleware.model.factory.SearchContentFactory;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/239:51
 */
public class CommonFilterSearchListPresenter extends CommonFilterSearchListContract.Presenter {
    @SuppressLint("CheckResult")
    @Override
    public void getCommonFilterSearchList(int pageIndex,SearchContentFactory.FilterType type, Map<String, Object> param) {
        Flowable.just(SearchContentFactory.createSearchContent(pageIndex, type, param))
                .compose(RxSchedulers.io_main())
                .subscribe(searchContent -> {
                    if(searchContent.success) {
                        if(null == searchContent.getResult()||searchContent.getResult().size()<=0) {
                            getView().getCommonFilterSearchListFailed("未搜索到数据信息！");
                        }
                        else {
                            getView().getCommonFilterSearchListSuccess(CommonLabelListEntity.success().result(searchContent.getResult()));
                        }
                    }
                    else {
                        getView().getCommonFilterSearchListFailed("未搜索到数据信息！");
                    }
                });

    }
}
