package com.supcon.mes.module_sbda.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

import com.app.annotation.BindByTag;
import com.bumptech.glide.Glide;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.util.EAMStatusHelper;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.middleware.util.RequestOptionUtil;
import com.supcon.mes.module_sbda.R;

/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/4/2.
 */

public class SBDAListAdapter extends BaseListDataRecyclerViewAdapter<CommonDeviceEntity> {

    public SBDAListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<CommonDeviceEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<CommonDeviceEntity> implements View.OnClickListener {
//        @BindByTag("itemSBDADeviceIc")
        ImageView itemSBDADeviceIc;

        @BindByTag("itemSBDADeviceTitle")
        AppCompatTextView itemSBDADeviceTitle;

        @BindByTag("itemSBDADeviceCode")
        AppCompatTextView itemSBDADeviceCode;

        @BindByTag("itemSBDADeviceArea")
        AppCompatTextView getItemSBDADeviceArea;

        @BindByTag("eamStatus")
        AppCompatTextView eamStatus;

        @BindByTag("itemSBDADeviceType")
        AppCompatTextView itemSBDADeviceType;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
            itemSBDADeviceIc = itemView.findViewById(R.id.itemSBDADeviceIc);
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            onItemChildViewClick(v, position, getItem(position));
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sbda;           //获取列表自视图布局
        }

        @Override
        protected void update(CommonDeviceEntity entity) {
            itemSBDADeviceIc.setImageResource(R.drawable.ic_default_pic3);
            if(entity.eamPic!=null){
                Glide.with(context).load(entity.eamPic).apply(RequestOptionUtil.getEamRequestOptions(context)).into(itemSBDADeviceIc);
            }
            else {
                new EamPicController().initEamPic(itemSBDADeviceIc, entity.eamId);
            }

            itemSBDADeviceTitle.setText(entity.eamName);
            itemSBDADeviceCode.setText("["+entity.eamCode+"]");
            getItemSBDADeviceArea.setText(entity.installPlace);
            int statusBackgroundRes = R.drawable.eam_status_use;
            String status = entity.eamState;
            if (status.equals("01"))
                statusBackgroundRes = R.drawable.eam_status_use;
            else if (status.equals("04"))
                statusBackgroundRes = R.drawable.eam_status_drop;
            else if (status.equals("03"))
                statusBackgroundRes = R.drawable.eam_status_delay;
            else if (status.equals("02"))
                statusBackgroundRes = R.drawable.eam_status_stop;

            eamStatus.setText(EAMStatusHelper.getType(entity.eamState));
            eamStatus.setBackgroundResource(statusBackgroundRes);

//            itemSBDADeviceType.setText();
        }
    }
}

