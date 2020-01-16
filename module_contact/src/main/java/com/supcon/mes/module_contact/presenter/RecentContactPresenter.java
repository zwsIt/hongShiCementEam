package com.supcon.mes.module_contact.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.ContactEntityDao;
import com.supcon.mes.module_contact.model.contract.RecentContactContract;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2019/12/3
 * Email:wangshizhan@supcom.com
 */
public class RecentContactPresenter extends RecentContactContract.Presenter {


//    private static final long MONTH_TIME = 2592000000L;

    @SuppressLint("CheckResult")
    @Override
    public void getRecentContactList(int pageNo, int pageSize, String content) {
        Flowable.timer(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        List<ContactEntity>  recentContactEntities = EamApplication.dao().getContactEntityDao().queryBuilder()
                                .where(ContactEntityDao.Properties.UpdateTime.ge(1L))
                                .orderDesc(ContactEntityDao.Properties.UpdateTime)
                                .offset((pageNo-1)* pageSize)
                                .limit(pageSize)
                                .list();
                        LogUtil.d("recentContactEntities size:"+recentContactEntities.size());
                        getView().getRecentContactListSuccess(recentContactEntities);
                    }
                });
    }
}
