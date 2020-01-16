package com.supcon.mes.module_contact.controller;

import android.view.View;

import com.app.annotation.Presenter;
import com.bumptech.glide.Glide;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.contract.StaffPicDownloadContract;
import com.supcon.mes.middleware.presenter.StaffPicPresenter;
import com.supcon.mes.middleware.util.RequestOptionUtil;
import com.supcon.mes.module_contact.R;

import java.io.File;

/**
 * Created by wangshizhan on 2019/12/3
 * Email:wangshizhan@supcom.com
 */
@Presenter(StaffPicPresenter.class)
public class ContactPicController extends BaseViewController implements StaffPicDownloadContract.View {

    CustomCircleTextImageView userIcon;

    public ContactPicController(View rootView) {
        super(rootView);
    }


    public void getStaffPic(Long staffId){

        if (userIcon==null) {
            userIcon = getRootView().findViewById(R.id.userIcon);
        }

//        if(staffId!=null){
//            userIcon.setImageDrawable(null);
//            presenterRouter.create(StaffPicDownloadAPI.class).getStaffPic(staffId);
//        }

    }

    @Override
    public void getStaffPicSuccess(File entity) {
        Glide.with(userIcon.getContext()).load(entity).apply(RequestOptionUtil.getEamRequestOptions(userIcon.getContext())).into(userIcon);
    }

    @Override
    public void getStaffPicFailed(String errorMsg) {
        LogUtil.e(""+errorMsg);
    }

    @Override
    public void getDocIdsSuccess(CommonEntity entity) {

    }

    @Override
    public void getDocIdsFailed(String errorMsg) {

    }
}
