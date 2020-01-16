package com.supcon.mes.module_contact.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.ContactEntityDao;
import com.supcon.mes.module_contact.model.contract.ContactContract;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2019/12/3
 * Email:wangshizhan@supcom.com
 * 筛选数据用，同部门、所有人、我的下属、搜索
 */
public class ContactPresenter extends ContactContract.Presenter {

    @SuppressLint("CheckResult")
    @Override
    public void getContactList(int pageNo, int pageSize, String content, String departmentName) {
        Flowable.timer(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        QueryBuilder<ContactEntity> queryBuilder = EamApplication.dao().getContactEntityDao().queryBuilder();
                        List<ContactEntity> contactEntities;

                        if (!TextUtils.isEmpty(departmentName)) {
                            if (TextUtils.isEmpty(content)) {
                                contactEntities = queryBuilder
                                        .where(ContactEntityDao.Properties.DEPARTMENTNAME.eq(departmentName))
                                        .orderAsc(ContactEntityDao.Properties.SearchPinyin)
                                        .offset((pageNo - 1) * pageSize)
                                        .limit(pageSize)
                                        .list();
                            } else {
                                contactEntities = queryBuilder
                                        .where(ContactEntityDao.Properties.DEPARTMENTNAME.eq(departmentName))
                                        .whereOr(ContactEntityDao.Properties.NAME.like("%" + content + "%"),
                                                ContactEntityDao.Properties.MOBILE.like("%" + content + "%"),
                                                ContactEntityDao.Properties.EMAIL.like("%" + content + "%"),
                                                ContactEntityDao.Properties.SearchPinyin.like("%" + content + "%"),
                                                ContactEntityDao.Properties.CODE.like("%" + content + "%"),
                                                ContactEntityDao.Properties.POSITIONNAME.like("%" + content + "%")
                                        )
                                        .orderAsc(ContactEntityDao.Properties.SearchPinyin)
                                        .offset((pageNo - 1) * pageSize)
                                        .limit(pageSize)
                                        .list();
                            }

                        } else {
                            if (!TextUtils.isEmpty(content)) {
                                contactEntities = queryBuilder
                                        .whereOr(ContactEntityDao.Properties.NAME.like("%" + content + "%"),
                                                ContactEntityDao.Properties.MOBILE.like("%" + content + "%"),
                                                ContactEntityDao.Properties.EMAIL.like("%" + content + "%"),
                                                ContactEntityDao.Properties.SearchPinyin.like("%" + content + "%"),
                                                ContactEntityDao.Properties.CODE.like("%" + content + "%"),
                                                ContactEntityDao.Properties.POSITIONNAME.like("%" + content + "%"),
                                                ContactEntityDao.Properties.DEPARTMENTNAME.like("%" + content + "%")
                                        )
                                        .orderAsc(ContactEntityDao.Properties.SearchPinyin)
                                        .offset((pageNo - 1) * pageSize)
                                        .limit(pageSize)
                                        .list();
                            } else {
                                contactEntities = queryBuilder
                                        .orderAsc(ContactEntityDao.Properties.SearchPinyin)
                                        .offset((pageNo - 1) * pageSize)
                                        .limit(pageSize)
                                        .list();
                            }
                        }

                        LogUtil.d("recentContactEntities size:" + contactEntities.size());
                        getView().getContactListSuccess(contactEntities);
                    }
                });
    }
}
