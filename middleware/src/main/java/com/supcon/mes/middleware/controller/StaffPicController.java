package com.supcon.mes.middleware.controller;

import android.text.TextUtils;
import android.view.View;

import com.app.annotation.Presenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
    private String picServerPath; // 图片路径

    public StaffPicController(View rootView) {
        super(rootView);
    }

    @Override
    public void getStaffPicSuccess(File file) {
        picFile = new File(file.getParent(), imageId + ".jpg");
        file.renameTo(picFile); // 下载的图片重命名为staffId的
        if (mSuccessListener != null) {
            mSuccessListener.onSuccess(picFile);
        }

        if (userIcon != null && userIcon.getTag(R.id.imageid).equals(imageId)) {

            Glide.with(userIcon.getContext()).load(picFile).apply(RequestOptionUtil.getNoCacheRequestOptions()).into(userIcon);
        }

        if (mineUserIcon != null) {
//            RequestOptions requestOptions = new RequestOptions()
//                    .placeholder(R.drawable.ic_default_txl_pic)
//                    .override(DisplayUtil.dip2px(100, context), DisplayUtil.dip2px(100, context))
//                    .centerInside();
            Glide.with(context).load(picFile).apply(RequestOptionUtil.getNoCacheRequestOptions()).into(mineUserIcon);
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
            if (imgId != 0){
                this.imageId = accountInfo.staffId;
                getStaffPic(accountInfo.imageId,"");
            }else
                mineUserIcon.setImageResource(R.drawable.ic_default_txl_pic);
        }
    }

    @Override
    public void getStaffPicFailed(String errorMsg) {
        LogUtil.e("StaffPicController:" + errorMsg);
//        if (userIcon != null && userIcon.getTag(R.id.imageid).equals(imageId)) {
//            Glide.with(userIcon.getContext()).load(R.drawable-xhdpi.ic_default_pic3).into(userIcon);
//        }
    }

    @Override
    public void getDocIdsSuccess(CommonEntity entity) {
        // 根据获取的图片附件的id下载图片
        presenterRouter.create(StaffPicDownloadAPI.class).getStaffPic(Long.parseLong((String) entity.result),getPicType());
    }


    @Override
    public void getDocIdsFailed(String errorMsg) {
        LogUtil.e("StaffPicController:" + errorMsg);
        if (userIcon != null && userIcon.getTag(R.id.imageid).equals(imageId)) {
            Glide.with(userIcon.getContext()).load(R.drawable.ic_default_txl_pic).into(userIcon);
        }
    }

    @Override
    public void getStaffPic(long id, String picType) {
//        this.imageId = id;
        presenterRouter.create(StaffPicDownloadAPI.class).getStaffPic(id,picType);
    }

    @Override
    public void getDocIds(long linkId) {
        // 获取图片的id
        presenterRouter.create(StaffPicDownloadAPI.class).getDocIds(linkId);
    }

    public File getPicFile() {
        return picFile;
    }

    /**'
     * 通讯录头像
     * @param linkId
     * @param userIcon
     * @param picServerPath
     */
    public void initStaffPic(long linkId, CustomCircleTextImageView userIcon,String picServerPath) {
        this.imageId = linkId;  // staffId 即 linkId，同时赋值为imageId
        this.userIcon = userIcon;
        this.picServerPath = picServerPath;
        getDocIds(linkId);
    }

    public void setImageView(CustomCircleTextImageView mineUserIcon) {
        this.mineUserIcon = mineUserIcon;
    }

    /**
     * 解析图片格式
     */
    private String getPicType(){
        if (TextUtils.isEmpty(picServerPath)) return "";

        return picServerPath.substring(picServerPath.lastIndexOf("."));
    }

}
