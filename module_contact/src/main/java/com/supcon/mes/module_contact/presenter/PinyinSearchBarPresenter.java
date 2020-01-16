package com.supcon.mes.module_contact.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.module_contact.model.contract.PinyinSearchBarContract;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangshizhan on 2019/12/12
 * Email:wangshizhan@supcom.com
 */
public class PinyinSearchBarPresenter extends PinyinSearchBarContract.Presenter {



    @SuppressLint("CheckResult")
    @Override
    public void findContactPosition(List<ContactEntity> contactEntities, String word) {

        Flowable.fromIterable(contactEntities)
                .subscribeOn(Schedulers.newThread())
                .filter(new Predicate<ContactEntity>() {
                    @Override
                    public boolean test(ContactEntity contactEntity) throws Exception {
                        LogUtil.d("filter:"+contactEntity.searchPinyin);
                        return contactEntity.searchPinyin.startsWith(word);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ContactEntity>() {
                    @Override
                    public void accept(ContactEntity contactEntity) throws Exception {
                        LogUtil.d("subscribe:"+contactEntity.searchPinyin);
                        getView().findContactPositionSuccess(contactEntity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });

    }
}
