package com.supcon.mes.module_yhgl.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.mes.middleware.model.bean.YHEntityVo;
import com.supcon.mes.module_yhgl.model.bean.YHDtoListEntity;
import com.supcon.mes.module_yhgl.model.contract.OfflineYHListContract;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

/**
 * Created by xushiyun on 2018/8/14
 * Email:ciruy.victory@gmail.com
 *
 * @author xushiyun
 */
public class OfflineYHListPresenter extends OfflineYHListContract.Presenter {

    @SuppressLint("CheckResult")
    @Override
    public void queryYHList(int pageNum, Map<String, Object> queryParams) {
        Flowable.timer(150, TimeUnit.MILLISECONDS)
                .map(aLong -> YHEntityVo.genFilterResult(pageNum, queryParams))
                .map(yhEntityDtos -> {
                    YHDtoListEntity yhDtoListEntity = new YHDtoListEntity();
                    yhDtoListEntity.result = yhEntityDtos;
                    yhDtoListEntity.success =  yhEntityDtos != null && yhEntityDtos.size() > 0;
                    return yhDtoListEntity;
                })
                .compose(RxSchedulers.io_main())
                .subscribe(yhDtoListEntity -> {
                    if (pageNum > 1) {
                        if (!yhDtoListEntity.success || yhDtoListEntity.result == null || yhDtoListEntity.result.size() == 0) {
                            yhDtoListEntity.success = true;
                            yhDtoListEntity.result = new ArrayList<>();
                            getView().queryYHListSuccess(yhDtoListEntity);
                            return;
                        }
                        getView().queryYHListSuccess(yhDtoListEntity);
                        return;
                    }
                        getView().queryYHListSuccess(yhDtoListEntity);
                });
    }

}
