package com.supcon.mes.middleware.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.ContactEntityDao;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.contract.RecentEamSelectContract;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by zhangwenshuai on 2020/5/25
 * Email:zhangwenshuai1@supcom.com
 */
public class RecentEamSelectPresenter extends RecentEamSelectContract.Presenter {


//    private static final long MONTH_TIME = 2592000000L;

    @SuppressLint("CheckResult")
    @Override
    public void getRecentEamList(int pageNo, int pageSize, String content) {
//        Flowable.timer(100, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(aLong -> {
//                    List<EamEntity>  recentContactEntities = EamApplication.dao().getContactEntityDao().queryBuilder()
//                            .where(ContactEntityDao.Properties.UpdateTime.ge(1L))
//                            .orderDesc(ContactEntityDao.Properties.UpdateTime)
//                            .offset((pageNo-1)* pageSize)
//                            .limit(pageSize)
//                            .list();
//                    getView().getRecentContactListSuccess(recentContactEntities);
//                });
    }
}
