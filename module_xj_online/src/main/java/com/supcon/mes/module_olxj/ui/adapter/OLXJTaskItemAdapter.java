package com.supcon.mes.module_olxj.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AttachmentController;
import com.supcon.mes.middleware.controller.AttachmentDownloadController;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_olxj.IntentRouter;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.constant.OLXJConstant;
import com.supcon.mes.module_olxj.model.bean.OLXJGroupEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.util.TextHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by zhangwenshuai1 on 2018/3/12.
 * 今日巡检-巡检明细
 *
 */

public class OLXJTaskItemAdapter extends BaseListDataRecyclerViewAdapter<OLXJWorkItemEntity> {

    public OLXJTaskItemAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position, OLXJWorkItemEntity olxjWorkItemEntity) {
        return olxjWorkItemEntity.viewType;
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJWorkItemEntity> getViewHolder(int viewType) {
        if (viewType == 1){
            return new TitleViewHolder(context);
        }else {
            return new ViewHolder(context);
        }
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<OLXJWorkItemEntity>{
        @BindByTag("eam")
        TextView eam;

        public TitleViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected void initBind() {
            super.initBind();

        }

        @Override
        protected int layoutId() {
            return R.layout.item_xj_task_item_title_view;
        }

        @Override
        protected void initView() {
            super.initView();

        }

        @Override
        protected void initListener() {
            super.initListener();
        }

        @Override
        protected void update(OLXJWorkItemEntity data) {
            eam.setText(data.eamID.name + "："+ data.part);
        }


    }
    class ViewHolder extends BaseRecyclerViewHolder<OLXJWorkItemEntity>{
        private AttachmentController mAttachmentController;
        private AttachmentDownloadController mAttachmentDownloadController;
        @BindByTag("content")
        TextView content;
        @BindByTag("result")
        TextView result;
        @BindByTag("galleryView")
        CustomGalleryView galleryView;

        public ViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected int layoutId() {
            return R.layout.item_xj_task_item_view;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void initListener() {
            super.initListener();
            galleryView.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    if (action == CustomGalleryView.ACTION_VIEW){
//                        CustomGalleryView customGalleryView = (CustomGalleryView) childView;
                        List<GalleryBean> galleryBeanList = galleryView.getGalleryAdapter().getList();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("images", (Serializable) FaultPicHelper.getImagePathList(galleryBeanList));
                        bundle.putInt("position", (Integer) obj);  //点击位置索引

                        int[] location = new int[2];
                        childView.getLocationOnScreen(location);  //点击图片的位置
                        bundle.putInt("locationX", location[0]);
                        bundle.putInt("locationY", location[1]);

                        bundle.putInt("width", DisplayUtil.dip2px(100, context));//必须
                        bundle.putInt("height", DisplayUtil.dip2px(100, context));//必须
                        ((Activity)context).getWindow().setWindowAnimations(R.style.fadeStyle);
                        IntentRouter.go(context, Constant.Router.IMAGE_VIEW, bundle);
                    }
                }
            });
        }

        @Override
        protected void update(OLXJWorkItemEntity data) {
            content.setText(String.format("%s%s", context.getResources().getString(R.string.content), data.content));
            if (data.realValue != null && OLXJConstant.MobileConclusion.AB_NORMAL.equals(data.realValue.id)){
                result.setTextColor(context.getResources().getColor(R.color.red));
                result.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_exception,null),null,null,null);
            }else {
                result.setTextColor(context.getResources().getColor(R.color.textColorlightblack));
                result.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            }
            result.setText(String.format("结果:%s", Util.strFormat2(data.concluse)));

            if (!TextUtils.isEmpty(data.attachmentId)){
                galleryView.setVisibility(View.VISIBLE);
                initAttachment(data,galleryView);
            }else {
                galleryView.setVisibility(View.GONE);
            }
        }

        private void initAttachment(OLXJWorkItemEntity data, CustomGalleryView galleryView) {
            if (mAttachmentController == null) {
                mAttachmentController = new AttachmentController();
            }
            if (mAttachmentDownloadController == null){
                mAttachmentDownloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_XJPATH);
            }

//        if (data.attachmentEntityList != null && data.attachmentEntityList.size() > 0){
//            downPic(data.attachmentEntityList, galleryView);
//            return;
//        }

            mAttachmentController.refreshGalleryView(new OnAPIResultListener<AttachmentListEntity>() {
                @Override
                public void onFail(String errorMsg) {
                    ToastUtils.show(context,errorMsg);
                }

                @Override
                public void onSuccess(AttachmentListEntity result) {
//                data.attachmentEntityList = result.result;
                    if (result.result.size() > 0) {
                        downPic(result.result, galleryView);
                    }
                }
            }, data.id);
        }

        private void downPic(List<AttachmentEntity> result, CustomGalleryView galleryView) {
            mAttachmentDownloadController.downloadPic(result, "mobileEAM_1.0.0_potrolTaskNew", new OnSuccessListener<List<GalleryBean>>() {
                @Override
                public void onSuccess(List<GalleryBean> result) {
                    galleryView.setGalleryBeans(result);
                }
            });
        }


    }


}
