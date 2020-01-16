package com.supcon.mes.module_contact.controller;

import android.content.Context;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseDataController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.ContactEntityDao;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.PinYinUtils;
import com.supcon.mes.module_contact.model.api.ContactAPI;
import com.supcon.mes.module_contact.model.api.ContactDataAPI;
import com.supcon.mes.module_contact.model.contract.ContactDataContract;
import com.supcon.mes.module_contact.presenter.ContactDataPresenter;
import com.supcon.mes.module_contact.presenter.ContactPresenter;

import java.util.List;
import java.util.Objects;

/**
 * Created by wangshizhan on 2019/12/5
 * Email:wangshizhan@supcom.com
 */
@Presenter({ContactDataPresenter.class, ContactPresenter.class})
public class ContactDataController extends BaseDataController implements ContactDataContract.View {

    private static long updateTime = 0L;
    private List<ContactEntity> mContactEntities;
    private boolean isInitFinish = false;

    public ContactDataController(Context context) {
        super(context);
    }

    @Override
    public void onInit() {
        super.onInit();
        mContactEntities = EamApplication.dao().getContactEntityDao().loadAll();
    }

    @Override
    public void initData() {
        super.initData();

        if (mContactEntities.size() != 0 && updateTime != 0) {
            isInitFinish = true;
        } else {
            mContactEntities.clear();
            refreshAddressBookData();
        }
    }

    @Override
    public void getStaffListSuccess(CommonBAPListEntity data) {
        List<ContactEntity> contactEntityList = data.result;
        for (ContactEntity contactEntity : contactEntityList) {
            contactEntity.setSearchPinyin(PinYinUtils.getPinyin(Objects.requireNonNull(contactEntity.getNAME())));
            try {
                ContactEntity contact = EamApplication.dao().getContactEntityDao().queryBuilder()
                        .where(ContactEntityDao.Properties.CODE.eq(contactEntity.getCODE())).unique();
                if (contact != null) {
                    contactEntity.updateTime = contact.updateTime;
                    EamApplication.dao().getContactEntityDao().update(contactEntity);
                } else {
                    EamApplication.dao().getContactEntityDao().insertOrReplace(contactEntity);
                }

            } catch (Exception e) {
                EamApplication.dao().getContactEntityDao().insertOrReplace(contactEntity);
            }
        }

        mContactEntities.addAll(contactEntityList);
        if (data.hasNext) {
            presenterRouter.create(ContactDataAPI.class).getStaffList(data.nextPage, 50);
        } else {
            if (updateTime == 0) {
                updateTime = System.currentTimeMillis();
                LogUtil.w("人员更新时间：" + DateUtil.dateFormat(updateTime));
            }
            if (mSuccessListener != null) {
                mSuccessListener.onSuccess(mContactEntities);
            }
        }
        isInitFinish = true;
    }

    @Override
    public void getStaffListFailed(String errorMsg) {
        LogUtil.e("" + errorMsg);
    }

    public void refreshAddressBookData() {
        presenterRouter.create(ContactDataAPI.class).getStaffList(1, 50);
    }


    public void getContentList(int pageNo, int pageSize, String content) {

        presenterRouter.create(ContactAPI.class).getContactList(pageNo, pageSize, content, "");

    }

    private OnSuccessListener<List> mSuccessListener;

    public void setSuccessListener(OnSuccessListener<List> successListener) {
        mSuccessListener = successListener;
    }

    public List<ContactEntity> getContactEntities() {
        return mContactEntities;
    }

    public boolean isInitFinish() {
        return isInitFinish;
    }
}
