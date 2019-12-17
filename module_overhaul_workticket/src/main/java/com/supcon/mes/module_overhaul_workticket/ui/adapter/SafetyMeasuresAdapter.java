package com.supcon.mes.module_overhaul_workticket.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_overhaul_workticket.R;
import com.supcon.mes.module_overhaul_workticket.constant.OperateType;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class SafetyMeasuresAdapter extends BaseListDataRecyclerViewAdapter<SafetyMeasuresEntity> {

    public SafetyMeasuresAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position, SafetyMeasuresEntity safetyMeasuresEntity) {
        return safetyMeasuresEntity.getType();
    }

    @Override
    protected BaseRecyclerViewHolder<SafetyMeasuresEntity> getViewHolder(int viewType) {
        if (viewType == OperateType.VIDEO.getType()){
            return new VideoSafetyMeasViewHolder(context);
        }else if (viewType == OperateType.PHOTO.getType()){
            return new PhotoSafetyMeasViewHolder(context);
        }else if (viewType == OperateType.NFC.getType()){
            return new NFCSafetyMeasViewHolder(context);
        }else {
            return new ConfirmSafetyMeasViewHolder(context);
        }
    }

    private class ConfirmSafetyMeasViewHolder extends BaseRecyclerViewHolder<SafetyMeasuresEntity> {
        @BindByTag("index")
        TextView index;
        @BindByTag("content")
        CustomTextView content;

        public ConfirmSafetyMeasViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_safety_measure;
        }

        @Override
        protected void update(SafetyMeasuresEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.getSafetyMeasure());
        }
    }
    private class VideoSafetyMeasViewHolder extends BaseRecyclerViewHolder<SafetyMeasuresEntity> {
        @BindByTag("index")
        TextView index;
        @BindByTag("content")
        CustomTextView content;

        public VideoSafetyMeasViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_video_safety_measure;
        }

        @Override
        protected void update(SafetyMeasuresEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.getSafetyMeasure());
        }
    }
    private class PhotoSafetyMeasViewHolder extends BaseRecyclerViewHolder<SafetyMeasuresEntity> {
        @BindByTag("index")
        TextView index;
        @BindByTag("content")
        CustomTextView content;

        public PhotoSafetyMeasViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_photo_safety_measure;
        }

        @Override
        protected void update(SafetyMeasuresEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.getSafetyMeasure());
        }
    }
    private class NFCSafetyMeasViewHolder extends BaseRecyclerViewHolder<SafetyMeasuresEntity> {
        @BindByTag("index")
        TextView index;
        @BindByTag("content")
        CustomTextView content;

        public NFCSafetyMeasViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_nfc_safety_measure;
        }

        @Override
        protected void update(SafetyMeasuresEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.getSafetyMeasure());
        }
    }

}
