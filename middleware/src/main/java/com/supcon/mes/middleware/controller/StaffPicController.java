package com.supcon.mes.middleware.controller;

import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.view.CustomPotraitView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.api.StaffPicDownloadAPI;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.contract.StaffPicDownloadContract;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.presenter.StaffPicPresenter;

import java.io.File;

/**
 * Created by wangshizhan on 2018/8/1
 * Email:wangshizhan@supcom.com
 */
@Presenter(StaffPicPresenter.class)
public class StaffPicController extends BaseViewController implements StaffPicDownloadContract.View, StaffPicDownloadAPI {

    private OnSuccessListener<File> mSuccessListener;
    private File picFile;
    private long imageId;

    @BindByTag("minePotrait")
    CustomPotraitView minePotrait;

    public StaffPicController(View rootView) {
        super(rootView);
    }

    @Override
    public void getStaffPicSuccess(File file) {
        picFile = new File(file.getParent(), String.valueOf(imageId)+".jpg");
        file.renameTo(picFile);
        if(mSuccessListener!=null){
            mSuccessListener.onSuccess(picFile);
        }

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_people)
                .override(DisplayUtil.dip2px(100, context), DisplayUtil.dip2px(100, context))
                .centerInside();
        Glide.with(context).load(picFile).apply(requestOptions).into(minePotrait.imageView());
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        AccountInfo accountInfo = EamApplication.getAccountInfo();

        if(accountInfo != null){
            minePotrait.setUsername(accountInfo.staffName);
            minePotrait.setText("" + accountInfo.staffCode);
            long imgId= accountInfo.imageId;
            if(imgId != 0)
                getStaffPic(accountInfo.imageId);
            else
                minePotrait.setIcon(R.drawable.ic_user_default);
        }
    }

    @Override
    public void getStaffPicFailed(String errorMsg) {
        LogUtil.e("StaffPicController:" + errorMsg);

    }

    @Override
    public void getStaffPic(long id) {
        this.imageId = id;
        presenterRouter.create(StaffPicDownloadAPI.class).getStaffPic(id);
    }

    public File getPicFile() {
        return picFile;
    }


}
