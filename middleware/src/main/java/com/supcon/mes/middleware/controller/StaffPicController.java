package com.supcon.mes.middleware.controller;

import android.view.View;

import com.app.annotation.Presenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.api.StaffPicDownloadAPI;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.contract.StaffPicDownloadContract;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.presenter.StaffPicPresenter;
import com.supcon.mes.middleware.util.RequestOptionUtil;

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

    CustomCircleTextImageView mineUserIcon;

    CustomCircleTextImageView userIcon;

    public StaffPicController(View rootView) {
        super(rootView);
    }

    @Override
    public void getStaffPicSuccess(File file) {
        picFile = new File(file.getParent(), imageId + ".jpg");
        file.renameTo(picFile);
        if (mSuccessListener != null) {
            mSuccessListener.onSuccess(picFile);
        }

        if (userIcon != null && userIcon.getTag(R.id.imageid).equals(imageId)) {
            Glide.with(userIcon.getContext()).load(picFile).apply(RequestOptionUtil.getEamRequestOptions(userIcon.getContext())).into(userIcon);
        }

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_people)
                .override(DisplayUtil.dip2px(100, context), DisplayUtil.dip2px(100, context))
                .centerInside();
        if (mineUserIcon != null) {
            Glide.with(context).load(picFile).apply(requestOptions).into(mineUserIcon);
        }
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        AccountInfo accountInfo = EamApplication.getAccountInfo();

        if (accountInfo != null) {
            long imgId = accountInfo.imageId;
            if (imgId != 0)
                getStaffPic(accountInfo.imageId);
            else
                mineUserIcon.setImageResource(R.drawable.ic_user_default);
        }
    }

    @Override
    public void getStaffPicFailed(String errorMsg) {
        LogUtil.e("StaffPicController:" + errorMsg);
//        if (userIcon != null && userIcon.getTag(R.id.imageid).equals(imageId)) {
//            Glide.with(userIcon.getContext()).load(R.drawable.ic_default_pic3).into(userIcon);
//        }
    }

    @Override
    public void getDocIdsSuccess(CommonEntity entity) {
        presenterRouter.create(StaffPicDownloadAPI.class).getStaffPic(Long.parseLong((String) entity.result));
    }


    @Override
    public void getDocIdsFailed(String errorMsg) {
        LogUtil.e("StaffPicController:" + errorMsg);
        if (userIcon != null && userIcon.getTag(R.id.imageid).equals(imageId)) {
            Glide.with(userIcon.getContext()).load(R.drawable.ic_default_pic3).into(userIcon);
        }
    }

    @Override
    public void getStaffPic(long id) {
        this.imageId = id;
        presenterRouter.create(StaffPicDownloadAPI.class).getStaffPic(id);
    }

    @Override
    public void getDocIds(long linkId) {
        presenterRouter.create(StaffPicDownloadAPI.class).getDocIds(linkId);
    }

    public File getPicFile() {
        return picFile;
    }

    public void initEamPic(long linkId, CustomCircleTextImageView userIcon) {
        this.imageId = linkId;
        this.userIcon = userIcon;
        getDocIds(linkId);
    }

    public void setImageView(CustomCircleTextImageView mineUserIcon) {
        this.mineUserIcon = mineUserIcon;
    }
}
