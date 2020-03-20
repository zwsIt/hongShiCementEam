package com.supcon.mes.module_contact.presenter;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.module_contact.model.contract.ContactDataContract;
import com.supcon.mes.module_contact.model.network.ContactHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2019/12/5
 * Email:wangshizhan@supcom.com
 * 下载在线通讯录数据用
 */
public class ContactDataPresenter extends ContactDataContract.Presenter {

    @Override
    public void getStaffList(int pageNo, int pageSize) {

        mCompositeSubscription.add(
                ContactHttpClient.getStaffList(EamApplication.getCid(), pageNo, pageSize)
                        .onErrorReturn(new Function<Throwable, CommonBAPListEntity<ContactEntity>>() {
                            @Override
                            public CommonBAPListEntity<ContactEntity> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity commonEntity = new CommonBAPListEntity();
                                commonEntity.success = false;
                                commonEntity.errMsg = throwable.toString();
                                return commonEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<ContactEntity>>() {
                            @Override
                            public void accept(CommonBAPListEntity<ContactEntity> commonBAPListEntity) throws Exception {
                                if (commonBAPListEntity.result != null && commonBAPListEntity.result.size() > 0) {
                                    getView().getStaffListSuccess(commonBAPListEntity);
                                } else {
                                    getView().getStaffListFailed(commonBAPListEntity.errMsg);
                                }
                            }
                        }));


    }
}
