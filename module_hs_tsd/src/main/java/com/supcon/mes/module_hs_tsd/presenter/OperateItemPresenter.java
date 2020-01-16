package com.supcon.mes.module_hs_tsd.presenter;

import android.text.TextUtils;

import com.supcon.mes.module_hs_tsd.model.bean.OperateItemListEntity;
import com.supcon.mes.module_hs_tsd.model.contract.OperateItemContract;
import com.supcon.mes.module_hs_tsd.model.network.HttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/28
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class OperateItemPresenter extends OperateItemContract.Presenter {
    @Override
    public void listOperateItems(String url, Long tableId) {
        mCompositeSubscription.add(
                HttpClient.listOperateItem(url,tableId)
                .onErrorReturn(new Function<Throwable, OperateItemListEntity>() {
                    @Override
                    public OperateItemListEntity apply(Throwable throwable) throws Exception {
                        OperateItemListEntity operateItemListEntity = new OperateItemListEntity();
                        operateItemListEntity.errMsg = throwable.toString();
                        return operateItemListEntity;
                    }
                })
                .subscribe(new Consumer<OperateItemListEntity>() {
                    @Override
                    public void accept(OperateItemListEntity operateItemListEntity) throws Exception {
                        if (TextUtils.isEmpty(operateItemListEntity.errMsg)){
                            getView().listOperateItemsSuccess(operateItemListEntity);
                        }else {
                            getView().listOperateItemsFailed(operateItemListEntity.errMsg);
                        }
                    }
                })
        );
    }
}
